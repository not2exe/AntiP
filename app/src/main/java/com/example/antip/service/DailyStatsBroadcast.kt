package com.example.antip.service

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import androidx.room.Room
import com.example.antip.model.UsageTime
import com.example.antip.model.db.DailyStatsDao
import com.example.antip.model.db.DailyStatsDatabase
import com.example.antip.model.db.DailyStatsEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class DailyStatsBroadcast : BroadcastReceiver() {
    private val myScope = CoroutineScope(Job())

    @SuppressLint("InvalidWakeLockTag")
    override fun onReceive(context: Context?, intent: Intent?) {
        val pm: PowerManager = context?.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl: PowerManager.WakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TAKE_STATS")
        wl.acquire(10 * 60 * 1000L /*10 minutes*/)

        val usageTime: UsageTime = UsageTime()
        Log.d("Service", "Service is started")
        val database =
            Room.databaseBuilder(context, DailyStatsDatabase::class.java, "stats_table")
                .build()
        val dailyStatsDao: DailyStatsDao = database.dailyStatsDao()
        usageTime.refreshTime(context)

        myScope.launch {
            dailyStatsDao.insertStats(
                DailyStatsEntry(formatDate(Calendar.getInstance()), 0)
            )

        }
        wl.release()


    }

    fun setAlarm(context: Context) {
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.HOUR, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyStatsBroadcast::class.java)
        val pi = PendingIntent.getBroadcast(context, 0, intent, 0)
        am.setRepeating(
            AlarmManager.RTC_WAKEUP,
            cal.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pi
        )
    }


    private fun formatDate(calendar: Calendar): String {
        val date:Date=calendar.time
        val dateFormat: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(date)

    }
}
