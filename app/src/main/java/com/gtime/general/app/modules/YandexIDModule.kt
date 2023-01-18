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
            YandexAuthSdk(context, YandexAuthOptions.Builder(context).build())

        @AppScope
        @Provides
        fun provideLoginOptionsBuilder(): YandexAuthLoginOptions.Builder =
            YandexAuthLoginOptions.Builder()

        @AppScope
        @Provides
        fun provideIntentID(
            sdk: YandexAuthSdk,
            loginOptionsBuilder: YandexAuthLoginOptions.Builder
        ): Intent = sdk.createLoginIntent(loginOptionsBuilder.build())
    }
}
