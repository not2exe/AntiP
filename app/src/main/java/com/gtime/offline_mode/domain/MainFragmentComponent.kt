package com.gtime.offline_mode.domain

import com.gtime.general.scopes.FragmentScope
import com.gtime.general.ui.MainFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [AdaptersModule::class])
interface MainFragmentComponent {
    @dagger.Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: MainFragment,
        ): MainFragmentComponent
    }

    fun mainFragmentViewComponent(): MainFragmentViewComponent
}