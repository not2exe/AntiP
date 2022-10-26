package com.antip.model.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [DailyStatsEntry::class], version = 1)
abstract class DailyStatsDatabase:RoomDatabase() {
    abstract fun dailyStatsDao():DailyStatsDao

}