package com.gtime.ui.stateholders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.antip.R
import com.gtime.Constants
import com.gtime.model.Cache
import com.gtime.model.dataclasses.Achievement
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
        achievements.value?.add(Achievement(R.drawable.icon, "Download App", R.drawable.done))
        achievements.value?.add(Achievement(R.drawable.heart, "Survive one day", imageHardcore))
    }


}