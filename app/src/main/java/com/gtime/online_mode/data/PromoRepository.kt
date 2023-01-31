package com.gtime.online_mode.data

import com.google.firebase.firestore.CollectionReference
import com.gtime.general.Constants
import com.gtime.general.scopes.AppScope
import com.gtime.online_mode.data.model.PromoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@AppScope
class PromoRepository @Inject constructor(
    @Named(Constants.PROMO_STORAGE_COLLECTION) private val promoRef: CollectionReference,
) {

    private val mapAvailability = hashMapOf<String, Boolean>()
    suspend fun makeMapAvailability() {
        promoRef.get().await().toObjects(PromoModel::class.java).forEach {
            mapAvailability[it.offerId] = it.promocodes.size > 0
        }
    }

    fun isAvailable(documentID: String): Boolean = mapAvailability[documentID] ?: false

    suspend fun getPromo(documentID: String): String = withContext(Dispatchers.IO) {
        val model =
            promoRef.document(documentID).get().await().toObject(PromoModel::class.java)
                ?: return@withContext ""
        val promo = model.promocodes.first()
        val arrayList = model.promocodes.filter { it != model.promocodes.first() }
        promoRef.document(documentID).update(Constants.PROMOS_FIELD, arrayList)
        return@withContext promo
    }

}