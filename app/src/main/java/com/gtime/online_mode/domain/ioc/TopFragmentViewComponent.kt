package com.gtime.online_mode.domain.ioc

import com.gtime.general.ViewControllerModule
import com.gtime.general.scopes.FragmentViewScope
import com.gtime.online_mode.ui.TopFragment
import dagger.Subcomponent


@FragmentViewScope
@Subcomponent(modules = [ViewControllerModule::class])
interface TopFragmentViewComponent {
    fun inject(fragment: TopFragment)
}