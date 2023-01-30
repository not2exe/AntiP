package com.gtime.online_mode

import com.gtime.general.AdaptersModule
import com.gtime.general.scopes.FragmentScope
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [AdaptersModule::class])
interface TopFragmentComponent {
    @dagger.Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: TopFragment): TopFragmentComponent
    }

    fun topFragmentViewComponent(): TopFragmentViewComponent
}