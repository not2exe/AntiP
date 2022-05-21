package com.example.antip.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.antip.model.db.DailyStatsDao
import com.example.antip.model.db.DailyStatsDatabase
import com.example.antip.model.db.DailyStatsEntry
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import javax.inject.Inject

class StatsFragmentViewModel(application: Application) : AndroidViewModel(application) {
    val stats = MutableLiveData<List<DailyStatsEntry?>>(listOf(null))



    fun initStats() {
        val database =
            Room.databaseBuilder(getApplication(), DailyStatsDatabase::class.java, "stats_table")
                .build()
        val dailyStatsDao: DailyStatsDao = database.dailyStatsDao()
        viewModelScope.launch {
            stats.value=dailyStatsDao.getAllStats()
            Log.d("stats",stats.value.toString())

        }

    }

}