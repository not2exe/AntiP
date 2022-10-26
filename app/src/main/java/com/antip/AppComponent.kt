package com.antip

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [UsageTimeUtility::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context):AppComponent
    }
}