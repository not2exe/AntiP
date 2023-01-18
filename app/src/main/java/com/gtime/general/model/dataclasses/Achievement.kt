package com.gtime.general.model.dataclasses

import com.gtime.general.Entity

data class Achievement(val imageAchievement: Int, val description: String, val imageState: Int):
    Entity(description)
