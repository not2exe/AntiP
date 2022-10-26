package com.antip.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.antip.R
import com.example.antip.model.Cash
import com.example.antip.model.dataclasses.Achievement

class AchievementsFragmentViewModel(application: Application) : AndroidViewModel(application) {


    val achievements = MutableLiveData<ArrayList<Achievement?>>(arrayListOf(null))
    private val context = getApplication<Application>()
    fun initAchievements() {
        achievements.value?.clear()
        val cash: Cash = Cash(context)
        val imageHardcore: Int = if (cash.getFromBoolean("AchievementHardcore"))
            R.drawable.done
        else
            R.drawable.lock

        achievements.value?.add(Achievement(R.drawable.icon, "Download App", R.drawable.done))
        achievements.value?.add(Achievement(R.drawable.heart, "Survive one day", imageHardcore))


    }


}