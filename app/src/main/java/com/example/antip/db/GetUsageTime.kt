package com.example.antip.db

import android.app.Service
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.antip.App
import com.example.antip.R
import java.util.*
@RequiresApi(Build.VERSION_CODES.Q)
class GetUsageTime : Service() {
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Send a notification that service is started
        val database= Room.databaseBuilder(
            this,
            UsageTimeDatabase::class.java,
            "usage_time_table"
        ).build()
        val usageTimeDao = database.usageTimeDao()



        // Do a periodic task
        mHandler = Handler()
        mRunnable = Runnable { getAppsInfoToDb(usageTimeDao) }
        mHandler.postDelayed(mRunnable, 5000)

        return START_STICKY
    }

    fun getAppsInfoToDb(usageTimeDao:UsageTimeDao) {
        val usageStatsManager: UsageStatsManager =getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        val lzero:Long=0


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
                    getIconApp(queryUsageStats[i].packageName),
                    queryUsageStats[i].packageName,
                    (queryUsageStats[i].totalTimeVisible)
                )
                usageTimeDao.insertScore(app)
                



            }
        }
    }

    private fun getIconApp(packageName:String): Drawable {
        try {
            val icon: Drawable = packageManager.getApplicationIcon(packageName)
            return icon
        }
        catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            Log.d("NameNotFoundException","yes")

        }
        return getDrawable(R.drawable.app1)!!

    }
}