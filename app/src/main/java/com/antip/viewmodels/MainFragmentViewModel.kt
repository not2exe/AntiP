package com.antip.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.antip.model.Cash
import com.example.antip.model.UsageTime
import com.example.antip.model.dataclasses.AppEntity
import java.util.*


class MainFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>()
    private val usageTime = UsageTime()
    private val cash: Cash = Cash(context)

    val usefulApps = MutableLiveData<ArrayList<AppEntity?>>(arrayListOf(null))
    val harmfulApps = MutableLiveData<ArrayList<AppEntity?>>(arrayListOf(null))
    val scoresAll = MutableLiveData(0)
    fun provideTimeInModel() {
        usageTime.refreshTime(context)
    }


    fun initApps() {
        usefulApps.value = arrayListOf(null)
        harmfulApps.value = arrayListOf(null)

        usefulApps.value=usageTime.getArrayListOfUseful()
        harmfulApps.value=usageTime.getArrayListOfHarmful()
        scoresAll.value=usageTime.getScores()

        usefulApps.value?.sortByDescending { it?.scores }
        harmfulApps.value?.sortByDescending { it?.scores }

    }

    fun refresh() {
        usageTime.refreshTime(context)
        initApps()
    }

    fun getIsLostHardcore():Boolean{
        return cash.getFromBoolean("IsLostHardcore")
    }


}

enum class State {
    USEFUL, HARMFUL
}