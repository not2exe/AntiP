package com.gtime.general.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gtime.general.Constants

@Dao
interface DailyStatsOnlineDao {
    @Query("SELECT * FROM ${Constants.SCORE_ONLINE_TABLE} WHERE date = :aDate ")
    suspend fun getStatsByDate(aDate: Long): DailyStatsOnline

    @Query("SELECT * FROM ${Constants.SCORE_ONLINE_TABLE}")
    suspend fun getAllStats(): List<DailyStatsOnline>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(dailyStatsEntry: DailyStatsOnline)
}
