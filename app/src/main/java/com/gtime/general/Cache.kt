package com.gtime.general

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gtime.general.scopes.AppScope
import com.gtime.online_mode.AccountInfo
import javax.inject.Inject

@AppScope
class Cache @Inject constructor(applicationContext: Context) {
    private val booleanShared =
        applicationContext.getSharedPreferences(Constants.CACHE_BOOLEANS, Context.MODE_PRIVATE)
    private val intShared =
        applicationContext.getSharedPreferences(Constants.CACHE_INT, Context.MODE_PRIVATE)
    private val accShared =
        applicationContext.getSharedPreferences(Constants.CACHE_ACC, Context.MODE_PRIVATE)

    val onlineLiveData = MutableLiveData<Boolean>(isOnline())

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
        accShared.getString(Constants.KEY_URL, "") ?: "",
        Firebase.auth.currentUser != null
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


    fun setOnline(isOnline: Boolean) {
        booleanShared.edit { putBoolean(Constants.IS_ONLINE, isOnline) }
        onlineLiveData.value = isOnline
    }

    fun isOnline(): Boolean = booleanShared.getBoolean(Constants.IS_ONLINE, false)
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