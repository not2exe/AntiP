package com.gtime.online_mode.ui.stateholders

import androidx.lifecycle.*
import com.gtime.online_mode.data.UsersPromoRepository
import com.gtime.online_mode.data.model.UserPromoModel
import com.gtime.online_mode.state_classes.StateOfRequests
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

    val usersPromo: LiveData<List<UserPromoModel>> = usersPromoRepository.usersPromoCodes
    val state: LiveData<StateOfRequests> = usersPromoRepository.state
}