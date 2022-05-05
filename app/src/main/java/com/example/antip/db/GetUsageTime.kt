package com.example.antip.db

import android.app.usage.UsageEvents
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Context.USAGE_STATS_SERVICE
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.antip.App
import com.example.antip.R
import java.text.SimpleDateFormat
import java.util.*


class GetUsageTime()  {

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getAppsInfo(context: Context):ArrayList<App> {
        val usageStatsManager: UsageStatsManager =context.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        val lzero:Long=0
        var Apps= arrayListOf<App>()
        val cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH,-1)


        val queryUsageStats:List<UsageStats> = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_BEST,
            cal.timeInMillis,
            System.currentTimeMillis()
        )




        for (i in queryUsageStats.indices){
            if (queryUsageStats[i].totalTimeVisible != lzero)
            {

                val app = App(
                    getIconApp(context,queryUsageStats[i].packageName),
                    queryUsageStats[i].packageName,
                    ((queryUsageStats[i].totalTimeInForeground/1000)).toInt()
                )

                Apps.add(app)


                



            }
        }
        Apps.sortByDescending{ it.scores }
        return Apps

    }

    private fun getIconApp(context: Context, packageName:String): Drawable {
        try {
            val icon: Drawable = context.packageManager.getApplicationIcon(packageName)
            return icon
        }
        catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            Log.d("NameNotFoundException","yes")

        }
        return ContextCompat.getDrawable(context, R.drawable.app1)!!


    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun getAppsInfoV2(context: Context, startTime:Long, endTime:Long):HashMap<String,AppUsageInfo> {
        var currentEvent: UsageEvents.Event
        val allEvents: ArrayList<UsageEvents.Event> = ArrayList()
        val map: HashMap<String, AppUsageInfo> = HashMap()
        val mUsageStatsManager =
            (context.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager)!!

        val usageEvents = mUsageStatsManager!!.queryEvents(startTime, endTime)

        while (usageEvents.hasNextEvent()) {
            currentEvent = UsageEvents.Event()
            usageEvents.getNextEvent(currentEvent)
            val packageName = currentEvent.packageName
            if (currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED || currentEvent.eventType == UsageEvents.Event.ACTIVITY_PAUSED || currentEvent.eventType == UsageEvents.Event.ACTIVITY_STOPPED) {
                allEvents.add(currentEvent) // an extra event is found, add to all events list.
                // taking it into a collection to access by package name
                if (!map.containsKey(packageName)) {
                    map[packageName] = AppUsageInfo()
                }
            }
        }

        for (i in 0 until allEvents.size - 1) {
            val event0 = allEvents[i]
            val event1 = allEvents[i + 1]

            //for launchCount of apps in time range
            if (event0.packageName != event1.packageName && event1.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                // if true, E1 (launch event of an app) app launched
                Objects.requireNonNull(map[event1.packageName])!!.launchCount++
            }

            //for UsageTime of apps in time range
            if (event0.eventType == UsageEvents.Event.ACTIVITY_RESUMED &&
                (event1.eventType == UsageEvents.Event.ACTIVITY_PAUSED || event1.eventType == UsageEvents.Event.ACTIVITY_STOPPED)
                && event0.packageName == event1.packageName
            ) {
                val diff = event1.timeStamp - event0.timeStamp
                Objects.requireNonNull(map[event0.packageName])!!.timeInForeground += diff
            }
        }
        // and return the map.

        return map

    }



}

class AppUsageInfo internal constructor() {
    var timeInForeground: Long = 0
    var launchCount = 0
}