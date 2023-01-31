package com.gtime.online_mode.domain

import com.gtime.general.Entity

data class OfferUiModel(
    val documentId: String,
    val cost: Int,
    val description: String,
    val fullDescription: String,
    val urlOfferImage: String,
    val isAvailable: Boolean
) : Entity(documentId)
