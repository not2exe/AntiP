package com.gtime.domain

import android.content.Context
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.gtime.Constants
import com.gtime.DailyStatsWorker
import com.gtime.IncLifeWorker
import dagger.Module
import dagger.Provides
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
interface WorkerModule {
    companion object {
        @Provides
        @AppScope
        @Named(Constants.DAILY_STATS_WORKER)
        fun provideDailyRequest(): PeriodicWorkRequest {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR, 23)
            cal.set(Calendar.MINUTE, 30)
            return PeriodicWorkRequestBuilder<DailyStatsWorker>(
                repeatInterval = repeatDailyInterval,
                repeatIntervalTimeUnit = TimeUnit.HOURS,
                flexTimeInterval = flexTimeDailyInterval,
                flexTimeIntervalUnit = TimeUnit.MINUTES
            ).setInitialDelay(
                cal.timeInMillis - Calendar.getInstance().timeInMillis,
                TimeUnit.MILLISECONDS
            ).build()
        }

        @Provides
        @AppScope
        @Named(Constants.INC_LIFE_WORKER)
        fun provideIncRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<IncLifeWorker>(
                repeatInterval = repeatIncInterval,
                repeatIntervalTimeUnit = TimeUnit.DAYS,
                flexTimeInterval = flexTimeIncInterval,
                flexTimeIntervalUnit = TimeUnit.HOURS
            ).build()
        }

        @Provides
        @AppScope
        fun provideWorker(context: Context): WorkManager = WorkManager.getInstance(context)

        private const val repeatDailyInterval = 24L
        private const val flexTimeDailyInterval = 15L

        private const val repeatIncInterval = 3L
        private const val flexTimeIncInterval = 1L
    }
}