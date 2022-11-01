package com.gtime.model

import android.content.Context
import androidx.core.content.edit
import com.gtime.Constants
import com.gtime.domain.AppScope
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
    private val intShared =
        applicationContext.getSharedPreferences(Constants.CACHE_INT, Context.MODE_PRIVATE)

    init {
        if (isFirstLaunch()) {
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
            putBoolean(Constants.KEY_FIRST_LAUNCH, false)
            putBoolean(Constants.KEY_NORMAL_MODE, true)
            putBoolean(Constants.KEY_HARDCORE_MODE, false)
            apply()
        }
    }

    fun getAllUseful(): MutableCollection<out Any?> = namesOfUseful.all.values

    fun getAllHarmful(): MutableCollection<out Any?> = namesOfHarmful.all.values

    fun inputIntoHarmful(value: String) = namesOfHarmful.edit().putString(value, value).apply()

    fun inputIntoUseful(value: String) = namesOfUseful.edit().putString(value, value).apply()

    fun removeFromUseful(key: String) = namesOfUseful.edit().remove(key).apply()

    fun removeFromHarmful(key: String) = namesOfHarmful.edit().remove(key).apply()

    fun getFromBoolean(key: String): Boolean = booleanShared.getBoolean(key, false)

    fun inputIntoBoolean(key: String, value: Boolean) =
        booleanShared.edit().putBoolean(key, value).apply()

    fun incLife() {
        val life = getLife()
        if (life <= 3) {
            intShared.edit().putInt(Constants.KEY_LIFE, life + 1)
                .apply()
        }
    }


    fun decLife() {
        val life = getLife()
        if (life > 0) {
            intShared.edit().putInt(Constants.KEY_LIFE, life - 1)
                .apply()
        }
    }


    fun getLife(): Int = intShared.getInt(Constants.KEY_LIFE, 0)

    private fun isFirstLaunch(): Boolean =
        booleanShared.getBoolean(Constants.KEY_FIRST_LAUNCH, true)
}