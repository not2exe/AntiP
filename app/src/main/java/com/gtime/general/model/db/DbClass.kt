package com.gtime.general.model.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [DailyStatsOnline::class, AppDataBaseEntity::class, DailyStatsOffline::class],
    version = 1
)
abstract class DbClass : RoomDatabase() {
    abstract fun dailyStatsOfflineDao(): DailyStatsOfflineDao
    abstract fun appTableDao(): AppTableDao
    abstract fun dailyStatsOnlineDao(): DailyStatsOnlineDao
}