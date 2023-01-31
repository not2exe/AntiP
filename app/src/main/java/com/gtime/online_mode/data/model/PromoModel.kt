package com.gtime.online_mode.data.model

data class PromoModel(val offerId: String, val promocodes: ArrayList<String>) {
    constructor() : this("", arrayListOf())
}
