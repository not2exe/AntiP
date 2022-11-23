package com.gtime.domain

import android.content.Context
import androidx.navigation.Navigator
import androidx.room.Room
import com.gtime.Constants
import com.gtime.model.Cache
import com.gtime.model.db.DailyStatsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
interface LocalStoragesModule {
    companion object {
        @AppScope
        @Provides
        fun provideDailyStatsDatabase(context: Context): DailyStatsDatabase =
            Room.databaseBuilder(
                context,
                DailyStatsDatabase::class.java,
                Constants.SCORE_TABLE
            ).build()

        @AppScope
        @Provides
        fun provideDailyStatsDao(dailyStatsDatabase: DailyStatsDatabase) =
            dailyStatsDatabase.dailyStatsDao()

        @Provides
        @Named(Constants.CACHE_HARMFUL)
        fun provideNamesOfHarmful(cache: Cache): List<String> = cache.getAllHarmful().map { it.toString() }.toList()

        @Provides
        @Named(Constants.CACHE_USEFUL)
        fun provideNamesOfUseful(cache: Cache): List<String> = cache.getAllUseful().map { it.toString() }.toList()
    }
}