package com.example.antip.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.antip.App

@Dao
interface UsageTimeDao {
    @Query("SELECT * FROM usage_time_table ORDER BY  scores")
    fun getBestScores(): List<App>

    @Insert
    fun insertScore(app: App)

}