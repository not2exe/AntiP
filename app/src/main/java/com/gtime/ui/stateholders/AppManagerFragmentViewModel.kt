package com.gtime.ui.stateholders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antip.R
import com.gtime.Constants
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

    fun handleChanges(sourceId: Int, targetId: Int, positionSource: Int, positionTarget: Int, sourceElem: AppEntity) {
        if (targetId == sourceId) return
        val usefulMutableList =
            usefulApps.value?.toMutableList() ?: emptyList<AppEntity>().toMutableList()
        val harmfulMutableList =
            harmfulApps.value?.toMutableList() ?: emptyList<AppEntity>().toMutableList()
        val othersMutableList =
            otherApps.value?.toMutableList() ?: emptyList<AppEntity>().toMutableList()

        if(sourceElem.name== Constants.EMPTY)return
        val name = sourceElem.name ?: ""
        when (sourceId) {
            R.id.rvUseful -> {
                cache.removeFromUseful(name)
                usefulMutableList.removeAt(positionSource)
                handleAdd(
                    targetId,
                    harmfulMutableList,
                    othersMutableList,
                    usefulMutableList,
                    sourceElem,
                    positionTarget,
                    name
                )
            }
            R.id.rvHarmful -> {
                cache.removeFromHarmful(name)
                harmfulMutableList.removeAt(positionSource)
                handleAdd(
                    targetId,
                    harmfulMutableList,
                    othersMutableList,
                    usefulMutableList,
                    sourceElem,
                    positionTarget,
                    name
                )
            }
            R.id.rvOthers -> {
                othersMutableList.removeAt(positionSource)
                handleAdd(
                    targetId,
                    harmfulMutableList,
                    othersMutableList,
                    usefulMutableList,
                    sourceElem,
                    positionTarget,
                    name
                )
            }
        }
        harmfulApps.value = harmfulMutableList
        usefulApps.value = usefulMutableList
        otherApps.value = othersMutableList
    }

    private fun handleAdd(
        targetId: Int,
        harmfulMutableList: MutableList<AppEntity>,
        othersMutableList: MutableList<AppEntity>,
        usefulMutableList: MutableList<AppEntity>,
        sourceElem: AppEntity,
        positionTarget: Int,
        name: String
    ) {
        when (targetId) {
            R.id.rvOthers -> {
                othersMutableList.add(positionTarget, sourceElem)
            }
            R.id.rvUseful -> {
                usefulMutableList.add(positionTarget, sourceElem)
                cache.inputIntoUseful(name)
            }
            R.id.rvHarmful -> {
                harmfulMutableList.add(positionTarget, sourceElem)
                cache.inputIntoHarmful(name)
            }
        }
    }
}

