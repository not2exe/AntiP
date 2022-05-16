package com.example.antip.model.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [DailyStatsEntry::class], version = 1)
abstract class DailyStatsDatabase:RoomDatabase() {
    abstract fun dailyStatsDao():DailyStatsDao

}