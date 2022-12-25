package com.gtime.ui.stateholders

import androidx.lifecycle.*
import com.example.antip.R
import com.gtime.model.Cache
import com.gtime.model.UsageTimeRepository
import com.gtime.model.dataclasses.AppEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class AppManagerFragmentViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val usageTimeRepository: UsageTimeRepository,
    private val cache: Cache
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): AppManagerFragmentViewModel
    }

    val usefulApps =
        Transformations.switchMap(usageTimeRepository.usefulApps) { getLiveDataList(it) }
    val harmfulApps =
        Transformations.switchMap(usageTimeRepository.harmfulApps) { getLiveDataList(it) }
    val neutralApps =
        Transformations.switchMap(usageTimeRepository.neutralApps) { getLiveDataList(it) }


    private fun getLiveDataList(list: List<AppEntity>): LiveData<List<AppEntity>> =
        MutableLiveData(list.sortedByDescending { it.scores })

    fun handleChanges(
        sourceId: Int,
        targetId: Int,
        sourceElem: AppEntity
    ) = viewModelScope.launch {
        if (targetId == sourceId) return@launch

        when (sourceId) {
            R.id.rvUseful -> {
                usageTimeRepository.removeFromUseful(sourceElem)
                handleAdd(
                    targetId,
                    sourceElem,
                )
            }
            R.id.rvHarmful -> {
                usageTimeRepository.removeFromHarmful(sourceElem)
                handleAdd(
                    targetId,
                    sourceElem,
                )
            }
            R.id.rvOthers -> {
                usageTimeRepository.removeFromOthers(sourceElem)
                handleAdd(
                    targetId,
                    sourceElem,
                )
            }
        }
    }


    private suspend fun handleAdd(
        targetId: Int,
        sourceElem: AppEntity,
    ) {
        when (targetId) {
            R.id.rvOthers -> {
                usageTimeRepository.putIntoOthers(sourceElem)
            }
            R.id.rvUseful -> {
                usageTimeRepository.putIntoUseful(sourceElem)
            }
            R.id.rvHarmful -> {
                usageTimeRepository.putIntoHarmful(sourceElem)
            }
        }
    }
}

