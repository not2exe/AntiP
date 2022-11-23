package com.gtime.domain

import com.gtime.ui.AppManagerFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [AdaptersModule::class, DragListenerModule::class])
interface AppManagerFragmentComponent {
    @dagger.Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance appManagerFragment: AppManagerFragment): AppManagerFragmentComponent
    }

    fun appManagerFragmentViewComponent(): AppManagerFragmentViewComponent.Factory
}