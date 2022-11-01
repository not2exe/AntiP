package com.gtime

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gtime.model.Cache
import javax.inject.Inject

class IncLifeWorker @Inject constructor(
    applicationContext: Context,
    params: WorkerParameters,
    private val cache: Cache
) : CoroutineWorker(applicationContext, params) {
    override suspend fun doWork(): Result {
        cache.incLife()
        return Result.success()
    }
}