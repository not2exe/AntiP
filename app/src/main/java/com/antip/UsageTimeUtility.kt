package com.antip

import android.app.usage.UsageStatsManager
import android.content.Context
import dagger.Module
import dagger.Provides

@AppScope
@Module
interface UsageTimeUtility {
    @Provides
    fun provideUsageStatsManager(applicationContext: Context): UsageStatsManager =
        applicationContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    @Provides
    fun providePackageManager(applicationContext: Context) = applicationContext.packageManager
}