package com.playground.albazip.src.update.setworker.data

data class WorkerTimeData(
    val workDay: String, var isSelected: Boolean ?= false,
    var openTime: String ?= "00:00", var closeTime: String ?= "00:00",
    var totalTime: String ?= "0시간"
)