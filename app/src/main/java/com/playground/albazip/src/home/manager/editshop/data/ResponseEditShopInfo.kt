package com.playground.albazip.src.home.manager.editshop.data

import com.playground.albazip.config.BaseResponse

data class ResponseEditShopInfo(
    val data: ResultEditShopInfoData,
) : BaseResponse() {
    data class ResultEditShopInfoData(
        val address: String,
        val endTime: String,
        val holiday: List<String>,
        val name: String,
        val openSchedule: List<OpenSchedule>,
        val payday: String,
        val startTime: String,
        val type: String
    )

    data class OpenSchedule(
        val day: String,
        val endTime: String,
        val startTime: String
    )
}