package com.gtime.online_mode

import com.gtime.general.ViewControllerModule
import com.gtime.general.scopes.FragmentViewScope
import dagger.Subcomponent


@FragmentViewScope
@Subcomponent(modules = [ViewControllerModule::class])
interface TopFragmentViewComponent {
    fun inject(fragment: TopFragment)
}