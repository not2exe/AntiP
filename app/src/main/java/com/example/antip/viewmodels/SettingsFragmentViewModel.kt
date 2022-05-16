package com.example.antip.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.antip.R
import kotlin.collections.ArrayList

class SettingsFragmentViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context: Context = application

    val usefulApps = MutableLiveData<ArrayList<AppS?>>(arrayListOf(null))
    val harmfulApps = MutableLiveData<ArrayList<AppS?>>(arrayListOf(null))
    val otherApps = MutableLiveData<ArrayList<AppS?>>(arrayListOf(null))



    fun initApps() {
        val list: List<ApplicationInfo> =
            context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        val mapOfUseful = context.getSharedPreferences("nameOfUseful", 0).all.values
        val mapOfHarmful = context.getSharedPreferences("nameOfHarmful", 0).all.values
        var appS: AppS
        for (i in list.indices) {
            if (context.packageManager.getLaunchIntentForPackage(list[i].packageName) != null
                && (list[i].flags and ApplicationInfo.FLAG_SYSTEM) == 0
                && (list[i].flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0
            ) {

                appS = AppS(getIconApp(list[i].packageName), getName(list[i].packageName))
                when (appS.name) {
                    in mapOfHarmful -> harmfulApps.value?.add(appS)
                    in mapOfUseful -> usefulApps.value?.add(appS)
                    else -> otherApps.value?.add(appS)
                }
            }
        }


    }


    private fun getIconApp(packageName: String): Drawable {
        return try {
            context.packageManager.getApplicationIcon(packageName)
        } catch (e: PackageManager.NameNotFoundException) {
            ContextCompat.getDrawable(context, R.drawable.undefined)!!
        }


    }

    private fun getName(packageName: String): String {
        val apps: List<ApplicationInfo> = context.packageManager.getInstalledApplications(0)
        for (i in apps.indices) {
            if (apps[i].packageName == packageName)
                return apps[i].loadLabel(context.packageManager).toString()

        }
        return "gTime"
    }


}

data class AppS(val image: Drawable, val name: String)
