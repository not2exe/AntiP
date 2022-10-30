package com.gtime

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import javax.inject.Inject

class App : Application() {
    val appComponent by lazy { DaggerAppComponent.factory().create(applicationContext) }

    @Inject
    lateinit var request: PeriodicWorkRequest

    @Inject
    lateinit var workManager: WorkManager

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        workManager.enqueueUniquePeriodicWork("", ExistingPeriodicWorkPolicy.KEEP, request)
    }
}