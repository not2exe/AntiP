package com.gtime.online_mode.ui.stateholders

import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.gtime.online_mode.data.AccountRepository
import com.gtime.online_mode.state_classes.StateOfAuth
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class LoginViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val accountRepository: AccountRepository,
    private val auth: FirebaseAuth
) :
    ViewModel() {
    val stateOfAuth = MutableLiveData<StateOfAuth>(StateOfAuth.WaitingForUserAction)
    val accountInfoForDisplay = accountRepository.accountInfoModel.value

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): LoginViewModel
    }

    fun checkIfPasswordIsNormal(password: String) =
        password.length >= 8

    fun createAccount(email: String, password: String) = viewModelScope.launch {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    stateOfAuth.value = StateOfAuth.CreateSuccess
                } else {
                    stateOfAuth.value =
                        if (task.exception is FirebaseAuthUserCollisionException)
                            StateOfAuth.CreateError.AlreadyExistError(
                                email,
                                password
                            )
                        else
                            StateOfAuth.CreateError.OtherError(email, password)
                }

            }
    }

    fun signInAccount(email: String, password: String) = viewModelScope.launch {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            stateOfAuth.value =
                if (it.isSuccessful) StateOfAuth.SignInSuccess else StateOfAuth.SignInError
        }
    }

    fun successCreateNewAccount() = viewModelScope.launch {
        accountRepository.successAuthFirebase()
        updateProfile()
    }

    fun successSignIn() = viewModelScope.launch {
        val acc = auth.currentUser ?: return@launch
        accountRepository.successAuthFirebase()
        if (accountInfoForDisplay?.name != acc.displayName || accountInfoForDisplay?.urlAvatar != acc.photoUrl.toString()) {
            updateProfile()
        }
    }


    private fun updateProfile() = viewModelScope.launch {
        Firebase.auth.currentUser?.updateProfile(userProfileChangeRequest {
            displayName = accountInfoForDisplay?.name
            photoUri = accountInfoForDisplay?.urlAvatar?.toUri()
        })
    }

}