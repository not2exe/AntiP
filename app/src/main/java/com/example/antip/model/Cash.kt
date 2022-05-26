package com.example.antip.model

import android.content.Context
import android.content.SharedPreferences

class Cash(private val context: Context) {
    private val startNamesOfUsefulApps = arrayOf(
        "CoolReader", "InShot", "KineMaster", "PicsArt", "Strava",
        "Sleep Cycle", "Daylio", "Calm", "Seven", "Brainly",
        "English", "TED", "GitHub", "Canva", "WolframAlpha",
        "Кинопоиск", "Webinar", "Kaspersky", "Lumosicty", "AccuBattery",
        "GetCourse", "MyBook", "Zoom", "Figma"
    )
    private val startNamesOfHarmfulApps = arrayOf(
        "TikTok", "Instagram", "Facebook", "Netflix",
        "YouTube", "Twitter", "Pinterest", "Snapchat", "WhatsApp",
        "Reddit", "Twitch", "VK", "Telegram", "Hearthstone", "Discord"
    )
    private val namesOfUseful = context.getSharedPreferences("nameOfUseful", Context.MODE_PRIVATE)
    private val namesOfHarmful = context.getSharedPreferences("nameOfHarmful", Context.MODE_PRIVATE)
    private val booleanShared = context.getSharedPreferences("booleanShared", Context.MODE_PRIVATE)

    init {

        if (booleanShared.all.isEmpty())
            initBoolean()

        if (namesOfHarmful.all.isEmpty() && namesOfUseful.all.isEmpty())
            initCashWithNames()


    }

    private fun initCashWithNames() {
        var editor: SharedPreferences.Editor = namesOfUseful.edit()
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

    private fun initBoolean() {
        val editor: SharedPreferences.Editor = booleanShared.edit()
        editor.putBoolean("FirstLaunch", true)
        editor.putBoolean("NormalMode", true)
        editor.putBoolean("HardcoreMode", false)
        editor.putBoolean("AchievementHardcore", false)
        editor.apply()
    }

    fun inputIntoBoolean(key:String,value:Boolean){
        booleanShared.edit().putBoolean(key,value).apply()

    }





    fun getAllUseful(): MutableCollection<out Any?> {
        return namesOfUseful.all.values

    }

    fun getAllHarmful(): MutableCollection<out Any?> {
        return namesOfHarmful.all.values
    }

    fun inputIntoHarmful(value: String) {
        namesOfHarmful.edit().putString(value, value).apply()

    }

    fun inputIntoUseful(value: String) {
        namesOfUseful.edit().putString(value, value).apply()
    }

    fun removeFromUseful(key: String) {
        namesOfUseful.edit().remove(key).apply()
    }

    fun removeFromHarmful(key: String) {
        namesOfHarmful.edit().remove(key).apply()

    }

    fun getFromBoolean(key:String): Boolean {
        return booleanShared.getBoolean(key, false)

    }
}