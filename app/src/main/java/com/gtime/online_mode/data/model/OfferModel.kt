package com.gtime.online_mode.data.model

import com.google.firebase.firestore.PropertyName

data class OfferModel(
    var offerId: String,
    val cost: Int,
    val description: String,
    val fullDescription: String,
    val urlOfferImage: String,
) {
    constructor() : this("", 0, "", "", "")
}
