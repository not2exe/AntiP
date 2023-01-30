package com.gtime.online_mode.ui.stateholders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.gtime.general.Constants
import com.gtime.online_mode.data.TopScoresSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class TopScoresViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    topScoresSource: TopScoresSource
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): TopScoresViewModel
    }

    val flow =
        Pager(PagingConfig(pageSize = Constants.PAGE_SIZE)) { topScoresSource }.flow.cachedIn(
            viewModelScope
        )
}