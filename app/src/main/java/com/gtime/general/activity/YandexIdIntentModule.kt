package com.gtime.general.activity

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.example.antip.R
import com.google.android.material.snackbar.Snackbar
import com.gtime.general.scopes.ActivityScope
import com.gtime.online_mode.data.AccountRepository
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Module
interface YandexIdIntent {
    companion object {
        @Provides
        @ActivityScope
        fun provideLauncher(
            activity: ComponentActivity,
            sdk: YandexAuthSdk,
            accountRepository: AccountRepository,
            scope: CoroutineScope,
        ): ActivityResultLauncher<YandexAuthLoginOptions> =
            activity.registerForActivityResult(
                sdk.contract
            ) {
                when (it) {
                    is YandexAuthResult.Success -> {
                        scope.launch {
                            accountRepository.getDecodedJWT(it.token)
                            withContext(Dispatchers.Main) {
                                activity.findNavController(R.id.fcvMainContainer)
                                    .navigate(R.id.loginFragment)
                                activity.findViewById<DrawerLayout>(R.id.drawerLayout)
                                    .closeDrawer(GravityCompat.START)
                            }
                        }
                    }

                    is YandexAuthResult.Failure -> {
                        Snackbar.make(
                            activity.findViewById(android.R.id.content),
                            R.string.error_authorization,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    is YandexAuthResult.Cancelled -> {

                    }

                }
            }

    }
}