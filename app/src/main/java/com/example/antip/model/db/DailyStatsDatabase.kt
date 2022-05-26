package com.example.antip.model.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import javax.inject.Inject
import javax.inject.Provider


@Database(entities = [DailyStatsEntry::class], version = 1)
abstract class DailyStatsDatabase:RoomDatabase() {
    abstract fun dailyStatsDao():DailyStatsDao

    class CallBack @Inject constructor(
        private val database:Provider<DailyStatsDatabase>
    ):RoomDatabase.Callback()

}