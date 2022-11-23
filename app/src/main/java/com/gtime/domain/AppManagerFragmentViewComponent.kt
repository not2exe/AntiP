package com.gtime.domain

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.gtime.ui.AppManagerFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentViewScope
@Subcomponent(modules = [ViewControllerModule::class])
interface AppManagerFragmentViewComponent {
    @dagger.Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance view: View, @BindsInstance viewLifecycleOwner: LifecycleOwner): AppManagerFragmentViewComponent
    }

    fun inject(appManagerFragment: AppManagerFragment)
}