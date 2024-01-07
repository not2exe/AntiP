package com.gtime.offline_mode.ui.stateholders

import androidx.lifecycle.*
import com.example.antip.R
import com.gtime.general.KindOfApps
import com.gtime.general.model.UsageTimeRepository
import com.gtime.general.model.dataclasses.AppEntity
import com.gtime.general.model.db.AppDataBaseEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class AppManagerFragmentViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val usageTimeRepository: UsageTimeRepository,
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): AppManagerFragmentViewModel
    }

    val usefulApps = usageTimeRepository.usefulApps.map(::getLiveDataList)
    val harmfulApps = usageTimeRepository.toxicApps.map(::getLiveDataList)
    val neutralApps = usageTimeRepository.neutralApps.map(::getLiveDataList)

    private val hashMap: LiveData<Map<String, Int>> = neutralApps.map { list ->
        list.mapIndexed { index, element -> element.name to index }.toMap()
    }


    private fun getLiveDataList(list: List<AppEntity>): List<AppEntity> =
        list.sortedByDescending { it.percentsOsGeneral }

    fun setMultiplier(appDataBaseEnt: AppDataBaseEntity) {
        viewModelScope.launch {
            usageTimeRepository.setMultiplier(
                AppDataBaseEntity(
                    appDataBaseEnt.packageName ?: "",
                    appDataBaseEnt.isGame,
                    appDataBaseEnt.kindOfApp,
                    appDataBaseEnt.multiplier
                )
            )
        }
    }

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
        val kindOfApps = when (targetId) {
            R.id.rvOthers -> KindOfApps.NEUTRAL
            R.id.rvUseful -> KindOfApps.USEFUL
            R.id.rvHarmful -> KindOfApps.TOXIC
            else -> null
        }
        usageTimeRepository.put(sourceElem, kindOfApps ?: return)
    }

    fun getPos(name: String?): Int = hashMap.value?.get(name ?: "") ?: 0
}

