package com.playground.albazip.src.update.runtime.data

import java.io.Serializable

data class RunningTimeData(
    var day: String?="일",

    var restState: Boolean = false,
    var time24State: Boolean = false,

    var openTime: String? = "00:00",
    var closeTime: String? = "00:00",
    var totalTime: String? = "0시간",

    var openFlag:Boolean = false,
    var closeFlag:Boolean = false,

    var allTimeFlag:Boolean = false

):Serializable