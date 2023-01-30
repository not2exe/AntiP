package com.gtime.online_mode.data

import com.google.firebase.firestore.CollectionReference
import com.gtime.general.Constants
import com.gtime.general.scopes.AppScope
import javax.inject.Inject
import javax.inject.Named

@AppScope
class ShopRepository @Inject constructor(
    @Named(Constants.SHOP_COLLECTION) refShop: CollectionReference,
) {


}