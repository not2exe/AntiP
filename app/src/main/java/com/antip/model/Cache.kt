package com.antip.model

import android.content.Context
import androidx.core.content.edit
import com.antip.AppScope
import com.antip.Constants
import javax.inject.Inject

@AppScope
class Cache @Inject constructor(applicationContext: Context) {
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
    private val namesOfUseful =
        applicationContext.getSharedPreferences(Constants.CACHE_USEFUL, Context.MODE_PRIVATE)
    private val namesOfHarmful =
        applicationContext.getSharedPreferences(Constants.CACHE_HARMFUL, Context.MODE_PRIVATE)
    private val booleanShared =
        applicationContext.getSharedPreferences(Constants.CACHE_BOOLEANS, Context.MODE_PRIVATE)

    init {
        if(isFirstLaunch()){
            initBoolean()
            initCashWithNames()
        }
    }

    private fun initCashWithNames() {
        namesOfUseful.edit {
            for (i in startNamesOfUsefulApps.indices) {
                putString(startNamesOfUsefulApps[i], startNamesOfUsefulApps[i])
            }
            apply()
        }
        namesOfHarmful.edit {
            for (i in startNamesOfHarmfulApps.indices) {
                putString(startNamesOfHarmfulApps[i], startNamesOfHarmfulApps[i])
            }
            apply()
        }
    }

    private fun initBoolean() {
        booleanShared.edit {
            putBoolean(Constants.KEY_FIRST_LAUNCH, true)
            putBoolean(Constants.KEY_NORMAL_MODE, true)
            putBoolean(Constants.KEY_HARDCORE_MODE, false)
            putBoolean(Constants.KEY_ACHIEVEMENT_HARDCORE, false)
            apply()
        }
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

    fun isFirstLaunch(): Boolean = booleanShared.getBoolean(Constants.KEY_FIRST_LAUNCH, false)

}