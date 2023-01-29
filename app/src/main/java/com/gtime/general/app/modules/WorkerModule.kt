package com.gtime.general.app.modules

import android.content.Context
import android.util.Log
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.gtime.general.Constants
import com.gtime.general.scopes.AppScope
import com.gtime.offline_mode.DailyStatsWorker
import com.gtime.offline_mode.IncLifeWorker
import dagger.Module
import dagger.Provides
import java.text.DateFormat
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
            cal.set(Calendar.HOUR_OF_DAY, 23)
            cal.set(Calendar.MINUTE, 30)
            val delay =
                if (cal.timeInMillis > Calendar.getInstance().timeInMillis) cal.timeInMillis - Calendar.getInstance().timeInMillis-(3*3600000) else 0
            return PeriodicWorkRequestBuilder<DailyStatsWorker>(
                repeatInterval = repeatDailyInterval,
                repeatIntervalTimeUnit = TimeUnit.HOURS,
                flexTimeInterval = flexTimeDailyInterval,
                flexTimeIntervalUnit = TimeUnit.MINUTES
            ).setInitialDelay(
                delay,
                TimeUnit.MILLISECONDS
            ).addTag(Constants.DAILY_STATS_WORKER).build()
        }

        @Provides
        @AppScope
        @Named(Constants.INC_LIFE_WORKER)
        fun provideIncRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<IncLifeWorker>(
                repeatInterval = repeatIncInterval,
                repeatIntervalTimeUnit = TimeUnit.MINUTES,
            ).addTag(Constants.INC_LIFE_WORKER).build()
        }

        @Provides
        @AppScope
        fun provideWorker(context: Context): WorkManager = WorkManager.getInstance(context)

        private const val repeatDailyInterval = 24L
        private const val flexTimeDailyInterval = 15L

        private const val repeatIncInterval = 15L
        private const val flexTimeIncInterval = 15L
    }
}