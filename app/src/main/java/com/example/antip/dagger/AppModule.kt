package com.a1218v.binpo

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.antip.model.db.DailyStatsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: RoomDatabase.Callback
    ) = Room.databaseBuilder(
        app,
        DailyStatsDatabase::class.java,
        "stats_table"
    )
        .addCallback(callback)
        .build()

    @Provides
    fun provideTaskDao(db: DailyStatsDatabase) = db.dailyStatsDao()

}