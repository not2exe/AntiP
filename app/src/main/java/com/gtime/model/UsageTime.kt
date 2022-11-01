package com.gtime.model

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import com.gtime.domain.AppScope
import com.gtime.model.dataclasses.AppEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


@AppScope
class UsageTime @Inject constructor(
    private val cache: Cache,
    private val usageStatsManager: UsageStatsManager,
    private val packageManager: PackageManager
) {
    val arrayOfHarmful: ArrayList<AppEntity> = ArrayList<AppEntity>()
    val arrayOfUseful: ArrayList<AppEntity> = ArrayList<AppEntity>()
    var scoresAll: Int = 0


    fun refreshTime() {
        arrayOfHarmful.clear()
        arrayOfUseful.clear()
        scoresAll = 0
        val start: Calendar = (Calendar.getInstance())
        start.add(Calendar.DAY_OF_MONTH, 0)
        start.set(Calendar.HOUR_OF_DAY, 0)
        start.set(Calendar.MINUTE, 0)
        start.set(Calendar.MILLISECOND, 0)
        val end: Calendar = Calendar.getInstance()

        val mapOfTime: Map<String, Long> =
            getAppsInfo(start.timeInMillis, end.timeInMillis)
        val mapOfUseful = cache.getAllUseful()
        val mapOfHarmful = cache.getAllHarmful()
        arrayOfHarmful.clear()
        arrayOfUseful.clear()
        var appEntity: AppEntity
        mapOfTime.keys.forEach {
            appEntity = AppEntity(
                getIconApp(it),
                getName(it),
                toScore(mapOfTime.getOrDefault(it, 0))
            )
            when (appEntity.name) {
                in mapOfHarmful -> {
                    arrayOfHarmful.add(appEntity)
                    scoresAll -= appEntity.scores
                }
                in mapOfUseful -> {
                    arrayOfUseful.add(appEntity)
                    scoresAll += appEntity.scores
                }
            }
        }

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
                map[event0.packageName] =
                    map.getOrDefault(event0.packageName, 0L) + (event1.timeStamp - event0.timeStamp)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getApplicationLabel(
                packageManager.getApplicationInfo(
                    packageName,
                    PackageManager.ApplicationInfoFlags.of(0L)
                )
            ).toString()
        } else {
            packageManager.getApplicationLabel(
                packageManager.getApplicationInfo(
                    packageName,
                    PackageManager.GET_META_DATA
                )
            ).toString()
        }
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }

    suspend fun getAllApps(): List<AppEntity> = withContext(Dispatchers.IO) {
        val list = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0))
        } else {
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        }
        val rezList = ArrayList<AppEntity>()
        for (i in list.indices) {
            if (packageManager.getLaunchIntentForPackage(list[i].packageName) != null
                && (list[i].flags and ApplicationInfo.FLAG_SYSTEM) == 0
                && (list[i].flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0
            ) {
                rezList.add(
                    AppEntity(
                        getIconApp(list[i].packageName),
                        getName(list[i].packageName),
                        0
                    )
                )
            }
        }
        rezList
    }


    private fun toScore(scores: Long): Int = (scores / 10000).toInt()
}



