package com.example.antip.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
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
        bindingOrNull = FragmentMenuBinding.bind(view)

        with(binding) {

            buttonChangeMode.setOnClickListener {
                findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToChangeMode())

            }
            buttonManageApps.setOnClickListener {
                onManageAppsClick()

            }
            buttonCheckStats.setOnClickListener {
                onStatsClick()

            }
            buttonBack.setOnClickListener {
                findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToMainFragment())
            }
        }

    }

    private fun onManageAppsClick() {
        lifecycleScope.launch {
            with(binding) {
                linear.visibility = View.INVISIBLE
                buttonBack.visibility = View.INVISIBLE
                loadImage.visibility = View.VISIBLE
                loadText.visibility = View.VISIBLE
                pBar.visibility = View.VISIBLE


            }

        }
        findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToAppManagerFragment())

    }

    private fun onStatsClick() {
        findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToStatsFragment())
    }

}