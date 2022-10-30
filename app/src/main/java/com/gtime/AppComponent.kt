package com.gtime

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [UsageTimeUtilityModule::class,WorkerModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context):AppComponent
    }
    fun inject(app: App)
    fun activity():ActivityComponent
}