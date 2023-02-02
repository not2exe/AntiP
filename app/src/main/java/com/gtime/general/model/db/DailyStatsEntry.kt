package com.gtime.general.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gtime.general.Constants


open class DailyStatsEntry(
    open val date: Long,
    open val scores: Int
)
