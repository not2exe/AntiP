package com.example.antip.model

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Context.USAGE_STATS_SERVICE
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.antip.R
import java.util.*


class UsageTime() {
    private val arrayOfAll: ArrayList<App> = ArrayList<App>()

    fun getArrayListOfAllApps(): ArrayList<App> {
        return arrayOfAll

    }


    fun refreshTime(context: Context) {


        val start: Calendar = (Calendar.getInstance())
        start.add(Calendar.DAY_OF_MONTH, 0)
        start.set(Calendar.HOUR_OF_DAY, 0)
        start.set(Calendar.MINUTE, 0)
        start.set(Calendar.MILLISECOND, 0)
        val end: Calendar = Calendar.getInstance()


        val mapOfTime: Map<String, ArrayList<Long>> =
            getAppsInfo(context, start.timeInMillis, end.timeInMillis)
        val mapOfUseful = context.getSharedPreferences("nameOfUseful", Context.MODE_PRIVATE).all
        val mapOfHarmful = context.getSharedPreferences("nameOfHarmful", Context.MODE_PRIVATE).all
        arrayOfAll.clear()

        var temp: Int

        mapOfTime.keys.forEach {
            arrayOfAll.add(
                App(
                    getIconApp(context, it),
                    getName(context, it),
                    sumArray(mapOfTime[it]!!)
                )
            )
        }

    }

    private fun getAppsInfo(
        context: Context,
        startTime: Long,
        endTime: Long
    ): HashMap<String, ArrayList<Long>> {
        var currentEvent: UsageEvents.Event
        val allEvents: ArrayList<UsageEvents.Event> = ArrayList()
        val map: HashMap<String, ArrayList<Long>> = HashMap()
        val mUsageStatsManager =
            (context.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager)

        val usageEvents = mUsageStatsManager.queryEvents(startTime, endTime)

        while (usageEvents.hasNextEvent()) {
            currentEvent = UsageEvents.Event()
            usageEvents.getNextEvent(currentEvent)
            val packageName = currentEvent.packageName
            if (currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED || currentEvent.eventType == UsageEvents.Event.ACTIVITY_PAUSED || currentEvent.eventType == UsageEvents.Event.ACTIVITY_STOPPED) {
                allEvents.add(currentEvent)
            }
        }

        for (i in 0 until allEvents.size - 1) {
            val event0 = allEvents[i]
            val event1 = allEvents[i + 1]

            if (event0.eventType == UsageEvents.Event.ACTIVITY_RESUMED &&
                (event1.eventType == UsageEvents.Event.ACTIVITY_PAUSED || event1.eventType == UsageEvents.Event.ACTIVITY_STOPPED)
                && event0.packageName == event1.packageName
            ) {
                val diff = event1.timeStamp - event0.timeStamp
                if (map.containsKey(event0.packageName)) {
                    map[event0.packageName]?.add(diff)

                } else {
                    map[event0.packageName] = arrayListOf(diff)
                }


            }
        }


        return map

    }

    private fun getIconApp(context: Context, packageName: String): Drawable {
        return try {
            context.packageManager.getApplicationIcon(packageName)
        } catch (e: PackageManager.NameNotFoundException) {
            ContextCompat.getDrawable(context, R.drawable.undefined)!!
        }


    }

    private fun getName(context: Context, packageName: String): String {
        val apps: List<ApplicationInfo> = context.packageManager.getInstalledApplications(0)
        for (i in apps.indices) {
            if (apps[i].packageName == packageName)
                return apps[i].loadLabel(context.packageManager).toString()

        }
        return "gTime"
    }

    private fun sumArray(arr: ArrayList<Long>): Int {
        var result: Long = 0
        arr.forEach {
            result += it

        }
        return result.toInt() * 314 / (60000 * 50)

    }
}



