package com.gtime.domain

import android.content.Context
import com.gtime.Constants
import com.gtime.ui.adapters.AppAdapter
import com.gtime.ui.adapters.ManagerAdapter
import com.gtime.listeners.DragListener
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
        fun provideHarmfulAdapter(dragListener: DragListener,context: Context): ManagerAdapter =
            ManagerAdapter(dragListener,context)

        @FragmentScope
        @Provides
        @Named(Constants.ADAPTER_USEFUL)
        fun provideUsefulAdapter(dragListener: DragListener,context: Context): ManagerAdapter =
            ManagerAdapter(dragListener,context)

        @FragmentScope
        @Provides
        @Named(Constants.ADAPTER_OTHERS)
        fun provideOthersAdapter(dragListener: DragListener,context: Context): ManagerAdapter =
            ManagerAdapter(dragListener,context)

    }
}