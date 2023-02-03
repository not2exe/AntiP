package com.gtime.general.app.modules

import android.app.usage.UsageStatsManager
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.gtime.general.scopes.AppScope
import dagger.Module
import dagger.Provides


@Module
interface SystemServicesModule {
    companion object {
        @AppScope
        @Provides
        fun provideUsageStatsManager(applicationContext: Context): UsageStatsManager =
            ContextCompat.getSystemService(applicationContext, UsageStatsManager::class.java)!!

        @AppScope
        @Provides
        fun providePackageManager(applicationContext: Context): PackageManager =
            applicationContext.packageManager

        @AppScope
        @Provides
        fun provideClipBoardManager(applicationContext: Context): ClipboardManager =
            ContextCompat.getSystemService(applicationContext, ClipboardManager::class.java)!!
    }
}