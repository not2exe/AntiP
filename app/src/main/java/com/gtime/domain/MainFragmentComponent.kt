package com.gtime.domain

import com.gtime.ui.MainFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [AdaptersModule::class])
interface MainFragmentComponent {
    @dagger.Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: MainFragment): MainFragmentComponent
    }
    fun mainFragmentViewComponent(): MainFragmentViewComponent.Factory
}