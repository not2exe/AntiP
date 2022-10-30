package com.gtime

import android.content.Context
import androidx.room.Room
import com.gtime.model.db.DailyStatsDatabase
import dagger.Module
import dagger.Provides

@Module
interface RoomModule {
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
    }
}