package com.gtime.model.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [DailyStatsEntry::class, AppDataBaseEntity::class], version = 1)
abstract class DbClass : RoomDatabase() {
    abstract fun dailyStatsDao(): DailyStatsDao
    abstract fun appTableDao(): AppTableDao

}