package com.example.antip.viewmodels

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

    fun onNormalButtonClick() {
        cash.inputIntoBoolean("NormalMode", true)
        cash.inputIntoBoolean("HardcoreMode", false)


    }

    fun onHardcoreButtonClick() {
        cash.inputIntoBoolean("HardcoreMode", true)
        cash.inputIntoBoolean("NormalMode", false)

    }
}