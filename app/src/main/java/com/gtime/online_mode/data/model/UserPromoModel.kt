package com.gtime.online_mode.data.model

import com.gtime.general.Entity

data class UserPromoModel(val id: String, val promoModel: PromoModel, val offerModel: OfferModel) :
    Entity(id)
