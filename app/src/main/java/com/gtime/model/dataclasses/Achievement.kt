package com.gtime.model.dataclasses

import com.gtime.ui.adapters.Entity

data class Achievement(val imageAchievement: Int, val description: String, val imageState: Int):
    Entity(description)
