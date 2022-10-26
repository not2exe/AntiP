package com.antip.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.antip.model.Cash

class ChangeModeViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>()
    private val cash: Cash = Cash(context)
    fun getIsLostHardcore(): Boolean {
        return cash.getFromBoolean("isLostHardcore")
    }

    fun getStateNormal(): Boolean {
        return cash.getFromBoolean("NormalMode")

    }

    fun getStateHardcore(): Boolean {
        return cash.getFromBoolean("HardcoreMode")
    }

    fun onNormalButtonClick(key: Boolean) {
        if (key)
            cash.inputIntoBoolean("NormalMode", false)
        else
            cash.inputIntoBoolean("NormalMode", true)

    }

    fun onHardcoreButtonClick(key: Boolean) {
        if (key)
            cash.inputIntoBoolean("HardcoreMode", false)
        else
            cash.inputIntoBoolean("HardcoreMode", true)

    }
}