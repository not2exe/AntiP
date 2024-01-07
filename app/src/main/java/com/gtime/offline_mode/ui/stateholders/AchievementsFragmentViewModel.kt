package com.gtime.offline_mode.ui.stateholders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.gtime.general.Cache
import com.gtime.general.Constants
import com.gtime.general.model.dataclasses.Achievement
import com.notexe.gtime.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class AchievementsFragmentViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val cache: Cache
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): AchievementsFragmentViewModel
    }


    val achievements = MutableLiveData<ArrayList<Achievement>>(arrayListOf())


    fun initAchievements() {
        val imageHardcore: Int = if (cache.getFromBoolean(Constants.KEY_ACHIEVEMENT_HARDCORE))
            R.drawable.done
        else
            R.drawable.lock
    }


}