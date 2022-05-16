package com.example.antip.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface DailyStatsDao {

    @Query("SELECT * FROM stats_table WHERE date = :aDate ")
    suspend fun getStatsByDate(aDate:String):DailyStatsEntry
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(dailyStatsEntry: DailyStatsEntry)
}