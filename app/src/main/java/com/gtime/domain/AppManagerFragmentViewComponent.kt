package com.gtime.domain

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.gtime.ui.AppManagerFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentViewScope
@Subcomponent(modules = [ViewControllerModule::class])
interface AppManagerFragmentViewComponent {
    fun inject(appManagerFragment: AppManagerFragment)
}