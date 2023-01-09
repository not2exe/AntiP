package com.gtime

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gtime.domain.AppScope
import com.gtime.model.Cache
import com.gtime.model.UsageTimeRepository
import com.gtime.model.db.DailyStatsDao
import com.gtime.model.db.DailyStatsEntry
import java.util.*
import javax.inject.Inject

@AppScope
class DailyStatsWorker @Inject constructor(
    applicationContext: Context,
    params: WorkerParameters,
    private val usageTimeRepository: UsageTimeRepository,
    private val dao: DailyStatsDao,
    private val cache: Cache
) : CoroutineWorker(applicationContext, params) {
    override suspend fun doWork(): Result {
        usageTimeRepository.refreshUsageApps()
        dao.insertStats(
            DailyStatsEntry(
                Calendar.getInstance().timeInMillis,
                usageTimeRepository.uiGeneralScores.value ?: 0
            )
        )
        if ((usageTimeRepository.uiGeneralScores.value ?: 0) < 0) {
            if (cache.getFromBoolean(Constants.KEY_HARDCORE_MODE)) {
                cache.decLives()
                cache.decLives()
                cache.decLives()
            } else {
                cache.decLives()
            }
        }
        return Result.success()
    }
}
