package com.gtime.domain

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.example.antip.databinding.FragmentAppManagerBinding
import com.example.antip.databinding.FragmentChangeModeBinding
import com.example.antip.databinding.FragmentMainBinding
import com.example.antip.databinding.NavHeaderMainBinding
import com.google.android.material.navigation.NavigationView
import com.gtime.Constants
import com.gtime.MainActivity
import com.gtime.ui.*
import com.gtime.ui.adapters.AppAdapter
import com.gtime.ui.adapters.ManagerAdapter
import com.gtime.ui.stateholders.AppManagerFragmentViewModel
import com.gtime.ui.stateholders.ChangeModeViewModel
import com.gtime.ui.stateholders.MainFragmentViewModel
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthSdk
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
interface ViewControllerModule {
    companion object {
        @FragmentViewScope
        @Provides
        fun provideMainViewController(
            fragment: MainFragment,
            mainFragmentViewModelFactory: MainFragmentViewModel.Factory,
            adapter: AppAdapter
        ): MainViewController {
            val viewModel by fragment.viewModels<MainFragmentViewModel> {
                LambdaFactory(
                    fragment
                ) { handle: SavedStateHandle ->
                    mainFragmentViewModelFactory.create(handle)
                }
            }
            return MainViewController(
                viewModel = viewModel,
                viewLifecycleOwner = fragment.viewLifecycleOwner,
                binding = FragmentMainBinding.bind(fragment.requireView()),
                context = fragment.requireContext(),
                adapter = adapter
            )
        }

        @FragmentViewScope
        @Provides
        fun provideChangeModeViewController(
            fragment: ChangeModeFragment,
            changeViewModelFactory: ChangeModeViewModel.Factory
        ): ChangeModeViewController {
            val viewModel by fragment.viewModels<ChangeModeViewModel> {
                LambdaFactory(
                    fragment
                ) { handle: SavedStateHandle ->
                    changeViewModelFactory
                        .create(handle)
                }
            }
            return ChangeModeViewController(
                binding = FragmentChangeModeBinding.bind(fragment.requireView()),
                viewModel = viewModel,
                navController = fragment.findNavController()
            )
        }

        @FragmentViewScope
        @Provides
        fun provideAppManagerViewController(
            @Named(Constants.ADAPTER_HARMFUL) harmfulAdapter: ManagerAdapter,
            @Named(Constants.ADAPTER_USEFUL) usefulAdapter: ManagerAdapter,
            @Named(Constants.ADAPTER_OTHERS) othersAdapter: ManagerAdapter,
            fragment: AppManagerFragment,
            appManagerFragmentViewModelFactory: AppManagerFragmentViewModel.Factory,
        ): AppManagerViewController {
            val viewModel by fragment.viewModels<AppManagerFragmentViewModel> {
                LambdaFactory(fragment) { handle: SavedStateHandle ->
                    appManagerFragmentViewModelFactory.create(handle)
                }
            }
            return AppManagerViewController(
                harmfulAdapter = harmfulAdapter,
                usefulAdapter = usefulAdapter,
                othersAdapter = othersAdapter,
                navController = fragment.findNavController(),
                viewModel = viewModel,
                binding = FragmentAppManagerBinding.bind(fragment.requireView()),
                viewLifecycleOwner = fragment.viewLifecycleOwner
            )
        }

    }
}