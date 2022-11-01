package com.gtime.domain

import androidx.fragment.app.viewModels
import com.gtime.listeners.DragListener
import com.gtime.ui.AppManagerFragment
import com.gtime.ui.stateholders.AppManagerFragmentViewModel
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
            return DragListener(viewModel)
        }
    }
}