package com.example.antip.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.antip.model.App
import com.example.antip.model.CashWithNames
import com.example.antip.model.UsageTime
import com.example.antip.model.db.DailyStatsDao
import com.example.antip.model.db.DailyStatsDatabase
import com.example.antip.model.db.DailyStatsEntry
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainFragmentViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context: Context = application
    val cashWithNames: CashWithNames = CashWithNames(context)
    private val usageTime = UsageTime()

    val usefulApps = MutableLiveData<ArrayList<App?>>(arrayListOf(null))
    val harmfulApps = MutableLiveData<ArrayList<App?>>(arrayListOf(null))
    val scoresAll= MutableLiveData<Int>(0)
    init {
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
        val database =
            Room.databaseBuilder(context, DailyStatsDatabase::class.java, "stats_table")
                .build()
        val databaseDao:DailyStatsDao=database.dailyStatsDao()

        viewModelScope.launch {
            databaseDao.insertStats(DailyStatsEntry(0,formatDate(),scoresAll.value!!))
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