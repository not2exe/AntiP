package com.gtime.general.activity

import androidx.activity.ComponentActivity
import com.gtime.general.scopes.ActivityScope
import com.gtime.general.ui.MainActivity
import com.gtime.offline_mode.domain.AppManagerFragmentComponent
import com.gtime.offline_mode.domain.ChangeModeViewComponent
import com.gtime.offline_mode.domain.MainFragmentComponent
import com.gtime.offline_mode.ui.AchievementsFragment
import com.gtime.online_mode.domain.ioc.LoginViewComponent
import com.gtime.online_mode.domain.ioc.ShopFragmentComponent
import com.gtime.online_mode.domain.ioc.TopFragmentComponent
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [YandexIdIntent::class])
interface ActivityComponent {

    @dagger.Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: ComponentActivity): ActivityComponent
    }

    fun inject(mainActivity: MainActivity)
    fun inject(achievementsFragment: AchievementsFragment)
    fun mainFragmentComponent(): MainFragmentComponent.Factory
    fun changeMenuViewComponent(): ChangeModeViewComponent.Factory
    fun appManagerFragmentComponent(): AppManagerFragmentComponent.Factory
    fun loginFragmentViewComponent(): LoginViewComponent.Factory
    fun topFragmentComponent(): TopFragmentComponent.Factory
    fun shopFragmentComponent(): ShopFragmentComponent.Factory
}