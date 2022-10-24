package com.playground.albazip.src.update.runtime.data

data class RequestMSignUp(
    val name: String,
    val type: String,
    val address: String,
    val ownerName: String,
    val registerNumber: String,
    val holiday: List<String>,
    val payDay: String,
    val openSchedule: ArrayList<OpenScheduleData>
)