package com.gtime.online_mode.data.model

data class AccountInfoModel(
    val name: String,
    val email: String,
    val urlAvatar: String,
    val isFirebaseAuth: Boolean = false
)
