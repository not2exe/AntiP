package com.gtime.general.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gtime.general.Constants

@Entity(tableName = Constants.SCORE_OFFLINE_TABLE)
data class DailyStatsOffline(
    @PrimaryKey override val date: Long,
    override val scores: Int
) :
    DailyStatsEntry(date, scores)