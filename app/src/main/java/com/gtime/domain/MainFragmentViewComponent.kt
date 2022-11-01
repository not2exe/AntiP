package com.gtime.domain

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.gtime.ui.MainFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentViewScope
@Subcomponent(modules = [ViewControllerModule::class])
interface MainFragmentViewComponent {
    @dagger.Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance view: View,
            @BindsInstance viewLifecycleOwner: LifecycleOwner
        ): MainFragmentViewComponent
    }
    fun inject(mainFragment: MainFragment)
}