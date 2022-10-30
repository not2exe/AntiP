package com.gtime.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtime.model.Cache
import com.gtime.model.UsageTime
import com.gtime.model.dataclasses.AppEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class AppManagerFragmentViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val cache: Cache,
    private val usageTime: UsageTime
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): AppManagerFragmentViewModel
    }

    val usefulApps = MutableLiveData<List<AppEntity>>(arrayListOf())
    val harmfulApps = MutableLiveData<List<AppEntity>>(arrayListOf())
    val otherApps = MutableLiveData<List<AppEntity>>(arrayListOf())


    fun refreshApps() {
        val mapOfUseful = cache.getAllUseful()
        val mapOfHarmful = cache.getAllHarmful()
        val usefulList = mutableListOf<AppEntity>()
        val harmfulList = mutableListOf<AppEntity>()
        val othersList = mutableListOf<AppEntity>()
        viewModelScope.launch {
            usageTime.getAllApps().forEach {
                when (it.name) {
                    in mapOfHarmful -> harmfulList.add(it)
                    in mapOfUseful -> usefulList.add(it)
                    else -> othersList.add(it)
                }
            }
            usefulApps.value = usefulList
            harmfulApps.value = harmfulList
            otherApps.value = othersList
        }

    }
}

