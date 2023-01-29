package com.gtime.offline_mode

import android.content.Context
import android.util.Log
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
        Log.d("WeAreTheChampions","MyFriiend")
        cache.incLives()
        return Result.success()
    }
}