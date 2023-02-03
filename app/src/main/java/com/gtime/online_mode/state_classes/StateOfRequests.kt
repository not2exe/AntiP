package com.gtime.online_mode.state_classes

sealed class StateOfRequests {
    object WaitingForUserAction : StateOfRequests()
    sealed class Error : StateOfRequests() {
        object Failure : Error()
        object AuthError : Error()
        object LackOfCoinsError : Error()
        class PromoError(val cost: Int) : Error()
    }

    sealed class Success : StateOfRequests() {
        class SuccessWithCoins(val offerID: String, val cost: Int) : Success()
        object FullSuccess : Success()
    }
}
