package com.gtime.domain

import com.gtime.ui.ChangeModeFragment
import dagger.BindsInstance
import dagger.Subcomponent
@FragmentViewScope
@Subcomponent(modules = [ViewControllerModule::class])
interface ChangeModeViewComponent{
    @dagger.Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance changeModeFragment: ChangeModeFragment): ChangeModeViewComponent
    }
    fun inject(changeModeFragment: ChangeModeFragment)
}