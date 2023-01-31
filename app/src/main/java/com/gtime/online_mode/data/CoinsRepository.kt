package com.gtime.online_mode.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.gtime.general.Constants
import com.gtime.general.scopes.AppScope
import com.gtime.online_mode.data.model.CoinsModel
import com.gtime.online_mode.state_sealed_class.StateOfBuy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@AppScope
class CoinsRepository @Inject constructor(
    @Named(Constants.COINS_STORAGE_COLLECTION) private val coinsRef: CollectionReference,
    private val auth: FirebaseAuth
) {
    val coins = MutableLiveData<Int>()
    val state = MutableLiveData<StateOfBuy>(StateOfBuy.WaitingForUserAction)

    init {
        initCoins()
    }
    private fun initCoins() {
        val email = auth.currentUser?.email ?: return
        coinsRef.document(email).addSnapshotListener { value, error ->
            val obj = value?.toObject(CoinsModel::class.java) ?: CoinsModel(0)
            coins.postValue(obj.coins)
        }
    }

    suspend fun addCoins(coins: Int) {
        val email = auth.currentUser?.email ?: return
        val coinsOld =
            coinsRef.document(email).get().await().toObject(CoinsModel::class.java)?.coins ?: 0
        coinsRef.document(email).set(
            hashMapOf(Constants.COINS to coinsOld + coins),
            SetOptions.merge()
        )
    }

    suspend fun subtractCoins(cost: Int, offerId: String) =
        withContext(Dispatchers.IO) {
            val email = auth.currentUser?.email
            if (email == null) {
                state.postValue(StateOfBuy.AuthError)
                return@withContext
            }
            val coinsOld =
                coinsRef.document(email).get().await().toObject(CoinsModel::class.java)?.coins ?: 0
            if (coinsOld < cost) {
                state.postValue(StateOfBuy.LackOfCoinsError)
                return@withContext
            }
            coinsRef.document(email).set(
                hashMapOf(Constants.COINS to coinsOld - cost), SetOptions.merge()
            ).addOnCompleteListener {
                state.postValue(StateOfBuy.SuccessWithCoins(offerId, cost))
            }
        }

    fun clearState() {
        state.postValue(StateOfBuy.WaitingForUserAction)
    }
}