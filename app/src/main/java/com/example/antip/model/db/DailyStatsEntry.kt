package com.example.antip.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "stats_table")
data class DailyStatsEntry(
    @PrimaryKey val date: Long,
    val scores: Int
)
