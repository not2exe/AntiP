package com.antip.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.antip.R
import com.example.antip.databinding.FragmentChangeModeBinding
import com.example.antip.viewmodels.ChangeModeViewModel

class ChangeMode : Fragment(R.layout.fragment_change_mode) {
    var bindingOrNull: FragmentChangeModeBinding? = null
    private val binding get() = bindingOrNull!!
    private val viewModel by viewModels<ChangeModeViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingOrNull = FragmentChangeModeBinding.bind(view)
        with(binding) {
            buttonBack.setOnClickListener {
                findNavController().navigate(ChangeModeDirections.actionChangeModeToMenuFragment())
            }
            initRadioButtons()

            normalModeButton.setOnClickListener {
                viewModel.onNormalButtonClick(!normalModeButton.isChecked)
            }
            hardcoreModeButton.setOnClickListener {
                viewModel.onHardcoreButtonClick(!hardcoreModeButton.isChecked)
            }
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