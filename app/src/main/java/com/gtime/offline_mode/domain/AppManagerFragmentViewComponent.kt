package com.gtime.offline_mode.domain

import com.gtime.general.ViewControllerModule
import com.gtime.general.scopes.FragmentViewScope
import com.gtime.offline_mode.ui.AppManagerFragment
import dagger.Subcomponent

@FragmentViewScope
@Subcomponent(modules = [ViewControllerModule::class])
interface AppManagerFragmentViewComponent {
    fun inject(appManagerFragment: AppManagerFragment)
}