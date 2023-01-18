package com.gtime.offline_mode.domain

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import com.example.antip.R
import com.gtime.general.Constants
import com.gtime.general.LambdaFactory
import com.gtime.general.model.db.AppDataBaseEntity
import com.gtime.general.scopes.FragmentScope
import com.gtime.offline_mode.listeners.DragListener
import com.gtime.offline_mode.ui.AppManagerFragment
import com.gtime.offline_mode.ui.adapters.AppAdapter
import com.gtime.offline_mode.ui.adapters.ManagerAdapter
import com.gtime.offline_mode.ui.stateholders.AppManagerFragmentViewModel
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
            viewModelFactory: AppManagerFragmentViewModel.Factory,
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