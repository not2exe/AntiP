package com.gtime.general.app

import android.content.Context
import com.gtime.general.activity.ActivityComponent
import com.gtime.general.scopes.AppScope
import com.gtime.general.app.modules.*
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [WorkerModule::class, UsageTimeUtilityModule::class, ScopeModule::class, LocalStoragesModule::class, YandexIDModule::class,FirebaseModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun inject(app: App)
    fun activity(): ActivityComponent.Factory
}