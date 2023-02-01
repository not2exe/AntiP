package com.gtime.online_mode.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.gtime.general.Constants
import com.gtime.general.scopes.AppScope
import com.gtime.online_mode.data.model.PromoModel
import com.gtime.online_mode.data.model.UserPromoModel
import com.gtime.online_mode.state_classes.StateOfRequests
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@AppScope
class UsersPromoRepository @Inject constructor(
    private val auth: FirebaseAuth,
    @Named(Constants.USERS_PROMO_STORAGE_COLLECTION) private val usersPromoStorage: CollectionReference,
    private val shopRepository: ShopRepository
) {
    val usersPromoCodes = MutableLiveData<List<UserPromoModel>>()
    val state = MutableLiveData<StateOfRequests>()
    suspend fun refreshUsersPromo() = withContext(Dispatchers.IO) {
        val email = auth.currentUser?.email
        if (email == null) {
            state.postValue(StateOfRequests.Error.AuthError)
            return@withContext
        }
        val list = mutableListOf<UserPromoModel>()
        usersPromoStorage.document(email).collection(Constants.PROMO_WITH_DESC_COLLECTION)
            .get().await().forEach { document ->
                val promoModel =
                    PromoModel(document.id, document.toObject(PromoModel::class.java).promocodes)
                val offerModel = shopRepository.getById(promoModel.offerID)
                list.add(UserPromoModel(id = email, promoModel, offerModel))
            }
        usersPromoCodes.postValue(list)
    }

    fun clearState() {
        state.postValue(StateOfRequests.WaitingForUserAction)
    }

}
