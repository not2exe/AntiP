package com.example.antip.viewmodels

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.antip.App
import com.example.antip.R
import com.example.antip.db.GetUsageTime



class MainFragmentViewModel(application: Application):AndroidViewModel(application) {


    @RequiresApi(Build.VERSION_CODES.Q)
    val currentAppsLD= MutableLiveData<ArrayList<App>> (onScreenStarted())

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun onScreenStarted():ArrayList<App> {
        val getUsageTime=GetUsageTime()
        return getUsageTime.getAppsInfo(getApplication())
    }
    fun clear(){
        currentAppsLD.value?.clear()

    }



}