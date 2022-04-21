package com.example.antip.db

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
import androidx.core.content.ContextCompat.getDrawable
import com.example.antip.App
import com.example.antip.R

import java.util.*
import kotlin.collections.ArrayList


class GetUsageTime()  {
    @RequiresApi(Build.VERSION_CODES.Q)
    fun getAppsInfo(context: Context):ArrayList<App> {
        val usageStatsManager: UsageStatsManager =context.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        val lzero:Long=0
        var Apps= arrayListOf<App>()
        val cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH,-1)
        val queryUsageStats:List<UsageStats> = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            cal.timeInMillis,
            System.currentTimeMillis()
        )
        for (i in queryUsageStats.indices){
            if (queryUsageStats[i].totalTimeVisible != lzero)
            {
                val app = App(
                    getIconApp(context,queryUsageStats[i].packageName),
                    queryUsageStats[i].packageName,
                    (queryUsageStats[i].totalTimeVisible)
                )
                Log.d("123",app.toString())
                Apps.add(app)


                



            }
        }
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


}