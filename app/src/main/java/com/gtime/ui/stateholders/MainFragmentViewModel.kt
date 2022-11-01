package com.gtime.ui.stateholders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.gtime.Constants
import com.gtime.State
import com.gtime.model.Cache
import com.gtime.model.UsageTime
import com.gtime.model.dataclasses.AppEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class MainFragmentViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val usageTime: UsageTime,
    private val cache: Cache
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): MainFragmentViewModel
    }

    val usefulApps = MutableLiveData<ArrayList<AppEntity>>(arrayListOf())
    val harmfulApps = MutableLiveData<ArrayList<AppEntity>>(arrayListOf())
    val scoresAll = MutableLiveData(0)
    val stateOfKindOfApps = MutableLiveData<State>(State.USEFUL)


    fun refresh() {
        usageTime.refreshTime()
        initApps()
    }

    fun getIsLostHardcore(): Boolean {
        return cache.getFromBoolean(Constants.KEY_IS_LOST_HARDCORE)
    }

    fun setState(state: State) {
        stateOfKindOfApps.value = state
    }


    private fun initApps() {
        usefulApps.value = usageTime.arrayOfUseful
        harmfulApps.value = usageTime.arrayOfHarmful
        scoresAll.value = usageTime.scoresAll
        usefulApps.value?.sortByDescending { it.scores }
        harmfulApps.value?.sortByDescending { it.scores }
    }

}

