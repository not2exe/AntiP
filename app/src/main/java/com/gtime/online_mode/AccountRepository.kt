package com.gtime.online_mode

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gtime.general.Cache
import com.gtime.general.Constants
import com.gtime.general.scopes.AppScope
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken
import io.github.nefilim.kjwt.JWT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AppScope
class AccountRepository @Inject constructor(
    private val yandexAuthSdk: YandexAuthSdk,
    private val cache: Cache
) {
    val accountInfo = MutableLiveData<AccountInfo>()

    init {
        val user = Firebase.auth.currentUser
        accountInfo.value =
            AccountInfo(
                user?.displayName ?: "",
                user?.email ?: "",
                user?.photoUrl.toString(),
                Firebase.auth.currentUser != null
            )
    }

    suspend fun getDecodedJWT(yandexAuthToken: YandexAuthToken?) = withContext(Dispatchers.IO) {
        yandexAuthToken ?: return@withContext
        JWT.decode(yandexAuthSdk.getJwt(yandexAuthToken)).tap {
            val acc = AccountInfo(
                name = it.claimValue(Constants.DISPLAY_NAME).orNull() ?: "",
                email = it.claimValue(Constants.EMAIL).orNull() ?: "",
                urlAvatar = Constants.AVATAR_URL_START + it.claimValue(Constants.AVATAR_ID)
                    .orNull()
            )
            accountInfo.postValue(
                acc
            )
        }
    }

    fun isOnline(): Boolean = cache.getFromBoolean(Constants.IS_ONLINE)
    fun clearAccountInfo() {
        val emptyAcc = AccountInfo("", "", "")
        accountInfo.value = emptyAcc
    }

    fun successAuthFirebase() {
        val acc = accountInfo.value ?: return
        accountInfo.value = AccountInfo(acc.name, acc.email, acc.urlAvatar, true)
    }

}
