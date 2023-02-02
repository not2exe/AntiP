package com.gtime.offline_mode

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gtime.general.Cache
import com.gtime.general.Constants
import com.gtime.general.model.UsageTimeRepository
import com.gtime.general.model.db.DailyStatsOffline
import com.gtime.general.model.db.DailyStatsOfflineDao
import com.gtime.general.model.db.DailyStatsOnline
import com.gtime.general.model.db.DailyStatsOnlineDao
import com.gtime.general.scopes.AppScope
import java.util.*
import javax.inject.Inject


class DailyStatsWorker @Inject constructor(
    applicationContext: Context,
    params: WorkerParameters,
    private val usageTimeRepository: UsageTimeRepository,
    private val daoOffline: DailyStatsOfflineDao,
    private val daoOnline: DailyStatsOnlineDao,
    private val cache: Cache
) : CoroutineWorker(applicationContext, params) {
    override suspend fun doWork(): Result {
        if (usageTimeRepository.isNotInit()) {
            usageTimeRepository.refreshAll()
            usageTimeRepository.fromDataBaseToRep()
        }
        usageTimeRepository.refreshUsageApps()
        val date = Calendar.getInstance().timeInMillis
        val scores = usageTimeRepository.uiGeneralScores.value ?: 0
        if (cache.isOnline()) {
            daoOnline.insertStats(DailyStatsOnline(date, scores))
            return Result.success()
        }
        daoOffline.insertStats(
            DailyStatsOffline(date, scores)
        )
        if (scores < 0) {
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
