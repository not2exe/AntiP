package com.gtime.offline_mode.domain

import androidx.fragment.app.viewModels
import com.gtime.general.LambdaFactory
import com.gtime.general.scopes.FragmentScope
import com.gtime.offline_mode.listeners.DragListener
import com.gtime.offline_mode.ui.AppManagerFragment
import com.gtime.offline_mode.ui.stateholders.AppManagerFragmentViewModel
import com.notexe.gtime.databinding.FragmentAppManagerBinding
import dagger.Module
import dagger.Provides

@Module
interface DragListenerModule {
    companion object {
        @FragmentScope
        @Provides
        fun provideDragListener(
            fragment: AppManagerFragment,
            viewModelFactory: AppManagerFragmentViewModel.Factory
        ): DragListener {
            val viewModel by fragment.viewModels<AppManagerFragmentViewModel> {
                LambdaFactory(fragment) {
                    viewModelFactory.create(it)
                }
            }
            return DragListener(viewModel, FragmentAppManagerBinding.bind(fragment.requireView()))
        }
    }
}