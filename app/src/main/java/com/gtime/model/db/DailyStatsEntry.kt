package com.gtime.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stats_table")
data class DailyStatsEntry(
    @PrimaryKey val date: Long,
    val scores: Int
)
