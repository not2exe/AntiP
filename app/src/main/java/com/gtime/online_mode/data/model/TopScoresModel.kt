package com.gtime.online_mode.data.model

import com.gtime.general.Entity

data class TopScoresModel(
    val documentID: String,
    val scores: Int,
    val urlAvatar: String,
    val userDisplay: String
) : Entity(documentID) {
    constructor() : this("", 0, "", "")
}
