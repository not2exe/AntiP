package com.antip.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import androidx.room.Room
import com.example.antip.model.Cash
import com.example.antip.model.UsageTime
import com.example.antip.model.db.DailyStatsDatabase
import com.example.antip.model.db.DailyStatsEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*


class DailyStatsBroadcast :
    BroadcastReceiver() {
    private val myScope = CoroutineScope(Job())

    override fun onReceive(context: Context?, intent: Intent?) {
        val tag: String = "Tag:PW"
        val pm: PowerManager = context?.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl: PowerManager.WakeLock = pm.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            tag
        )
        wl.acquire(10 * 60 * 1000L /*10 minutes*/)

        val usageTime = UsageTime()

        usageTime.refreshTime(context)
        val database = Room.databaseBuilder(
            context,
            DailyStatsDatabase::class.java,
            "stats_table"
        ).build()
        val statsDao = database.dailyStatsDao()
        val cash: Cash = Cash(context)

        myScope.launch {
            statsDao.insertStats(
                DailyStatsEntry(Calendar.getInstance().timeInMillis, usageTime.getScores())
            )
            if (cash.getFromBoolean("HardcoreMode")) {
                cash.inputIntoBoolean("isLostHardcore", usageTime.getScores() < 0)
                cash.inputIntoBoolean("AchievementHardcore", usageTime.getScores() >= 0)

            }


        }
        wl.release()


    }

    fun setAlarm(context: Context) {
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.HOUR, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 0)
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


}
