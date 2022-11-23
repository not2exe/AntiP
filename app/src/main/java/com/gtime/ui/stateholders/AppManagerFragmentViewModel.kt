package com.gtime.ui.stateholders

import androidx.lifecycle.*
import com.example.antip.R
import com.gtime.Constants
import com.gtime.model.Cache
import com.gtime.model.UsageTime
import com.gtime.model.dataclasses.AppEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class AppManagerFragmentViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val usageTime: UsageTime,
    private val cache: Cache
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): AppManagerFragmentViewModel
    }

    val usefulApps =
        Transformations.switchMap(usageTime.usefulApps) { getLiveDataList(it) }
    val harmfulApps = Transformations.switchMap(usageTime.harmfulApps) { getLiveDataList(it) }
    val neutralApps = Transformations.switchMap(usageTime.neutralApps) { getLiveDataList(it) }


    private fun getLiveDataList(list: List<AppEntity>): LiveData<List<AppEntity>> =
        MutableLiveData(list.sortedByDescending { it.scores })

    fun handleChanges(
        sourceId: Int,
        targetId: Int,
        sourceElem: AppEntity
    ) {
        if (targetId == sourceId) return

        when (sourceId) {
            R.id.rvUseful -> {
                usageTime.removeFromUseful(sourceElem)
                handleAdd(
                    targetId,
                    sourceElem,
                )
            }
            R.id.rvHarmful -> {
                usageTime.removeFromHarmful(sourceElem)
                handleAdd(
                    targetId,
                    sourceElem,
                )
            }
            R.id.rvOthers -> {
                usageTime.removeFromOthers(sourceElem)
                handleAdd(
                    targetId,
                    sourceElem,
                )
            }
        }
    }


    private fun handleAdd(
        targetId: Int,
        sourceElem: AppEntity,
    ) {
        when (targetId) {
            R.id.rvOthers -> {
                usageTime.putIntoOthers(sourceElem)
            }
            R.id.rvUseful -> {
                usageTime.putIntoUseful(sourceElem)
            }
            R.id.rvHarmful -> {
                usageTime.putIntoHarmful(sourceElem)
            }
        }
    }
}

