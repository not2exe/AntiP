package com.gtime.general.app.modules

import android.content.Context
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.gtime.general.Constants
import com.gtime.general.scopes.AppScope
import com.gtime.offline_mode.DailyStatsWorker
import com.gtime.offline_mode.IncLifeWorker
import com.gtime.online_mode.TopScoresWorker
import dagger.Module
import dagger.Provides
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
interface WorkerModule {
    companion object {
        @Provides
        @Named(Constants.DAILY_STATS_WORKER)
        fun provideDailyRequest(): PeriodicWorkRequest {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 22)
            cal.set(Calendar.MINUTE, 30)
            val delay =
                if (cal.timeInMillis > Calendar.getInstance().timeInMillis) cal.timeInMillis - Calendar.getInstance().timeInMillis - (3 * 3600000) else 0
            return PeriodicWorkRequestBuilder<DailyStatsWorker>(
                repeatInterval = repeatDailyInterval,
                repeatIntervalTimeUnit = TimeUnit.HOURS,
                flexTimeInterval = flexTimeDailyInterval,
                flexTimeIntervalUnit = TimeUnit.HOURS
            ).setInitialDelay(
                delay,
                TimeUnit.MILLISECONDS
            ).addTag(Constants.DAILY_STATS_WORKER).build()
        }

        @Provides
        @Named(Constants.INC_LIFE_WORKER)
        fun provideIncRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<IncLifeWorker>(
                repeatInterval = repeatIncInterval,
                repeatIntervalTimeUnit = TimeUnit.DAYS,
                flexTimeInterval = flexTimeIncInterval,
                flexTimeIntervalUnit = TimeUnit.HOURS
            ).addTag(Constants.INC_LIFE_WORKER).build()
        }

        @Provides
        @Named(Constants.TOP_SCORES_WORKER)
        fun provideTopScoresRequest(): PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<TopScoresWorker>(
                repeatInterval = repeatScoresInterval,
                repeatIntervalTimeUnit = TimeUnit.HOURS,
                flexTimeInterval = flexTimeScoresInterval,
                flexTimeIntervalUnit = TimeUnit.MINUTES
            ).addTag(Constants.TOP_SCORES_WORKER).build()

        @Provides
        @AppScope
        fun provideWorker(context: Context): WorkManager = WorkManager.getInstance(context)

        private const val repeatDailyInterval = 24L
        private const val flexTimeDailyInterval = 1L

        private const val repeatIncInterval = 3L
        private const val flexTimeIncInterval = 1L

        private const val repeatScoresInterval = 1L
        private const val flexTimeScoresInterval = 30L
    }
}