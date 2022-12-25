package com.gtime.app

import android.app.Application
import android.content.res.Configuration
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.gtime.Constants
import com.gtime.domain.DaggerAppComponent
import com.gtime.model.UsageTimeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class App : Application() {
    val appComponent by lazy { DaggerAppComponent.factory().create(this) }

    @Inject
    @Named(Constants.DAILY_STATS_WORKER)
    lateinit var dailyRequest: PeriodicWorkRequest

    @Inject
    @Named(Constants.INC_LIFE_WORKER)
    lateinit var incRequest: PeriodicWorkRequest

    @Inject
    lateinit var workManager: WorkManager

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        workManager.enqueueUniquePeriodicWork(
            Constants.DAILY_STATS_WORKER,
            ExistingPeriodicWorkPolicy.KEEP,
            dailyRequest
        )
        workManager.enqueueUniquePeriodicWork(
            Constants.INC_LIFE_WORKER,
            ExistingPeriodicWorkPolicy.KEEP,
            incRequest
        )
    }


}