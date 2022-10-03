package com.playground.albazip.src.update.setworker.data

data class WorkerTimeData(
    val workDay: String, var isSelected: Boolean ?= false,
    val openTime: String ?= "00:00", val closeTime: String ?= "00:00",
    val totalTime: String ?= "0시간"
)