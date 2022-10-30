package com.gtime

import androidx.fragment.app.Fragment
import com.gtime.ui.ChangeModeFragment
import com.gtime.ui.ChangeModeViewController
import dagger.BindsInstance
import dagger.Subcomponent
@FragmentViewScope
@Subcomponent(modules = [ViewControllerModule::class])
interface ChangeModeViewComponent{
    @dagger.Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance changeModeFragment: ChangeModeFragment):ChangeModeViewComponent
    }
    fun inject(changeModeFragment: ChangeModeFragment)
}