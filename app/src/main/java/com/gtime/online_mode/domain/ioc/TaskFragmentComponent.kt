package com.gtime.online_mode.domain.ioc

import com.gtime.general.AdaptersModule
import com.gtime.general.scopes.FragmentScope
import com.gtime.online_mode.ui.TasksFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [AdaptersModule::class])
interface TaskFragmentComponent {
    @dagger.Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: TasksFragment): TaskFragmentComponent
    }

    fun taskFragmentViewComponent(): TaskFragmentViewComponent
}