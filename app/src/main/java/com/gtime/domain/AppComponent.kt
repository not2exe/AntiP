package com.gtime.domain

import android.content.Context
import com.gtime.app.App
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [WorkerModule::class,UsageTimeUtilityModule::class,ScopeModule::class,LocalStoragesModule::class])
interface AppComponent{
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun inject(app: App)
    fun activity(): ActivityComponent
}