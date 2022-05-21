package com.example.antip.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.antip.model.App
import com.example.antip.model.UsageTime
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>()
    private val usageTime = UsageTime()

    val usefulApps = MutableLiveData<ArrayList<App?>>(arrayListOf(null))
    val harmfulApps = MutableLiveData<ArrayList<App?>>(arrayListOf(null))
    val scoresAll= MutableLiveData(0)
    fun provideTimeInModel() {
        usageTime.refreshTime(context)
    }




    fun initApps() {
        val mapOfUseful = context.getSharedPreferences("nameOfUseful", Context.MODE_PRIVATE).all
        val mapOfHarmful = context.getSharedPreferences("nameOfHarmful", Context.MODE_PRIVATE).all
        val arrayOfAll= usageTime.getArrayListOfAllApps()
        usefulApps.value=arrayListOf(null)
        harmfulApps.value=arrayListOf(null)
        scoresAll.value=0
        for(i in arrayOfAll.indices){
            when(arrayOfAll[i].name ){
                in mapOfHarmful.values -> {
                    harmfulApps.value?.add(arrayOfAll[i])
                    scoresAll.value = scoresAll.value?.minus(arrayOfAll[i].scores)
                }
                in mapOfUseful.values -> {
                    usefulApps.value?.add(arrayOfAll[i])
                    scoresAll.value = scoresAll.value?.plus(arrayOfAll[i].scores)
                }
            }
        }
        usefulApps.value?.sortByDescending { it?.scores }
        harmfulApps.value?.sortByDescending { it?.scores }

    }
    fun refresh(){
        usageTime.refreshTime(context)
        initApps()
    }
    private fun formatDate(): String {
        val dateFormat: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(Date())

    }







}

enum class State {
    USEFUL, HARMFUL
}