package com.example.antip.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.antip.model.Cash
import com.example.antip.model.UsageTime
import com.example.antip.model.dataclasses.App
import java.text.SimpleDateFormat
import java.util.*


class MainFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>()
    private val usageTime = UsageTime()
    private val cash: Cash = Cash(context)

    val usefulApps = MutableLiveData<ArrayList<App?>>(arrayListOf(null))
    val harmfulApps = MutableLiveData<ArrayList<App?>>(arrayListOf(null))
    val scoresAll = MutableLiveData(0)
    fun provideTimeInModel() {
        usageTime.refreshTime(context)
    }


    fun initApps() {
        val mapOfUseful = cash.getAllUseful()
        val mapOfHarmful = cash.getAllHarmful()
        val arrayOfAll = usageTime.getArrayListOfAllApps()
        usefulApps.value = arrayListOf(null)
        harmfulApps.value = arrayListOf(null)
        scoresAll.value = 0
        for (i in arrayOfAll.indices) {
            when (arrayOfAll[i].name) {
                in mapOfHarmful -> {
                    harmfulApps.value?.add(arrayOfAll[i])
                    scoresAll.value = scoresAll.value?.minus(arrayOfAll[i].scores)
                }
                in mapOfUseful -> {
                    usefulApps.value?.add(arrayOfAll[i])
                    scoresAll.value = scoresAll.value?.plus(arrayOfAll[i].scores)
                }
            }
        }
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