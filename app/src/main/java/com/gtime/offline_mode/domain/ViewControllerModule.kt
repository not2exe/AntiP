package com.gtime.offline_mode.domain

import android.content.Context
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.example.antip.R
import com.example.antip.databinding.FragmentAppManagerBinding
import com.example.antip.databinding.FragmentChangeModeBinding
import com.example.antip.databinding.FragmentMainBinding
import com.gtime.general.Constants
import com.gtime.general.LambdaFactory
import com.gtime.general.model.UsageTimeRepository
import com.gtime.general.scopes.FragmentViewScope
import com.gtime.offline_mode.ui.*
import com.gtime.offline_mode.ui.adapters.AppAdapter
import com.gtime.offline_mode.ui.adapters.ManagerAdapter
import com.gtime.offline_mode.ui.stateholders.AppManagerFragmentViewModel
import com.gtime.offline_mode.ui.stateholders.ChangeModeViewModel
import com.gtime.offline_mode.ui.stateholders.MainFragmentViewModel
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
            context: Context,
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
                adapterPredicts = ArrayAdapter(
                    context,
                    R.layout.spiner_item
                ),
                viewModel = viewModel,
                binding = FragmentAppManagerBinding.bind(fragment.requireView()),
                viewLifecycleOwner = fragment.viewLifecycleOwner
            )
        }

    }
}