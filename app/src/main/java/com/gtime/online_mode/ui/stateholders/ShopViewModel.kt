package com.gtime.online_mode.ui.stateholders

import androidx.lifecycle.*
import com.gtime.online_mode.data.AccountRepository
import com.gtime.online_mode.data.CoinsRepository
import com.gtime.online_mode.data.PromoRepository
import com.gtime.online_mode.data.ShopRepository
import com.gtime.online_mode.data.model.OfferModel
import com.gtime.online_mode.ui.OfferUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class ShopViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val shopRepository: ShopRepository,
    private val promoRepository: PromoRepository,
    private val coinsRepository: CoinsRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): ShopViewModel
    }

    val offersUI =
        Transformations.switchMap(shopRepository.offers) { toOffersUi(it) }
    val stateCoinRep =
        Transformations.switchMap(
            coinsRepository.state
        ) { MutableLiveData(it) }
    val statePromoRep =
        Transformations.switchMap(promoRepository.state) { MutableLiveData(it) }

    init {
        viewModelScope.launch {
            promoRepository.makeMapAvailability()
            shopRepository.get()
        }
    }

    fun tryToBuy(offerID: String, cost: Int) {
        viewModelScope.launch {
            coinsRepository.subtractCoins(
                cost,
                offerID
            )
        }
    }

    fun getPromoCodes(offerID: String, cost: Int) {
        viewModelScope.launch {
            promoRepository.getPromo(
                offerID, cost, accountRepository.getEmail(),
                accountRepository.isAuthorized()
            )
            shopRepository.refresh()
        }
    }

    private fun toOffersUi(offerModels: List<OfferModel>): MutableLiveData<List<OfferUiModel>> =
        MutableLiveData(offerModels.map { toOfferUI(it) })


    private fun toOfferUI(offer: OfferModel): OfferUiModel =
        OfferUiModel(
            documentId = offer.offerID,
            description = offer.description,
            fullDescription = offer.fullDescription,
            urlOfferImage = offer.urlOfferImage,
            cost = offer.cost,
            isAvailable = promoRepository.isAvailable(offer.offerID)
        )

    fun moneyBack(cost: Int) =
        viewModelScope.launch {
            coinsRepository.addCoins(cost)
        }

    fun clearPromoState() {
        promoRepository.clearState()
    }

    fun clearCoinState() {
        coinsRepository.clearState()
    }

    fun refreshOffers() = viewModelScope.launch {
        shopRepository.get()
    }

}