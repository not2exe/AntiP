package com.antip

import android.app.usage.UsageStatsManager
import android.content.Context
import dagger.Module
import dagger.Provides


@Module
interface UsageTimeUtilityModule {
    companion object{
        @AppScope
        @Provides
        fun provideUsageStatsManager(applicationContext: Context): UsageStatsManager =
            applicationContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        @AppScope
        @Provides
        fun providePackageManager(applicationContext: Context) = applicationContext.packageManager
    }
}