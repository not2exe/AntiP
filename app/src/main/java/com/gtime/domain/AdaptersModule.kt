package com.gtime.domain

import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import com.gtime.Constants
import com.gtime.listeners.DragListener
import com.gtime.ui.AppManagerFragment
import com.gtime.ui.adapters.AppAdapter
import com.gtime.ui.adapters.ManagerAdapter
import com.gtime.ui.adapters.ManagerViewHolder
import com.gtime.ui.stateholders.AppManagerFragmentViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
interface AdaptersModule {
    companion object {
        @FragmentScope
        @Provides
        fun provideAppAdapter(): AppAdapter = AppAdapter()

        @FragmentScope
        @Provides
        @Named(Constants.ADAPTER_HARMFUL)
        fun provideHarmfulAdapter(
            dragListener: DragListener,
            fragment: AppManagerFragment,
            viewModelFactory: AppManagerFragmentViewModel.Factory
        ): ManagerAdapter = createAdapter(dragListener, fragment, viewModelFactory)


        @FragmentScope
        @Provides
        @Named(Constants.ADAPTER_USEFUL)
        fun provideUsefulAdapter(
            dragListener: DragListener,
            fragment: AppManagerFragment,
            viewModelFactory: AppManagerFragmentViewModel.Factory
        ): ManagerAdapter =
            createAdapter(dragListener, fragment, viewModelFactory)

        @FragmentScope
        @Provides
        @Named(Constants.ADAPTER_OTHERS)
        fun provideOthersAdapter(
            dragListener: DragListener,
            fragment: AppManagerFragment,
            viewModelFactory: AppManagerFragmentViewModel.Factory
        ): ManagerAdapter =
            createAdapter(dragListener, fragment, viewModelFactory)

        private fun createAdapter(
            dragListener: DragListener,
            fragment: AppManagerFragment,
            viewModelFactory: AppManagerFragmentViewModel.Factory
        ): ManagerAdapter {
            val viewModel by fragment.viewModels<AppManagerFragmentViewModel> {
                LambdaFactory(fragment) { handle: SavedStateHandle ->
                    viewModelFactory.create(handle)
                }
            }
            return ManagerAdapter(dragListener, viewModel)
        }

    }
}