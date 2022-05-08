package com.example.antip.db

import android.app.usage.UsageEvents
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Context.USAGE_STATS_SERVICE
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import com.example.antip.App
import com.example.antip.R
import java.util.*
import kotlin.collections.ArrayList


class UsageTime()  {
    private val nameOfUsefulApps = arrayOf(
        "CoolReader", "InShot", "KineMaster", "PicsArt", "Strava",
        "Sleep Cycle", "Daylio", "Calm", "Seven", "Brainly",
        "English", "TED", "GitHub", "Canva", "WolframAlpha",
        "Кинопоиск", "Webinar", "Kapersky", "Lumosicty", "AccuBatery",
        "GetCourse", "MyBook"
    )
    private val nameOfHarmfulApps = arrayOf(
        "TikTok", "Instagram", "Facebook", "Zoom", "Netflix",
        "YouTube", "Twitter", "Pinterest", "Snapchat", "WhatsApp",
        "Reddit", "Twitch", "VK", "Spotify", "Hearthstone", "Discord"
    )

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
                var isRepeat=false

                val app = App(
                    getIconApp(context,queryUsageStats[i].packageName),
                    getName(context,queryUsageStats[i].packageName),
                    ((queryUsageStats[i].totalTimeInForeground/1000)).toInt()
                )
                for (i in 0 until Apps.size){
                    if(Apps[i].name==app.name){
                        Apps[i].scores+=app.scores
                        isRepeat=true
                    }
                }
                if(!isRepeat)
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

        }
        return context.getDrawable(R.drawable.undefined)!!


    }
    private fun getName(context: Context,packageName: String): String{
        val Apps:List<ApplicationInfo> =context.packageManager.getInstalledApplications(0)
        for (i in Apps.indices){
            if (Apps[i].packageName==packageName)
                return Apps[i].loadLabel(context.packageManager).toString()

        }
        return "gTime"

    }



    fun getAppsInfoV2(context: Context, startTime:Long, endTime:Long):HashMap<String,ArrayList<Long>> {
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
                if(map.containsKey(event0.packageName)){
                    map[event0.packageName]?.add(diff)

                }
                else{
                    map[event0.packageName]= arrayListOf(diff)
                }


            }
        }


        return map

    }



}

