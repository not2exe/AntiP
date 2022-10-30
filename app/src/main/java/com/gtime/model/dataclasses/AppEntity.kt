package com.gtime.model.dataclasses

import android.graphics.drawable.Drawable
import com.gtime.adapters.Entity


data class AppEntity(val image: Drawable?, override val name: String?, var scores: Int):Entity(name)
