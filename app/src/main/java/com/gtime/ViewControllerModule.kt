package com.gtime

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.example.antip.databinding.FragmentAppManagerBinding
import com.example.antip.databinding.FragmentChangeModeBinding
import com.example.antip.databinding.FragmentMainBinding
import com.gtime.adapters.AppAdapter
import com.gtime.adapters.ManagerAdapter
import com.gtime.ui.AppManagerFragment
import com.gtime.ui.ChangeModeFragment
import com.gtime.ui.ChangeModeViewController
import com.gtime.ui.MainFragment
import com.gtime.viewmodels.AppManagerFragmentViewModel
import com.gtime.viewmodels.ChangeModeViewModel
import com.gtime.viewmodels.MainFragmentViewModel
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
            view: View,
            lifecycleOwner: LifecycleOwner,
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
                viewLifecycleOwner = lifecycleOwner,
                binding = FragmentMainBinding.bind(view),
                navController = fragment.findNavController(),
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
            view: View,
            viewLifecycleOwner: LifecycleOwner
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
                binding = FragmentAppManagerBinding.bind(view),
                viewLifecycleOwner = viewLifecycleOwner
            )

        }
    }
}