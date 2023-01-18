package com.gtime.general.app.modules

import android.content.Context
import androidx.room.Room
import com.gtime.general.Constants
import com.gtime.general.scopes.AppScope
import com.gtime.general.model.db.AppTableDao
import com.gtime.general.model.db.DailyStatsDao
import com.gtime.general.model.db.DbClass
import dagger.Module
import dagger.Provides

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
        fun provideDailyStatsDao(dbClass: DbClass): DailyStatsDao =
            dbClass.dailyStatsDao()

        @AppScope
        @Provides
        fun provideAppTable(dbClass: DbClass): AppTableDao =
            dbClass.appTableDao()
    }
}