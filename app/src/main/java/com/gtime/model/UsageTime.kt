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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.roundToInt


@AppScope
class UsageTime @Inject constructor(
    private val cache: Cache,
    @Named(Constants.CACHE_USEFUL) private val namesOfUseful: List<String>,
    @Named(Constants.CACHE_HARMFUL) private val nameOfsHarmful: List<String>,
    private val usageStatsManager: UsageStatsManager,
    private val packageManager: PackageManager,
    private val scope: CoroutineScope
) {
    val usefulApps = MutableLiveData<List<AppEntity>>()
    val harmfulApps = MutableLiveData<List<AppEntity>>()
    val neutralApps = MutableLiveData<List<AppEntity>>()


    val uiGeneralScores: MutableLiveData<Int> = MutableLiveData()

    init {
        scope.launch {
            refreshAll()
            refreshScores()
        }
    }

    fun putIntoHarmful(appEntity: AppEntity) {
        cache.inputIntoHarmful(appEntity.name ?: "")
        val list = (harmfulApps.value ?: emptyList()).toMutableList()
        list.add(appEntity)
        harmfulApps.value = list
    }

    fun putIntoUseful(appEntity: AppEntity) {
        cache.inputIntoUseful(appEntity.name ?: "")
        val list = (usefulApps.value ?: emptyList()).toMutableList()
        list.add(appEntity)
        usefulApps.value = list
    }

    fun putIntoOthers(appEntity: AppEntity) {
        val list = (neutralApps.value ?: emptyList()).toMutableList()
        list.add(appEntity)
        neutralApps.value = list
    }

    fun removeFromHarmful(appEntity: AppEntity) {
        cache.removeFromHarmful(appEntity.name ?: "")
        val list = harmfulApps.value?.filter { it != appEntity } ?: emptyList()
        harmfulApps.value = list
    }

    fun removeFromUseful(appEntity: AppEntity) {
        cache.removeFromUseful(appEntity.name ?: "")
        val list = usefulApps.value?.filter { it != appEntity } ?: emptyList()
        usefulApps.value = list
    }

    fun removeFromOthers(appEntity: AppEntity) {
        val list = neutralApps.value?.filter { it != appEntity } ?: emptyList()
        neutralApps.value = list
    }

    suspend fun refreshScores() = withContext(Dispatchers.IO) {
        val useful = getMutableList(usefulApps)
        val harmful = getMutableList(harmfulApps)
        var uiScores = 0
        val start: Calendar = (Calendar.getInstance())
        start.add(Calendar.DAY_OF_MONTH, 0)
        start.set(Calendar.HOUR_OF_DAY, 0)
        start.set(Calendar.MINUTE, 0)
        start.set(Calendar.MILLISECOND, 0)
        val end: Calendar = Calendar.getInstance()
        val mapOfTime: Map<String, Long> =
            getAppsInfo(start.timeInMillis, end.timeInMillis)
        val scoresOfAll = toScore(mapOfTime[Constants.TIME_OF_ALL] ?: return@withContext).toDouble()

        mapOfTime.keys.forEach { packageName ->
            val name = getName(packageName) ?: ""
            val scores = toScore(mapOfTime[packageName] ?: 0)
            val icon = getIconApp(packageName)
            val percents: Int = (scores.toDouble() / scoresOfAll).roundToInt() * 100
            if (useful.removeIf { it.name == name }) {
                useful.add(
                    AppEntity(
                        image = icon,
                        name = name,
                        kindOfApps = KindOfApps.USEFUL,
                        percentsOsGeneral = percents,
                        _scores = scores
                    )
                )
                uiScores += scores
            }
            if (harmful.removeIf { it.name == name }) {
                harmful.add(
                    AppEntity(
                        image = icon,
                        name = name,
                        kindOfApps = KindOfApps.HARMFUL,
                        percentsOsGeneral = percents,
                        _scores = scores
                    )
                )
                uiScores -= scores
            }
        }

        uiGeneralScores.postValue(uiScores)
        harmfulApps.postValue(harmful)
        usefulApps.postValue(useful)
    }

    suspend fun refreshAll() = withContext(Dispatchers.IO) {
        val listOfAll = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val neutral = mutableListOf<AppEntity>()
        val useful = mutableListOf<AppEntity>()
        val harmful = mutableListOf<AppEntity>()
        for (i in listOfAll.indices) {
            if (packageManager.getLaunchIntentForPackage(listOfAll[i].packageName) != null
                && (listOfAll[i].flags and ApplicationInfo.FLAG_SYSTEM) == 0
                && (listOfAll[i].flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0
            ) {
                val icon = getIconApp(listOfAll[i].packageName)
                when (val name = getName(listOfAll[i].packageName)) {
                    in nameOfsHarmful -> {
                        harmful.add(AppEntity(icon, name, KindOfApps.HARMFUL))
                    }
                    in namesOfUseful -> {
                        useful.add(AppEntity(icon, name, KindOfApps.USEFUL))
                    }
                    else -> {
                        neutral.add(AppEntity(icon, name, KindOfApps.OTHERS))
                    }
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

    private fun getName(packageName: String): String? = try {
        packageManager.getApplicationLabel(
            packageManager.getApplicationInfo(
                packageName,
                PackageManager.GET_META_DATA
            )
        ).toString()
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }

    private suspend fun getMutableList(liveData: MutableLiveData<List<AppEntity>>): MutableList<AppEntity> =
        withContext(Dispatchers.Main) {
            (liveData.value ?: emptyList()).toMutableList()
        }

    private fun toScore(scores: Long): Int = (scores / 10000).toInt()
}



