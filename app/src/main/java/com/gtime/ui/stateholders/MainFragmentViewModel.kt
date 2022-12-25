package com.gtime.ui.stateholders

import android.util.Log
import androidx.lifecycle.*
import com.gtime.KindOfApps
import com.gtime.model.Cache
import com.gtime.model.UsageTimeRepository
import com.gtime.model.dataclasses.AppEntity
import com.gtime.model.db.DailyStatsDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class MainFragmentViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val usageTimeRepository: UsageTimeRepository,
    private val cache: Cache,
    private val dao:DailyStatsDao,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): MainFragmentViewModel
    }

    val usefulApps: LiveData<List<AppEntity>> =
        Transformations.switchMap(usageTimeRepository.usefulApps) { list ->
            MutableLiveData(list.filter { it.scores != 0 }.sortedByDescending { it.scores })
        }
    val harmfulApps: LiveData<List<AppEntity>> =
        Transformations.switchMap(usageTimeRepository.harmfulApps) { list ->
            MutableLiveData(list.filter { it.scores != 0 }.sortedByDescending { it.scores })
        }
    val scoresAll: LiveData<Int> =
        Transformations.switchMap(usageTimeRepository.uiGeneralScores) { MutableLiveData(it) }
    val stateOfKindOfApps = MutableLiveData<KindOfApps>(KindOfApps.USEFUL)
    val lives = MutableLiveData<Int>()


    init {
        refresh()
        refreshLife()
    }

    fun refresh() =
        viewModelScope.launch {
            usageTimeRepository.refreshScores()
        }


    private fun refreshLife() {
        lives.value = cache.getLives()
    }

    fun setState(state: KindOfApps) {
        stateOfKindOfApps.value = state
    }

}

