package com.gtime.domain

import com.gtime.MainActivity
import com.gtime.ui.AchievementsFragment
import dagger.Subcomponent

@Subcomponent
interface ActivityComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(achievementsFragment: AchievementsFragment)
    fun mainFragmentComponent(): MainFragmentComponent.Factory
    fun changeMenuViewComponent(): ChangeModeViewComponent.Factory
    fun appManagerFragmentComponent(): AppManagerFragmentComponent.Factory
}