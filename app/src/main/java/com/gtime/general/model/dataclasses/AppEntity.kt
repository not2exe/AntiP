package com.gtime.general.model.dataclasses

import android.graphics.drawable.Drawable
import com.gtime.general.KindOfApps
import com.gtime.general.Entity


data class AppEntity(
    val image: Drawable?,
    override val packageName: String?,
    val name:String,
    val kindOfApps: KindOfApps,
    val percentsOsGeneral: Int = 0,
    private val _scores: Int = 0,
    val multiplier: Double = 1.0,
) : Entity(packageName) {
    val scores = _scores
        get() = (field * multiplier).toInt()
}
