package com.gtime.model.db

import androidx.room.PrimaryKey
import com.gtime.Constants
import com.gtime.KindOfApps
import com.gtime.ui.adapters.Entity

@androidx.room.Entity(tableName = Constants.APP_TABLE)
data class AppDataBaseEntity(
    @PrimaryKey override val packageName: String,
    val kindOfApp: KindOfApps = KindOfApps.OTHERS,
    val multiplier: Double = 1.0
) :
    Entity(packageName)