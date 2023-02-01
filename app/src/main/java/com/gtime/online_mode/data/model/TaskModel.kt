package com.gtime.online_mode.data.model

data class TaskModel(val id: String, val award: Int, val description: String, val state: String) {
    constructor() : this("", 0, "", "")
}