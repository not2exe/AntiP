package com.gtime.general.app.modules

import android.content.Context
import android.content.Intent
import com.gtime.general.scopes.AppScope
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.Module
import dagger.Provides

@Module
interface YandexIDModule {
    companion object {
        @AppScope
        @Provides
        fun provideSdk(context: Context): YandexAuthSdk =
            YandexAuthSdk.create(YandexAuthOptions(context))

        @AppScope
        @Provides
        fun provideLoginOptionsBuilder(): YandexAuthLoginOptions =
            YandexAuthLoginOptions()
    }
}
