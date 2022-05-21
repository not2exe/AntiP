package com.example.antip.ui

import android.app.AlertDialog
import android.app.AppOpsManager
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.antip.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class DialogUsageSettings: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.permission_message)
                .setPositiveButton(
                    R.string.open_settings,
                    DialogInterface.OnClickListener { dialog, id ->
                        context?.let { requestUsageStatesPermission(it) }
                    })
                .setNegativeButton(
                    R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        findNavController().navigate(DialogUsageSettingsDirections.actionDialogUsageSettingsToMenuFragment())

                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    private fun requestUsageStatesPermission(context: Context) {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

}