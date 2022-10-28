package com.antip

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.antip.model.Cache
import com.antip.model.UsageTime
import com.antip.model.db.DailyStatsDao
import com.antip.model.db.DailyStatsEntry
import java.util.*
import javax.inject.Inject

class MyWorker @Inject constructor(
    applicationContext: Context,
    params: WorkerParameters,
    private val usageTime: UsageTime,
    private val dao: DailyStatsDao,
    private val cache:Cache
) : CoroutineWorker(applicationContext, params) {
    override suspend fun doWork(): Result {
        usageTime.refreshTime()
        dao.insertStats(DailyStatsEntry(Calendar.getInstance().timeInMillis, usageTime.scoresAll))
        //TODO
//        if (cache.getFromBoolean("HardcoreMode")) {
//            cache.inputIntoBoolean("isLostHardcore", usageTime.getScores() < 0)
//            cache.inputIntoBoolean("AchievementHardcore", usageTime.getScores() >= 0)
        return Result.success()
    }

}
