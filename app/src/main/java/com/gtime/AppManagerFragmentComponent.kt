package com.gtime

import androidx.fragment.app.Fragment
import com.gtime.ui.AppManagerFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [AdaptersModule::class])
interface AppManagerFragmentComponent {
    @dagger.Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance appManagerFragment: AppManagerFragment): AppManagerFragmentComponent
    }

    fun appManagerFragmentViewComponent(): AppManagerFragmentViewComponent.Factory
}