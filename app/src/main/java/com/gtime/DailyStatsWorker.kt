package com.gtime

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gtime.domain.AppScope
import com.gtime.model.Cache
import com.gtime.model.UsageTime
import com.gtime.model.db.DailyStatsDao
import com.gtime.model.db.DailyStatsEntry
import java.util.*
import javax.inject.Inject

@AppScope
class DailyStatsWorker @Inject constructor(
    applicationContext: Context,
    params: WorkerParameters,
    private val usageTime: UsageTime,
    private val dao: DailyStatsDao,
    private val cache: Cache
) : CoroutineWorker(applicationContext, params) {
    override suspend fun doWork(): Result {
        usageTime.refreshTime()
        dao.insertStats(DailyStatsEntry(Calendar.getInstance().timeInMillis, usageTime.scoresAll))
        if (usageTime.scoresAll < 0) {
            if (cache.getFromBoolean(Constants.KEY_HARDCORE_MODE)) {
                cache.decLife()
                cache.decLife()
                cache.decLife()
            } else {
                cache.decLife()
            }
        }
        return Result.success()
    }
}
