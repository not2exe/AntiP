package com.gtime.general.app.modules

import android.content.Context
import androidx.room.Room
import com.gtime.general.Constants
import com.gtime.general.model.db.AppTableDao
import com.gtime.general.model.db.DailyStatsOfflineDao
import com.gtime.general.model.db.DailyStatsOnlineDao
import com.gtime.general.model.db.DbClass
import com.gtime.general.scopes.AppScope
import dagger.Module
import dagger.Provides

@Module
interface LocalStoragesModule {
    companion object {
        @AppScope
        @Provides
        fun provideDailyStatsDatabase(context: Context): DbClass =
            Room.databaseBuilder(
                context = context,
                klass = DbClass::class.java,
                name = Constants.MAIN_DB,
            ).build()

        @AppScope
        @Provides
        fun provideDailyStatsOfflineDao(dbClass: DbClass): DailyStatsOfflineDao =
            dbClass.dailyStatsOfflineDao()

        @AppScope
        @Provides
        fun provideDailyStatsOnlineDao(dbClass: DbClass): DailyStatsOnlineDao =
            dbClass.dailyStatsOnlineDao()

        @AppScope
        @Provides
        fun provideAppTable(dbClass: DbClass): AppTableDao =
            dbClass.appTableDao()
    }
}