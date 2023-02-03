package com.gtime.offline_mode.domain

import com.gtime.general.ViewControllerModule
import com.gtime.general.scopes.FragmentViewScope
import com.gtime.offline_mode.ui.ChangeModeFragment
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