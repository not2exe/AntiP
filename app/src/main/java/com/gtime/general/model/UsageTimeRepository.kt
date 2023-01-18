package com.gtime.general.model

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gtime.general.Constants
import com.gtime.general.KindOfApps
import com.gtime.general.model.dataclasses.AppEntity
import com.gtime.general.model.db.AppDataBaseEntity
import com.gtime.general.model.db.AppTableDao
import com.gtime.general.scopes.AppScope
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
    val toxicApps = MutableLiveData<List<AppEntity>>()
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
            when (appDataBaseEntity.kindOfApp) {
                KindOfApps.TOXIC -> {
                    var list = getMutableList(toxicApps)
                    list = findAndReplace(list, appDataBaseEntity) ?: return@withContext
                    toxicApps.postValue(list)
                }
                KindOfApps.USEFUL -> {
                    var list = getMutableList(usefulApps)
                    list = findAndReplace(list, appDataBaseEntity) ?: return@withContext
                    usefulApps.postValue(list)
                }
                KindOfApps.NEUTRAL -> {
                    var list = getMutableList(neutralApps)
                    list = findAndReplace(list, appDataBaseEntity) ?: return@withContext
                    neutralApps.postValue(list)
                }
            }
            appDao.insert(appDataBaseEntity)
        }

    private fun findAndReplace(
        list: MutableList<AppEntity>,
        appDataBaseEntity: AppDataBaseEntity
    ): MutableList<AppEntity>? {
        val appEnt = list.find { it.packageName == appDataBaseEntity.packageName }
            ?: return null
        if (appEnt.multiplier == appDataBaseEntity.multiplier) return null
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
                putIntoUseful(appEntity, multiplier)
            }
            KindOfApps.TOXIC -> {
                multiplier = abs(appEntity.multiplier) * -1
                putIntoHarmful(appEntity, multiplier)
            }
            KindOfApps.NEUTRAL -> {
                multiplier = appEntity.multiplier
                putIntoOthers(appEntity, multiplier)
            }
        }
        appDao.insert(AppDataBaseEntity(packageName, kindOfApps, multiplier))
    }


    suspend fun refreshUsageApps() = withContext(Dispatchers.IO) {
        val useful = getMutableList(usefulApps)
        val harmful = getMutableList(toxicApps)
        val neutral = getMutableList(neutralApps)
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
            val time = mapOfTime[packageName] ?: 0
            val scores = toScore(time)
            val percents: Int = ((time.toDouble() / scoresOfAll.toDouble()) * 100).roundToInt()
            var elemInList = removeAndReturn(useful, packageName)
            if (elemInList != null) {
                handleAdd(useful, percents, scores, elemInList)
                uiScores += (scores * elemInList.multiplier).roundToInt()
            }
            elemInList = removeAndReturn(harmful, packageName)
            if (elemInList != null) {
                handleAdd(harmful, percents, scores, elemInList)
                uiScores += (scores * elemInList.multiplier).roundToInt()
            }
            elemInList = removeAndReturn(neutral, packageName)
            if (elemInList != null) {
                handleAdd(neutral, percents, scores, elemInList)
            }
        }
        uiGeneralScores.postValue(uiScores)
        toxicApps.postValue(harmful)
        usefulApps.postValue(useful)
        neutralApps.postValue(neutral)
    }


    private suspend fun refreshAll() = withContext(Dispatchers.IO) {
        val listOfAllOnDevice =
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        for (i in listOfAllOnDevice.indices) {
            if (!appDao.isAppInDatabase(listOfAllOnDevice[i].packageName) &&
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
                KindOfApps.TOXIC -> {
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
                KindOfApps.NEUTRAL -> {
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
        toxicApps.postValue(harmful)
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

    private fun handleAdd(
        list: MutableList<AppEntity>,
        percents: Int,
        scores: Int,
        elem: AppEntity
    ) {
        list.add(
            AppEntity(
                name = elem.name,
                packageName = elem.packageName,
                image = elem.image,
                percentsOsGeneral = percents,
                _scores = scores,
                kindOfApps = elem.kindOfApps,
                multiplier = elem.multiplier
            )
        )
    }

    private fun removeAndReturn(list: MutableList<AppEntity>, packageName: String): AppEntity? {
        var entity: AppEntity? = null
        for (i in 0 until list.size) {
            if (list[i].packageName == packageName) {
                entity = list[i]
                list.removeAt(i)
                break
            }
        }
        return entity
    }

    private suspend fun getMutableList(liveData: MutableLiveData<List<AppEntity>>): MutableList<AppEntity> =
        withContext(Dispatchers.Main) {
            (liveData.value ?: emptyList()).toMutableList()
        }

    private fun toScore(scores: Long): Int = (scores / 10000).toInt()

    private suspend fun putIntoHarmful(appEntity: AppEntity, multiplier: Double) =
        withContext(Dispatchers.IO) {
            val list = getMutableList(toxicApps)
            list.add(
                AppEntity(
                    image = appEntity.image,
                    name = appEntity.name,
                    packageName = appEntity.packageName,
                    kindOfApps = appEntity.kindOfApps,
                    percentsOsGeneral = appEntity.percentsOsGeneral,
                    _scores = appEntity.scores,
                    multiplier = multiplier
                )
            )
            toxicApps.postValue(list)
        }

    private suspend fun putIntoUseful(appEntity: AppEntity, multiplier: Double) =
        withContext(Dispatchers.IO) {
            val list = getMutableList(usefulApps)
            list.add(
                AppEntity(
                    image = appEntity.image,
                    name = appEntity.name,
                    packageName = appEntity.packageName,
                    kindOfApps = appEntity.kindOfApps,
                    percentsOsGeneral = appEntity.percentsOsGeneral,
                    _scores = appEntity.scores,
                    multiplier = multiplier
                )
            )
            usefulApps.postValue(list)
        }

    private suspend fun putIntoOthers(appEntity: AppEntity, multiplier: Double) =
        withContext(Dispatchers.IO) {
            val list = getMutableList(neutralApps)
            list.add(
                AppEntity(
                    image = appEntity.image,
                    name = appEntity.name,
                    packageName = appEntity.packageName,
                    kindOfApps = appEntity.kindOfApps,
                    percentsOsGeneral = appEntity.percentsOsGeneral,
                    _scores = appEntity.scores,
                    multiplier = multiplier
                )
            )
            neutralApps.postValue(list)
        }

    suspend fun removeFromHarmful(appEntity: AppEntity) = withContext(Dispatchers.IO) {
        val list = getMutableList(toxicApps).filter { it != appEntity }
        toxicApps.postValue(list)
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



