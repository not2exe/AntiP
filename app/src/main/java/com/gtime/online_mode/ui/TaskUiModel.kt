package com.gtime.online_mode.ui

import com.gtime.general.Entity

data class TaskUiModel(
    val id: String,
    val award: Int,
    val description: String,
    val state: StateOfTask?
) : Entity(id)