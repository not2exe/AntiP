package com.gtime.online_mode.ui.stateholders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.google.firebase.firestore.Query
import com.gtime.general.Constants
import com.gtime.general.model.UsageTimeRepository
import com.gtime.online_mode.data.TopScoresSource
import com.gtime.online_mode.ui.logic.TopScoresRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TopScoresViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    usageTimeRepository: UsageTimeRepository,
    private val query: Query,
    private val topScoresRepository: TopScoresRepository
) : ViewModel() {


    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): TopScoresViewModel
    }

    init {
        viewModelScope.launch {
            usageTimeRepository.refreshUsageApps()
            topScoresRepository.updateForCurrentUser(
                usageTimeRepository.uiGeneralScores.value ?: return@launch
            )
        }
    }

    val flow =
        Pager(PagingConfig(pageSize = Constants.PAGE_SIZE)) {
            TopScoresSource(
                query
            )
        }.flow.cachedIn(
            viewModelScope
        )
}