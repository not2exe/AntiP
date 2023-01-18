package com.gtime.general.model.db

import androidx.room.PrimaryKey
import com.gtime.general.Constants
import com.gtime.general.KindOfApps
import com.gtime.general.Entity

@androidx.room.Entity(tableName = Constants.APP_TABLE)
data class AppDataBaseEntity(
    @PrimaryKey override val packageName: String,
    val kindOfApp: KindOfApps = KindOfApps.NEUTRAL,
    val multiplier: Double = 1.0
) :
    Entity(packageName)