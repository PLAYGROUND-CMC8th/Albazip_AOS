package com.playground.albazip.src.home.manager.editshop.data

data class RequestEditShop(
    val address: String,
    val endTime: String?="",
    val holiday: List<String>,
    val name: String,
    val openSchedule: ArrayList<OpenSchedule>,
    val payday: String,
    val startTime: String?="",
    val type: String
) {
    data class OpenSchedule(
        val day: String,
        val startTime: String,
        val endTime: String,
    )
}