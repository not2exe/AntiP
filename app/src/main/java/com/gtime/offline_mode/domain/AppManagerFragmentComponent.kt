package com.gtime.offline_mode.domain

import com.gtime.general.AdaptersModule
import com.gtime.general.scopes.FragmentScope
import com.gtime.offline_mode.ui.AppManagerFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [AdaptersModule::class, DragListenerModule::class])
interface AppManagerFragmentComponent {
    @dagger.Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance appManagerFragment: AppManagerFragment): AppManagerFragmentComponent
    }

    fun appManagerFragmentViewComponent(): AppManagerFragmentViewComponent
}