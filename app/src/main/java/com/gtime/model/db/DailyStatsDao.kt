package com.gtime.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gtime.Constants

@Dao
interface DailyStatsDao {

    @Query("SELECT * FROM ${Constants.SCORE_TABLE} WHERE date = :aDate ")
    suspend fun getStatsByDate(aDate: Long): DailyStatsEntry

    @Query("SELECT * FROM ${Constants.SCORE_TABLE}")
    suspend fun getAllStats():List<DailyStatsEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(dailyStatsEntry: DailyStatsEntry)

}