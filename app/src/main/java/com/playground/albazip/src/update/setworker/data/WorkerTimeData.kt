package com.playground.albazip.src.update.setworker.data

import java.io.Serializable

data class WorkerTimeData(
    var workDay: String,
    var isSelected: Boolean? = false,
    var openTime: String? = "00:00", var closeTime: String? = "00:00",

    var totalTime: String? = "0시간",

    var openFlag:Boolean = false,
    var closeFlag:Boolean = false
):Serializable