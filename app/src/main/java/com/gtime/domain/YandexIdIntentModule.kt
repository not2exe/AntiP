package com.gtime.domain

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.gtime.model.IDRepository
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Module
interface YandexIdIntent {
    companion object {
        @Provides
        @ActivityScope
        fun provideLauncher(
            activity: ComponentActivity,
            sdk: YandexAuthSdk,
            idRepository: IDRepository,
            scope: CoroutineScope
        ):ActivityResultLauncher<Intent> =
            activity.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                try {
                    val yandexAuthToken: YandexAuthToken? = sdk.extractToken(it.resultCode, it.data)
                    if (yandexAuthToken != null) {
                        scope.launch {
                            idRepository.getDecodedJWT(yandexAuthToken)
                        }
                    }
                } catch (e: YandexAuthException) {
                    // Process error
                }
            }

    }
}