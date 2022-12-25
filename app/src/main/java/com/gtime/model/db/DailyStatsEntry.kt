package com.gtime.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gtime.Constants

@Entity(tableName = Constants.SCORE_TABLE)
data class DailyStatsEntry(
    @PrimaryKey val date: Long,
    val scores: Int
)
