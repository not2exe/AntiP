package com.gtime.offline_mode

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gtime.general.Cache
import javax.inject.Inject

class IncLifeWorker @Inject constructor(
    applicationContext: Context,
    params: WorkerParameters,
    private val cache: Cache
) : CoroutineWorker(applicationContext, params) {
    override suspend fun doWork(): Result {
        cache.incLives()
        return Result.success()
    }
}