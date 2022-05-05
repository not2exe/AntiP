package com.example.antip.viewmodels

import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
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
import com.example.antip.ui.MainFragmentDirections
import java.util.*
import kotlin.collections.ArrayList


class MainFragmentViewModel(application: Application):AndroidViewModel(application) {

    private val getUsageTime=GetUsageTime()
    @RequiresApi(Build.VERSION_CODES.Q)
    val dddd=gang()



    @RequiresApi(Build.VERSION_CODES.Q)
    val allApps:ArrayList<App> =getUsageTime.getAppsInfo(getApplication())



    val usefulApps= MutableLiveData<ArrayList<App>> (getUsefulApps())


    private fun getUsefulApps():ArrayList<App> {
        return allApps



    }

    private fun getUselessApps():ArrayList<App>{
        return allApps


    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun gang(){
        val cal: Calendar = Calendar.getInstance()
        val allApps:ArrayList<App> =getUsageTime.getAppsInfo(getApplication())
        cal.add(Calendar.HOUR_OF_DAY,-7)
        val cal2:Calendar= Calendar.getInstance()
        val pppp =getUsageTime.getAppsInfoV2(getApplication(),cal.timeInMillis,cal2.timeInMillis)

        for (i in 0 until allApps.size){
            Log.d("CHECK",allApps[i].packageName)
            Log.d("CHECK",(pppp[allApps[i].packageName]?.timeInForeground?.div(1000)).toString())

        }

    }

    private fun getIconApp(context: Context, packageName:String): Drawable {
        try {
            val icon: Drawable = context.packageManager.getApplicationIcon(packageName)
            return icon
        }
        catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            Log.d("NameNotFoundException","yes")

        }
        return ContextCompat.getDrawable(context, R.drawable.app1)!!


    }


}

enum class State {
    USEFUL,USELESS
}