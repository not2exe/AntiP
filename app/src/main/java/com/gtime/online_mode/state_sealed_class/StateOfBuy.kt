package com.gtime.online_mode.state_sealed_class

sealed class StateOfBuy {
    object WaitingForUserAction : StateOfBuy()
    object AuthError : StateOfBuy()
    object LackOfCoinsError : StateOfBuy()
    class PromoError(val cost: Int) : StateOfBuy()
    class SuccessWithCoins(val offerID: String,val cost:Int) : StateOfBuy()
    object FullSuccess : StateOfBuy()
}
