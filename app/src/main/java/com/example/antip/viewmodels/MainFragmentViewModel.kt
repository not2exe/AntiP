package com.example.antip.viewmodels

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.antip.App
import com.example.antip.R
import com.example.antip.db.UsageTime
import java.util.*
import kotlin.collections.ArrayList


class MainFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val nameOfUsefulApps = arrayOf(
        "CoolReader", "InShot", "KineMaster", "PicsArt", "Strava",
        "Sleep Cycle", "Daylio", "Calm", "Seven", "Brainly",
        "English", "TED", "GitHub", "Canva", "WolframAlpha",
        "Кинопоиск", "Webinar", "Kapersky", "Lumosicty", "AccuBatery",
        "GetCourse", "MyBook"
    )
    private val nameOfHarmfulApps = arrayOf(
        "TikTok", "Instagram", "Facebook", "Zoom", "Netflix",
        "YouTube", "Twitter", "Pinterest", "Snapchat", "WhatsApp",
        "Reddit", "Twitch", "VK", "Spotify", "Hearthstone", "Discord"
    )

    private val UsageTime = UsageTime()
    private val allApps: ArrayList<App> = UsageTime.getAppsInfo(getApplication())


    val usefulApps = MutableLiveData<ArrayList<App?>>(
        arrayListOf(
            null
        )
    )
    val harmfulApps = MutableLiveData<ArrayList<App?>>(
        arrayListOf(
            null
        )
    )
    val otherApps = MutableLiveData<ArrayList<App?>>(
        arrayListOf(
            null
        )
    )



    fun initApps(){
        for (i in 0 until allApps.size) {
            when (allApps[i].name) {
                in nameOfUsefulApps -> {
                    usefulApps.value?.add(allApps[i])

                }
                in nameOfHarmfulApps -> {
                    harmfulApps.value?.add(allApps[i])
                }
                else -> {
                    otherApps.value?.add(allApps[i])
                }
            }
        }
    }






    fun gang() {
        val cal: Calendar = Calendar.getInstance()
        val allApps: ArrayList<App> = UsageTime.getAppsInfo(getApplication())
        cal.add(Calendar.DAY_OF_MONTH, -1)
        val cal2: Calendar = Calendar.getInstance()
        val pppp = UsageTime.getAppsInfoV2(getApplication(), cal.timeInMillis, cal2.timeInMillis)
        Log.d("MVVM",pppp.toString())
    }

    private fun getIconApp(context: Context, packageName: String): Drawable {
        try {
            val icon: Drawable = context.packageManager.getApplicationIcon(packageName)
            return icon
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            Log.d("NameNotFoundException", "yes")

        }
        return ContextCompat.getDrawable(context, R.drawable.undefined)!!


    }
    private fun getName(context: Context,packageName: String): String{
        val Apps:List<ApplicationInfo> =context.packageManager.getInstalledApplications(0)
        for (i in Apps.indices){
            if (Apps[i].packageName==packageName)
                return Apps[i].loadLabel(context.packageManager).toString()

        }
        return "gTime"

    }


}

enum class State {
    USEFUL, USELESS
}