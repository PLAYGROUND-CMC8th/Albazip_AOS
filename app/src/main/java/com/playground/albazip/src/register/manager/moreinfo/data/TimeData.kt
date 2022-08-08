package com.playground.albazip.src.register.manager.moreinfo.data

data class TimeData(
    var daysTxt: String,
    var restState: Boolean = false,
    var allDayState: Boolean = false,
    var openTimeTxt: String? = "00:00",
    var closeTimeTxt: String? = "00:00",
    var totalTimeTxt: String? = "0시간"
)

// 요일
// 휴무일 체크 여부
// 24시간 체크 여부
// 오픈시간
// 마감시간
// 총시간