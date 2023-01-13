package com.gtime.model

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import com.gtime.Constants
import com.gtime.KindOfApps
import com.gtime.domain.AppScope
import com.gtime.model.dataclasses.AppEntity
import com.gtime.model.db.AppDataBaseEntity
import com.gtime.model.db.AppTableDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.roundToInt


@AppScope
class UsageTimeRepository @Inject constructor(
    private val appDao: AppTableDao,
    private val usageStatsManager: UsageStatsManager,
    private val packageManager: PackageManager,
    private val scope: CoroutineScope
) {
    val usefulApps = MutableLiveData<List<AppEntity>>()
    val harmfulApps = MutableLiveData<List<AppEntity>>()
    val neutralApps = MutableLiveData<List<AppEntity>>()
    val uiGeneralScores = MutableLiveData<Int>()

    init {
        scope.launch {
            refreshAll()
            fromDataBaseToRep()
            refreshUsageApps()
        }
    }


    suspend fun setMultiplier(appDataBaseEntity: AppDataBaseEntity) =
        withContext(Dispatchers.IO) {
            appDao.insert(appDataBaseEntity)
            when (appDataBaseEntity.kindOfApp) {
                KindOfApps.HARMFUL -> {
                    var list = getMutableList(harmfulApps)
                    list = findAndReplace(list, appDataBaseEntity)
                    harmfulApps.postValue(list)
                }
                KindOfApps.USEFUL -> {
                    var list = getMutableList(usefulApps)
                    list = findAndReplace(list, appDataBaseEntity)
                    usefulApps.postValue(list)
                }
                KindOfApps.OTHERS -> {
                    var list = getMutableList(neutralApps)
                    list = findAndReplace(list, appDataBaseEntity)
                    neutralApps.postValue(list)
                }
            }
        }

    private fun findAndReplace(
        list: MutableList<AppEntity>,
        appDataBaseEntity: AppDataBaseEntity
    ): MutableList<AppEntity> {
        val appEnt = list.find { it.packageName == appDataBaseEntity.packageName }
            ?: return list
        list.remove(appEnt)
        list.add(
            AppEntity(
                image = appEnt.image,
                packageName = appEnt.packageName,
                name = appEnt.name,
                kindOfApps = appEnt.kindOfApps,
                multiplier = appDataBaseEntity.multiplier,
                _scores = appEnt.scores
            )
        )
        return list
    }

    suspend fun put(appEntity: AppEntity, kindOfApps: KindOfApps) {
        val packageName = appEntity.packageName ?: ""
        val multiplier: Double
        when (kindOfApps) {
            KindOfApps.USEFUL -> {
                multiplier = abs(appEntity.multiplier)
                putIntoUseful(appEntity)
            }
            KindOfApps.HARMFUL -> {
                multiplier = abs(appEntity.multiplier) * -1
                putIntoHarmful(appEntity)
            }
            KindOfApps.OTHERS -> {
                multiplier = 0.0
                putIntoOthers(appEntity)
            }
        }
        appDao.insert(AppDataBaseEntity(packageName, kindOfApps, multiplier))
    }


    suspend fun refreshUsageApps() = withContext(Dispatchers.IO) {
        val useful = getMutableList(usefulApps)
        val harmful = getMutableList(harmfulApps)
        var uiScores = 0
        val start: Calendar = (Calendar.getInstance())
        start.set(Calendar.HOUR_OF_DAY, 0)
        start.set(Calendar.MINUTE, 0)
        start.set(Calendar.MILLISECOND, 0)
        val end: Calendar = Calendar.getInstance()
        val mapOfTime: Map<String, Long> =
            getAppsInfo(start.timeInMillis, end.timeInMillis)
        val scoresOfAll = mapOfTime[Constants.TIME_OF_ALL] ?: 0

        mapOfTime.keys.forEach { packageName ->
            val name = getName(packageName)
            val time = mapOfTime[packageName] ?: 0
            val scores = toScore(time)
            val icon = getIconApp(packageName)
            val percents: Int = ((time.toDouble() / scoresOfAll.toDouble()) * 100).roundToInt()
            if (useful.removeIf { it.packageName == packageName }) {
                useful.add(
                    AppEntity(
                        image = icon,
                        packageName = packageName,
                        name = name,
                        kindOfApps = KindOfApps.USEFUL,
                        percentsOsGeneral = percents,
                        _scores = scores,
                        multiplier = 1.0,
                    )
                )
                uiScores += scores
            }
            if (harmful.removeIf { it.packageName == packageName }) {
                harmful.add(
                    AppEntity(
                        image = icon,
                        packageName = packageName,
                        name = name,
                        kindOfApps = KindOfApps.HARMFUL,
                        percentsOsGeneral = percents,
                        _scores = scores,
                        multiplier = -1.0,
                    )
                )
                uiScores -= scores
            }
        }
        uiGeneralScores.postValue(uiScores)
        harmfulApps.postValue(harmful)
        usefulApps.postValue(useful)
    }


    private suspend fun refreshAll() = withContext(Dispatchers.IO) {
        val listOfAllOnDevice =
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        for (i in listOfAllOnDevice.indices) {
            if (appDao.getByName(listOfAllOnDevice[i].packageName) == null &&
                packageManager.getLaunchIntentForPackage(listOfAllOnDevice[i].packageName) != null
                && (listOfAllOnDevice[i].flags and ApplicationInfo.FLAG_SYSTEM) == 0
                && (listOfAllOnDevice[i].flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0
            ) {
                appDao.insert(
                    AppDataBaseEntity(
                        listOfAllOnDevice[i].packageName
                    )
                )
            }
        }
    }

    private suspend fun fromDataBaseToRep() = withContext(Dispatchers.IO) {
        val list = appDao.getAll()
        val neutral = mutableListOf<AppEntity>()
        val useful = mutableListOf<AppEntity>()
        val harmful = mutableListOf<AppEntity>()
        for (i in list.indices) {
            val icon = getIconApp(list[i].packageName)
            val name = getName(list[i].packageName)
            when (list[i].kindOfApp) {
                KindOfApps.HARMFUL -> {
                    harmful.add(
                        AppEntity(
                            icon,
                            list[i].packageName,
                            name,
                            list[i].kindOfApp,
                            multiplier = list[i].multiplier
                        )
                    )
                }
                KindOfApps.USEFUL -> {
                    useful.add(
                        AppEntity(
                            icon,
                            list[i].packageName,
                            name,
                            list[i].kindOfApp,
                            multiplier = list[i].multiplier
                        )
                    )
                }
                KindOfApps.OTHERS -> {
                    neutral.add(
                        AppEntity(
                            icon,
                            list[i].packageName,
                            name,
                            list[i].kindOfApp,
                            multiplier = list[i].multiplier
                        )
                    )
                }
            }
        }
        neutralApps.postValue(neutral)
        usefulApps.postValue(useful)
        harmfulApps.postValue(harmful)
    }

    private fun getAppsInfo(
        startTime: Long,
        endTime: Long
    ): HashMap<String, Long> {
        val allEvents: ArrayList<UsageEvents.Event> =
            getFilteredEvents(usageStatsManager.queryEvents(startTime, endTime))
        val map: HashMap<String, Long> = HashMap()

        for (i in 0 until allEvents.size - 1) {
            val event0 = allEvents[i]
            val event1 = allEvents[i + 1]
            if (event0.eventType == UsageEvents.Event.ACTIVITY_RESUMED &&
                (event1.eventType == UsageEvents.Event.ACTIVITY_PAUSED || event1.eventType == UsageEvents.Event.ACTIVITY_STOPPED)
                && event0.packageName == event1.packageName
            ) {
                val timeBetweenActions = (event1.timeStamp - event0.timeStamp)
                map[event0.packageName] =
                    (map[event0.packageName] ?: 0L) + timeBetweenActions
                map[Constants.TIME_OF_ALL] = (map[Constants.TIME_OF_ALL] ?: 0L) + timeBetweenActions
            }
        }
        return map
    }

    private fun getFilteredEvents(usageEvents: UsageEvents): ArrayList<UsageEvents.Event> {
        val allEvents = ArrayList<UsageEvents.Event>()
        var currentEvent: UsageEvents.Event
        while (usageEvents.hasNextEvent()) {
            currentEvent = UsageEvents.Event()
            usageEvents.getNextEvent(currentEvent)
            if (
                currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED
                || currentEvent.eventType == UsageEvents.Event.ACTIVITY_PAUSED
                || currentEvent.eventType == UsageEvents.Event.ACTIVITY_STOPPED
            ) {
                allEvents.add(currentEvent)
            }
        }
        return allEvents
    }

    private fun getIconApp(packageName: String): Drawable? = try {
        packageManager.getApplicationIcon(packageName)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }

    private fun getName(packageName: String): String = try {
        packageManager.getApplicationLabel(
            packageManager.getApplicationInfo(
                packageName,
                PackageManager.GET_META_DATA
            )
        ).toString()
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    }

    private suspend fun getMutableList(liveData: MutableLiveData<List<AppEntity>>): MutableList<AppEntity> =
        withContext(Dispatchers.Main) {
            (liveData.value ?: emptyList()).toMutableList()
        }

    private fun toScore(scores: Long): Int = (scores / 10000).toInt()

    private suspend fun putIntoHarmful(appEntity: AppEntity) = withContext(Dispatchers.IO) {
        val list = getMutableList(harmfulApps)
        list.add(appEntity)
        harmfulApps.postValue(list)
    }

    private suspend fun putIntoUseful(appEntity: AppEntity) = withContext(Dispatchers.IO) {
        val list = getMutableList(usefulApps)
        list.add(appEntity)
        usefulApps.postValue(list)
    }

    private suspend fun putIntoOthers(appEntity: AppEntity) = withContext(Dispatchers.IO) {
        val list = getMutableList(neutralApps)
        list.add(appEntity)
        neutralApps.postValue(list)
    }

    suspend fun removeFromHarmful(appEntity: AppEntity) = withContext(Dispatchers.IO) {
        val list = getMutableList(harmfulApps).filter { it != appEntity }
        harmfulApps.postValue(list)
    }

    suspend fun removeFromUseful(appEntity: AppEntity) = withContext(Dispatchers.IO) {
        val list = getMutableList(usefulApps).filter { it != appEntity }
        usefulApps.postValue(list)
    }

    suspend fun removeFromOthers(appEntity: AppEntity) = withContext(Dispatchers.IO) {
        val list = getMutableList(neutralApps).filter { it != appEntity }
        neutralApps.postValue(list)
    }
}



