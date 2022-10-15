package com.playground.albazip.src.update.runtime.data

import java.io.Serializable

data class OpenScheduleData(
    val startTime: String,
    val EndTime: String, val day: String
):Serializable