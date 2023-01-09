package com.gtime.domain

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.gtime.ui.MainFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentViewScope
@Subcomponent(modules = [ViewControllerModule::class])
interface MainFragmentViewComponent {
    fun inject(mainFragment: MainFragment)
}