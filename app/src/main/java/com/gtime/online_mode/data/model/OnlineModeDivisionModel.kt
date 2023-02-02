package com.gtime.online_mode.data.model

data class OnlineModeDivisionModel(
    val toxic: ArrayList<String>,
    val useful: ArrayList<String>
) {
    constructor() : this(arrayListOf(), arrayListOf())
}