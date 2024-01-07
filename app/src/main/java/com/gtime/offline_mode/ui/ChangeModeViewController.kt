package com.gtime.offline_mode.ui

import android.view.View
import androidx.navigation.NavController
import com.gtime.offline_mode.ui.stateholders.ChangeModeViewModel
import com.notexe.gtime.databinding.FragmentChangeModeBinding

class ChangeModeViewController(
    private val binding: FragmentChangeModeBinding,
    private val viewModel: ChangeModeViewModel,
    private val navController: NavController
) {
    fun setupViews() {
        setupButtons()
    }

    private fun setupButtons() = with(binding) {

        initRadioButtons()

        normalModeButton.setOnClickListener {
            viewModel.onNormalButtonClick(normalModeButton.isChecked)
        }
        hardcoreModeButton.setOnClickListener {
            viewModel.onHardcoreButtonClick(hardcoreModeButton.isChecked)
        }
    }

    private fun initRadioButtons() = with(binding) {
        if (viewModel.getIsLostHardcore()) {
            hardcoreModeButton.visibility = View.INVISIBLE
            viewModel.onNormalButtonClick(normalModeButton.isChecked)
            normalModeButton.isChecked = true

        }
        normalModeButton.isChecked = viewModel.getStateNormal()
        hardcoreModeButton.isChecked = viewModel.getStateHardcore()
    }
}