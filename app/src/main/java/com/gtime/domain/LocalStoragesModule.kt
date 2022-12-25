package com.gtime.domain

import android.content.Context
import androidx.room.Room
import com.gtime.Constants
import com.gtime.model.Cache
import com.gtime.model.db.AppDataBaseEntity
import com.gtime.model.db.AppTableDao
import com.gtime.model.db.DailyStatsDao
import com.gtime.model.db.DbClass
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
interface LocalStoragesModule {
    companion object {
        @AppScope
        @Provides
        fun provideDailyStatsDatabase(context: Context): DbClass =
            Room.databaseBuilder(
                context,
                DbClass::class.java,
                Constants.SCORE_TABLE
            ).build()

        @AppScope
        @Provides
        fun provideDailyStatsDao(dbClass: DbClass):DailyStatsDao =
            dbClass.dailyStatsDao()

        @AppScope
        @Provides
        fun provideAppTable(dbClass: DbClass):AppTableDao =
            dbClass.appTableDao()
    }
}