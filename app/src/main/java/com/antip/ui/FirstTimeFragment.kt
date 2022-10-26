package com.antip.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.antip.R
import com.example.antip.databinding.FragmentFirstTimeBinding

class FirstTimeFragment : Fragment(R.layout.fragment_first_time) {
    private var bindingOrNull: FragmentFirstTimeBinding? = null
    private val binding get() = bindingOrNull!!
    private var onPauseWas = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingOrNull = FragmentFirstTimeBinding.bind(view)
        binding.cancelButton.setOnClickListener {
            findNavController().navigate(FirstTimeFragmentDirections.actionFirstTimeFragmentToMenuFragment())
        }
        binding.openSettingsButton.setOnClickListener {
            requestUsagePermission()

        }

    }

    override fun onPause() {
        onPauseWas = true
        super.onPause()
    }

    override fun onResume() {
        if (onPauseWas)
            findNavController().navigate(FirstTimeFragmentDirections.actionFirstTimeFragmentToMainFragment())
        super.onResume()
    }

    private fun requestUsagePermission() {
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }
}