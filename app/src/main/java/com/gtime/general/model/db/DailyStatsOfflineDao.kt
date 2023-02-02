package com.gtime.general.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gtime.general.Constants

@Dao
interface DailyStatsOfflineDao {

    @Query("SELECT * FROM ${Constants.SCORE_OFFLINE_TABLE} WHERE date = :aDate ")
    suspend fun getStatsByDate(aDate: Long): DailyStatsOffline

    @Query("SELECT * FROM ${Constants.SCORE_OFFLINE_TABLE}")
    suspend fun getAllStats():List<DailyStatsOffline>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(dailyStatsEntry: DailyStatsOffline)

}