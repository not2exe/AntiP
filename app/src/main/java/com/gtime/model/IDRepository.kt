package com.gtime.model

import androidx.lifecycle.MutableLiveData
import com.gtime.Constants
import com.gtime.domain.AppScope
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken
import io.github.nefilim.kjwt.JWT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AppScope
class IDRepository @Inject constructor(
    private val yandexAuthSdk: YandexAuthSdk,
    private val cache: Cache
) {
    val accountInfo = MutableLiveData<AccountInfo>()

    init {
        val accountInfoInCache = cache.getAcc()
        if (accountInfoInCache.name != "") {
            accountInfo.value = accountInfoInCache
        }
    }

    suspend fun getDecodedJWT(yandexAuthToken: YandexAuthToken?) = withContext(Dispatchers.IO) {
        yandexAuthToken ?: return@withContext
        JWT.decode(yandexAuthSdk.getJwt(yandexAuthToken)).tap {
            val acc = AccountInfo(
                name = it.claimValue(Constants.DISPLAY_NAME).orNull() ?: "",
                email = it.claimValue(Constants.EMAIL).orNull() ?: "",
                urlAvatar = Constants.AVATAR_URL_START + it.claimValue(Constants.AVATAR_ID)
                    .orNull() + Constants.AVATAR_URL_END
            )
            accountInfo.postValue(
                acc
            )
            cache.saveAcc(acc)
        }
    }

    fun clearAccountInfo() {
        val emptyAcc = AccountInfo("", "", "")
        accountInfo.value = emptyAcc
        cache.saveAcc(emptyAcc)
    }

}
