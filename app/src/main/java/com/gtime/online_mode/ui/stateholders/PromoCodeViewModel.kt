package com.gtime.online_mode.ui.stateholders

import androidx.lifecycle.*
import com.gtime.online_mode.data.UsersPromoRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class PromoCodeViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val usersPromoRepository: UsersPromoRepository
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): PromoCodeViewModel
    }

    init {
        viewModelScope.launch {
            usersPromoRepository.refreshUsersPromo()
        }
    }

    fun refresh() = viewModelScope.launch {
        usersPromoRepository.refreshUsersPromo()
    }

    fun clearState() {
        usersPromoRepository.clearState()
    }

    val usersPromo =
        Transformations.switchMap(usersPromoRepository.usersPromoCodes) { MutableLiveData(it) }
    val state = Transformations.switchMap(usersPromoRepository.state) { MutableLiveData(it) }
}