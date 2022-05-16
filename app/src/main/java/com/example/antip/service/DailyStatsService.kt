package com.example.antip.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.example.antip.model.UsageTime
import com.example.antip.model.db.DailyStatsDao
import com.example.antip.model.db.DailyStatsDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.*


class DailyStatsService() : Service() {
    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        Log.d("DailyStatsService","ServiceCreated")
        super.onCreate()
        onDailyStatsStart()


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }

    private fun onDailyStatsStart() {
        val usageTime: UsageTime = UsageTime()
        val startOfDay: Calendar = Calendar.getInstance()
        startOfDay.add(Calendar.DAY_OF_MONTH, 1)
        startOfDay.set(Calendar.HOUR_OF_DAY, 0)
        startOfDay.set(Calendar.MINUTE, 0)
        startOfDay.set(Calendar.MILLISECOND, 0)

        if (startOfDay == Calendar.getInstance()) {
            Log.d("Service","Service is started")
            val database =
                Room.databaseBuilder(this, DailyStatsDatabase::class.java, "stats_table")
                    .build()
            val dailyStatsDao: DailyStatsDao = database.dailyStatsDao()
            usageTime.refreshTime(this)

            //dailyStatsDao.insertStats(DailyStatsEntry(0,formatDate(),dailyStatsDao.getStatsByDate(formatDate()).scores))


        }
    }


    override fun onDestroy() {
        super.onDestroy()

        val broadcastIntent: Intent = Intent()
        broadcastIntent.action = "restartService"
        broadcastIntent.setClass(this, Restarter::class.java)

    }


    private fun formatDate(): String {
        val dateFormat: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(Date())

    }
}