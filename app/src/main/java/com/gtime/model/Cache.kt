package com.gtime.model

import android.content.Context
import androidx.core.content.edit
import com.gtime.Constants
import com.gtime.domain.AppScope
import javax.inject.Inject

@AppScope
class Cache @Inject constructor(applicationContext: Context) {
    private val booleanShared =
        applicationContext.getSharedPreferences(Constants.CACHE_BOOLEANS, Context.MODE_PRIVATE)
    private val intShared =
        applicationContext.getSharedPreferences(Constants.CACHE_INT, Context.MODE_PRIVATE)
    private val accShared =
        applicationContext.getSharedPreferences(Constants.CACHE_ACC, Context.MODE_PRIVATE)

    init {
        if (isFirstLaunch()) {
            initBoolean()
            initLives()
        }
    }

    fun saveAcc(accountInfo: AccountInfo) = accShared.edit {
        putString(Constants.KEY_NAME, accountInfo.name)
        putString(Constants.KEY_EMAIL, accountInfo.email)
        putString(Constants.KEY_URL, accountInfo.urlAvatar)
    }

    fun getAcc(): AccountInfo = AccountInfo(
        accShared.getString(Constants.KEY_NAME, "") ?: "",
        accShared.getString(Constants.KEY_EMAIL, "") ?: "",
        accShared.getString(Constants.KEY_URL, "") ?: ""
    )


    fun getFromBoolean(key: String): Boolean = booleanShared.getBoolean(key, false)

    fun inputIntoBoolean(key: String, value: Boolean) =
        booleanShared.edit().putBoolean(key, value).apply()

    fun incLives() {
        val life = getLives()
        if (life <= 3) {
            intShared.edit().putInt(Constants.KEY_LIFE, life + 1)
                .apply()
        }
    }


    fun decLives() {
        val life = getLives()
        if (life > 0) {
            intShared.edit().putInt(Constants.KEY_LIFE, life - 1)
                .apply()
        }
    }


    fun getLives(): Int = intShared.getInt(Constants.KEY_LIFE, 0)

    private fun isFirstLaunch(): Boolean =
        booleanShared.getBoolean(Constants.KEY_FIRST_LAUNCH, true)

    private fun initLives() {
        incLives()
        incLives()
        incLives()
    }

    private fun initBoolean() {
        booleanShared.edit {
            putBoolean(Constants.KEY_FIRST_LAUNCH, false)
            putBoolean(Constants.KEY_NORMAL_MODE, true)
            putBoolean(Constants.KEY_HARDCORE_MODE, false)
            apply()
        }
    }
}