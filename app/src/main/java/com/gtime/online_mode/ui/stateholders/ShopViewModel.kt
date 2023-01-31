package com.gtime.online_mode.ui.stateholders

import androidx.lifecycle.*
import com.gtime.online_mode.data.PromoRepository
import com.gtime.online_mode.data.ShopRepository
import com.gtime.online_mode.data.model.OfferModel
import com.gtime.online_mode.domain.OfferUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class ShopViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val shopRepository: ShopRepository,
    private val promoRepository: PromoRepository
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): ShopViewModel
    }

    val offersUI =
        Transformations.switchMap(shopRepository.offers) { toOffersUi(it) }

    init {
        viewModelScope.launch {
            promoRepository.makeMapAvailability()
            shopRepository.get()
        }
    }

    private fun toOffersUi(offerModels: List<OfferModel>): MutableLiveData<List<OfferUiModel>> =
        MutableLiveData(offerModels.map { toOfferUI(it) })


    private fun toOfferUI(offer: OfferModel): OfferUiModel =
        OfferUiModel(
            documentId = offer.offerId,
            description = offer.description,
            fullDescription = offer.fullDescription,
            urlOfferImage = offer.urlOfferImage,
            cost = offer.cost,
            isAvailable = promoRepository.isAvailable(offer.offerId)
        )

}