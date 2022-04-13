package com.example.antip.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.antip.App
import com.example.antip.db.GetUsageTime
import com.google.android.material.internal.ContextUtils.getActivity


class MainFragmentViewModel: ViewModel() {
    val currentAppsLD: MutableLiveData<List<App>> = MutableLiveData<List<App>>()
    val app = App(getActivity(this).getDrawable(R.drawable.app1)!!,"1",123)
    adapter.addApp(app)
    @RequiresApi(Build.VERSION_CODES.Q)
    fun onScreenStarted() {
    }








}