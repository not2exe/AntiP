package com.gtime.ui

import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import com.example.antip.databinding.FragmentMenuBinding
import kotlinx.coroutines.launch

class MenuViewController(
    private val binding: FragmentMenuBinding,
    private val navController: NavController,
    private val lifecycleScope: LifecycleCoroutineScope
) {
    fun setupViews(){
        setupButtons()
    }
    private fun setupButtons() = with(binding) {
        buttonChangeMode.setOnClickListener {
            navController.navigate(MenuFragmentDirections.actionMenuFragmentToChangeModeFragment())

        }
        buttonManageApps.setOnClickListener {
            onManageAppsClick()

        }
        buttonCheckStats.setOnClickListener {
            onStatsClick()

        }
        buttonBack.setOnClickListener {
            navController.navigate(MenuFragmentDirections.actionMenuFragmentToMainFragment())
        }
    }

    private fun onManageAppsClick() {
        navController.navigate(MenuFragmentDirections.actionMenuFragmentToAppManagerFragment())

    }

    private fun onStatsClick() {
        navController.navigate(MenuFragmentDirections.actionMenuFragmentToAchievementsFragment())
    }
}