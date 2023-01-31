package com.gtime.online_mode.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.gtime.general.Constants
import com.gtime.general.scopes.AppScope
import com.gtime.online_mode.data.model.PromoModel
import com.gtime.online_mode.state_sealed_class.StateOfBuy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@AppScope
class PromoRepository @Inject constructor(
    @Named(Constants.PROMO_STORAGE_COLLECTION) private val promoRef: CollectionReference,
    @Named(Constants.USERS_PROMO_STORAGE_COLLECTION) private val usersPromoRef: CollectionReference,
    private val auth: FirebaseAuth,
) {
    val state = MutableLiveData<StateOfBuy>(StateOfBuy.WaitingForUserAction)
    private val mapAvailability = hashMapOf<String, Boolean>()
    suspend fun makeMapAvailability() {
        promoRef.get().await().documents.forEach {
            val obj = it.toObject(PromoModel::class.java)
            mapAvailability[it.id] = obj?.promocodes?.isNotEmpty() ?: false
        }
    }

    fun isAvailable(offerID: String): Boolean =
        mapAvailability[offerID] ?: false

    suspend fun getPromo(offerID: String, cost: Int) = withContext(Dispatchers.IO) {
        val email = auth.currentUser?.email
        val model = promoRef.document(offerID).get().await().toObject(PromoModel::class.java)
        if (model == null || email == null) {
            state.postValue(StateOfBuy.PromoError(cost = cost))
            return@withContext
        }

        val promo = model.promocodes.first()
        val newArrayPromoCodes = model.promocodes.filter { it != model.promocodes.first() }
        mapAvailability[offerID] = newArrayPromoCodes.isNotEmpty()
        promoRef.document(offerID).set(
            hashMapOf(Constants.PROMOS_FIELD to newArrayPromoCodes),
            SetOptions.merge()
        ).addOnCompleteListener {
            if (!it.isSuccessful) {
                state.postValue(StateOfBuy.PromoError(cost = cost))
            }
        }

        val promoWithDescRef =
            usersPromoRef.document(email).collection(Constants.PROMO_WITH_DESC_COLLECTION)
        val arrayOld =
            promoWithDescRef.document(offerID).get().await()
                .toObject(PromoModel::class.java)?.promocodes ?: arrayListOf()
        arrayOld.add(promo)
        promoWithDescRef.document(offerID).set(
            hashMapOf(Constants.PROMOS_FIELD to arrayOld),
            SetOptions.merge()
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                state.postValue(StateOfBuy.FullSuccess)
            } else {
                state.postValue(StateOfBuy.PromoError(cost = cost))
            }
        }
    }

    fun clearState() {
        state.postValue(StateOfBuy.WaitingForUserAction)
    }
}