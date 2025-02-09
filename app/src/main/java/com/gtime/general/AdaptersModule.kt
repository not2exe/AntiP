package com.gtime.general

import android.content.ClipboardManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import com.gtime.general.scopes.FragmentScope
import com.gtime.general.ui.AppAdapter
import com.gtime.offline_mode.listeners.DragListener
import com.gtime.offline_mode.ui.AppManagerFragment
import com.gtime.offline_mode.ui.adapters.ManagerAdapter
import com.gtime.offline_mode.ui.stateholders.AppManagerFragmentViewModel
import com.gtime.online_mode.ui.ShopFragment
import com.gtime.online_mode.ui.TasksFragment
import com.gtime.online_mode.ui.logic.PromoCardAdapter
import com.gtime.online_mode.ui.logic.ShopAdapter
import com.gtime.online_mode.ui.logic.TasksAdapter
import com.gtime.online_mode.ui.logic.TopAdapter
import com.gtime.online_mode.ui.stateholders.ShopViewModel
import com.gtime.online_mode.ui.stateholders.TaskViewModel
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


        @FragmentScope
        @Provides
        fun provideTopAdapter(): TopAdapter = TopAdapter()

        @FragmentScope
        @Provides
        fun provideShopAdapter(
            viewModelFactory: ShopViewModel.Factory,
            fragment: ShopFragment
        ): ShopAdapter {
            val viewModel by fragment.viewModels<ShopViewModel> {
                LambdaFactory(fragment) { handle: SavedStateHandle ->
                    viewModelFactory.create(handle)
                }
            }
            return ShopAdapter(viewModel = viewModel)
        }

        @FragmentScope
        @Provides
        fun provideTaskAdapter(
            viewModelFactory: TaskViewModel.Factory,
            fragment: TasksFragment
        ): TasksAdapter {
            val viewModel by fragment.viewModels<TaskViewModel> {
                LambdaFactory(fragment) { handle: SavedStateHandle ->
                    viewModelFactory.create(handle)
                }
            }
            return TasksAdapter(viewModel)
        }

        @FragmentScope
        @Provides
        fun providePromoCardAdapter(clipboardManager: ClipboardManager): PromoCardAdapter =
            PromoCardAdapter(clipboardManager)


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