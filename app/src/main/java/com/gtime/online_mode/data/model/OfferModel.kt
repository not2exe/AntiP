package com.gtime.online_mode.data.model

data class OfferModel(
    val cost: Int,
    val description: String,
    val fullDescription: String,
    val offerID: String,
    val urlOfferImage: String,
) {
    constructor() : this(0, "", "", "","")
}
