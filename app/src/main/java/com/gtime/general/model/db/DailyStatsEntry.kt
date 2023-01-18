package com.gtime.general.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gtime.general.Constants

@Entity(tableName = Constants.SCORE_TABLE)
data class DailyStatsEntry(
    @PrimaryKey val date: Long,
    val scores: Int
)
