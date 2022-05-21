package com.example.antip.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.antip.R
import com.example.antip.databinding.FragmentMenuBinding
import kotlinx.coroutines.launch

class MenuFragment : Fragment(R.layout.fragment_menu) {
    private var bindingOrNull: FragmentMenuBinding? = null // ? is for nullable variable type
    private val binding get() = bindingOrNull!! // !! is for turning off null safety

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingOrNull= FragmentMenuBinding.bind(view)

        with(binding){
            buttonChangeMode.setOnClickListener{

            }
            buttonManageApps.setOnClickListener{
                onManageAppsClick()

            }
            buttonCheckStats.setOnClickListener{
                onStatsClick()

            }
        }

    }

    private fun onManageAppsClick(){
        lifecycleScope.launch {
            binding.pBar.visibility=View.VISIBLE
        }
        findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToSettingsFragment())

    }
    private fun onStatsClick(){
        findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToStatsFragment())
    }

}