package com.example.antip.model

import android.content.Context
import android.content.SharedPreferences

class CashWithNames(context: Context) {
    private val startNamesOfUsefulApps = arrayOf(
        "CoolReader", "InShot", "KineMaster", "PicsArt", "Strava",
        "Sleep Cycle", "Daylio", "Calm", "Seven", "Brainly",
        "English", "TED", "GitHub", "Canva", "WolframAlpha",
        "Кинопоиск", "Webinar", "Kaspersky", "Lumosicty", "AccuBattery",
        "GetCourse", "MyBook"
    )
    private val startNamesOfHarmfulApps = arrayOf(
        "TikTok", "Instagram", "Facebook", "Zoom", "Netflix",
        "YouTube", "Twitter", "Pinterest", "Snapchat", "WhatsApp",
        "Reddit", "Twitch", "VK", "Spotify", "Hearthstone", "Discord"
    )
    private val namesOfUseful = context.getSharedPreferences("nameOfUseful", Context.MODE_PRIVATE)
    private val namesOfHarmful = context.getSharedPreferences("nameOfHarmful", Context.MODE_PRIVATE)
    private val isInited=context.getSharedPreferences("SettingsFragmentIsInit",Context.MODE_PRIVATE)

    init {
        var editor:SharedPreferences.Editor=isInited.edit()
        editor.putBoolean("Init",false)
        editor.apply()
        if (namesOfHarmful.all.isEmpty() && namesOfHarmful.all.isEmpty()){
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