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

    private val getUsageTime=GetUsageTime()
    @RequiresApi(Build.VERSION_CODES.Q)
    val allApps:ArrayList<App> =getUsageTime.getAppsInfo(getApplication())


    val usefulApps= MutableLiveData<ArrayList<App>> (getUsefulApps())


    private fun getUsefulApps():ArrayList<App> {
        return allApps



    }

    private fun getUselessApps():ArrayList<App>{
        return allApps


    }


}
enum class State {
    USEFUL,USELESS
}