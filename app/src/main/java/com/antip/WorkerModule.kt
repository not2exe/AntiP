package com.antip

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import java.util.*
import java.util.concurrent.TimeUnit

@Module
interface WorkerModule {
    companion object {
        @Provides
        @AppScope
        fun provideRequest() {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR, 23)
            cal.set(Calendar.MINUTE, 30)
            PeriodicWorkRequestBuilder<MyWorker>(
                repeatInterval = repeatInterval,
                repeatIntervalTimeUnit = TimeUnit.HOURS,
                flexTimeInterval = flexTimeInterval,
                flexTimeIntervalUnit = TimeUnit.MINUTES
            ).setInitialDelay(
                cal.timeInMillis - Calendar.getInstance().timeInMillis,
                TimeUnit.MILLISECONDS
            ).build()
        }

        @Provides
        @AppScope
        fun provideWorker(context: Context): WorkManager = WorkManager.getInstance(context)

        private const val repeatInterval = 15L
        private const val flexTimeInterval = 30L
    }
}