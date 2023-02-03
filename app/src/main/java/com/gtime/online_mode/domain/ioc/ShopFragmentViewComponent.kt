package com.gtime.online_mode.domain.ioc

import com.gtime.general.ViewControllerModule
import com.gtime.general.scopes.FragmentViewScope
import com.gtime.online_mode.ui.ShopFragment
import dagger.Subcomponent

@FragmentViewScope
@Subcomponent(modules = [ViewControllerModule::class])
interface ShopFragmentViewComponent {
    fun inject(shopFragment: ShopFragment)
}