package com.playground.albazip.src.register.manager.moreinfo.data

data class TimeData(
    val daysTxt: String,
    val restState: Boolean = false,
    val allDayState: Boolean = false,
    val openTimeTxt: String? = "00:00",
    val closeTimeTxt: String? = "00:00",
    val totalTimeTxt: String? = "0시간"
)

// 요일
// 휴무일 체크 여부
// 24시간 체크 여부
// 오픈시간
// 마감시간
// 총시간