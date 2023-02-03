package com.gtime.offline_mode.domain

import com.gtime.general.ViewControllerModule
import com.gtime.general.scopes.FragmentViewScope
import com.gtime.general.ui.MainFragment
import dagger.Subcomponent

@FragmentViewScope
@Subcomponent(modules = [ViewControllerModule::class])
interface MainFragmentViewComponent {
    fun inject(mainFragment: MainFragment)
}