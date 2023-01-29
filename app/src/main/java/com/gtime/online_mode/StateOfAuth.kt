package com.gtime.online_mode

sealed class StateOfAuth {
    object WaitingForUserAction : StateOfAuth()
    object SignInError : StateOfAuth()
    sealed class CreateError(open val email: String, open val password: String) : StateOfAuth() {
        class AlreadyExistError(override val email: String, override val password: String) :
            CreateError(email, password)

        class OtherError(override val email: String, override val password: String) :
            CreateError(email, password)
    }

    object SignInSuccess : StateOfAuth()
    object CreateSuccess : StateOfAuth()
}
