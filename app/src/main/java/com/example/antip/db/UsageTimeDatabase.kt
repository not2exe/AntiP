package com.example.antip.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.antip.App

@Database(entities = [App::class], version = 1)
abstract class UsageTimeDatabase: RoomDatabase() {
    abstract fun usageTimeDao(): UsageTimeDao
}