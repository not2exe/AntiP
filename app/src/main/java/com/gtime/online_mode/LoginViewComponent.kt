package com.gtime.online_mode

import com.gtime.general.scopes.FragmentViewScope
import com.gtime.general.ViewControllerModule
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentViewScope
@Subcomponent(modules = [ViewControllerModule::class])
interface LoginViewComponent {
    @dagger.Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: LoginFragment): LoginViewComponent
    }
    fun inject(fragment: LoginFragment)
}