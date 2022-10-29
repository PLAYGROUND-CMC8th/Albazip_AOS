package com.playground.albazip.src.update.runtime.network

data class RequestMSignUp(
    val address: String,
    val holiday: MutableList<String>,
    val name: String,
    val openSchedule: MutableList<OpenSchedule>,
    val ownerName: String,
    val payday: String,
    val registerNumber: String,
    val type: String
)

data class OpenSchedule(
    val day: String,
    val startTime: String,
    val endTime: String,
)