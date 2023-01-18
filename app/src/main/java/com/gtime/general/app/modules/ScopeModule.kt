package com.gtime.general.app.modules

import com.gtime.general.scopes.AppScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
interface ScopeModule {
    companion object {
        @AppScope
        @Provides
        fun provideCoroutineScope(): CoroutineScope =
            CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }
}