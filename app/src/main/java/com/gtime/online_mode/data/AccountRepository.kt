package com.gtime.online_mode.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gtime.general.Constants
import com.gtime.general.scopes.AppScope
import com.gtime.online_mode.data.model.AccountInfoModel
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken
import io.github.nefilim.kjwt.JWT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AppScope
class AccountRepository @Inject constructor(
    private val yandexAuthSdk: YandexAuthSdk,
    private val coinsRepository: CoinsRepository,
    private val taskRepository: TaskRepository,
) {
    val accountInfoModel = MutableLiveData<AccountInfoModel>()

    init {
        val user = Firebase.auth.currentUser
        accountInfoModel.value =
            AccountInfoModel(
                user?.displayName ?: "",
                user?.email ?: "",
                user?.photoUrl.toString(),
                Firebase.auth.currentUser != null
            )
    }

    suspend fun getDecodedJWT(yandexAuthToken: YandexAuthToken?) = withContext(Dispatchers.IO) {
        yandexAuthToken ?: return@withContext
        JWT.decode(yandexAuthSdk.getJwt(yandexAuthToken)).tap {
            val acc = AccountInfoModel(
                name = it.claimValue(Constants.DISPLAY_NAME).orNull() ?: "",
                email = it.claimValue(Constants.EMAIL).orNull() ?: "",
                urlAvatar = Constants.AVATAR_URL_START + it.claimValue(Constants.AVATAR_ID)
                    .orNull()
            )
            accountInfoModel.postValue(
                acc
            )
        }
    }

    fun isAuthorized() = accountInfoModel.value?.isFirebaseAuth ?: false
    fun getEmail() = accountInfoModel.value?.email ?: ""
    fun clearAccountInfo() {
        val emptyAcc = AccountInfoModel("", "", "")
        accountInfoModel.value = emptyAcc
    }

    suspend fun successAuthFirebase() {
        val acc = accountInfoModel.value ?: return
        accountInfoModel.value = AccountInfoModel(acc.name, acc.email, acc.urlAvatar, true)
        coinsRepository.refreshListener()
        taskRepository.getTasks()
    }

}
