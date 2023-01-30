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
    @Named(Constants.PROMO_STORAGE_COLLECTION) private val promoRef: CollectionReference
) {
    suspend fun isAvailable(documentID: String): Boolean = withContext(Dispatchers.IO) {
        val model = promoRef.document(documentID).get().await().toObject(PromoModel::class.java)
            ?: return@withContext false
        return@withContext model.array.size > 0
    }

    suspend fun getPromo(documentID: String): String = withContext(Dispatchers.IO) {
        val model =
            promoRef.document(documentID).get().await().toObject(PromoModel::class.java)
                ?: return@withContext ""
        val promo = model.array.first()
        val arrayList = model.array.filter { it != model.array.first() }
        promoRef.document(documentID).update(Constants.PROMOS_FIELD, arrayList)
        return@withContext promo
    }

}