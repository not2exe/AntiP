package com.gtime.online_mode.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.gtime.general.Constants
import com.gtime.general.scopes.AppScope
import com.gtime.online_mode.data.model.OfferModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@AppScope
class ShopRepository @Inject constructor(
    @Named(Constants.SHOP_COLLECTION) private val refShop: CollectionReference,
) {
    val offers = MutableLiveData<List<OfferModel>>()

    suspend fun get() = withContext(Dispatchers.IO) {
        val mutableList = mutableListOf<OfferModel>()
        refShop.get().await().documents.forEach {
            val obj = it.toObject(OfferModel::class.java) ?: OfferModel()
            val offerWithID =
                OfferModel(obj.cost, obj.description, obj.fullDescription, it.id, obj.urlOfferImage)
            mutableList.add(offerWithID)
        }
        offers.postValue(mutableList)
    }

    fun refresh() {
        offers.value = offers.value
    }


}