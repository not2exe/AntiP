package com.gtime.ui.stateholders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.gtime.Constants
import com.gtime.model.Cache
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class ChangeModeViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val cache: Cache
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): ChangeModeViewModel
    }

    fun getIsLostHardcore(): Boolean {
        return cache.getFromBoolean(Constants.KEY_IS_LOST_HARDCORE)
    }

    fun getStateNormal(): Boolean {
        return cache.getFromBoolean(Constants.KEY_NORMAL_MODE)

    }

    fun getStateHardcore(): Boolean {
        return cache.getFromBoolean(Constants.KEY_HARDCORE_MODE)
    }

    fun onNormalButtonClick(value: Boolean) =
        cache.inputIntoBoolean(Constants.KEY_NORMAL_MODE, value)


    fun onHardcoreButtonClick(value: Boolean) =
        cache.inputIntoBoolean(Constants.KEY_HARDCORE_MODE, value)


}