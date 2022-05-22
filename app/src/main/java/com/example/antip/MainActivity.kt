package com.example.antip


import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.antip.service.DailyStatsBroadcast


class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (getSharedPreferences("booleanShared", Context.MODE_PRIVATE).getBoolean(
                "FirstLaunch",
                true
            )
        ) {
            Log.d("there", "there")
            val dailyStatsBroadcast = DailyStatsBroadcast()
            dailyStatsBroadcast.setAlarm(this.applicationContext)
            initCash()

        }

    }


    private fun initCash() {
        val startNamesOfUsefulApps = arrayOf(
            "CoolReader", "InShot", "KineMaster", "PicsArt", "Strava",
            "Sleep Cycle", "Daylio", "Calm", "Seven", "Brainly",
            "English", "TED", "GitHub", "Canva", "WolframAlpha",
            "Кинопоиск", "Webinar", "Kaspersky", "Lumosicty", "AccuBattery",
            "GetCourse", "MyBook", "Zoom", "Figma"
        )
        val startNamesOfHarmfulApps = arrayOf(
            "TikTok", "Instagram", "Facebook", "Netflix",
            "YouTube", "Twitter", "Pinterest", "Snapchat", "WhatsApp",
            "Reddit", "Twitch", "VK", "Telegram", "Hearthstone", "Discord"
        )
        val namesOfUseful = getSharedPreferences("nameOfUseful", Context.MODE_PRIVATE)
        val namesOfHarmful = getSharedPreferences("nameOfHarmful", Context.MODE_PRIVATE)
        val booleanShared = getSharedPreferences("booleanShared", Context.MODE_PRIVATE)
        if (!booleanShared.contains("FirstLaunch")) {
            booleanShared.edit().putBoolean("FirstLaunch", false).apply()
        }
        var editor: SharedPreferences.Editor
        if (!booleanShared.getBoolean("FirstLaunch", true)) {
            editor = namesOfUseful.edit()
            for (i in startNamesOfUsefulApps.indices) {
                editor.putString(startNamesOfUsefulApps[i], startNamesOfUsefulApps[i])
            }
            editor.apply()
            editor = namesOfHarmful.edit()
            for (i in startNamesOfHarmfulApps.indices) {
                editor.putString(startNamesOfHarmfulApps[i], startNamesOfHarmfulApps[i])
            }
            editor.apply()

        }

    }


}





