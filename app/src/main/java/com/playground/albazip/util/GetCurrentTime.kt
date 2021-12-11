package com.playground.albazip.util

import java.text.SimpleDateFormat
import java.util.*

class GetCurrentTime() {
    // 현재시간 가져오기
    var now: Long = System.currentTimeMillis()

    // 날짜 형태로 변환
    var mDate: Date = Date(now)

    // 형변환
    var simpleDate: SimpleDateFormat = SimpleDateFormat("hh:mm")
    var getTime: String = simpleDate.format(mDate)

    // qr 스캔
    var sdf1: SimpleDateFormat? = SimpleDateFormat("yy.MM.dd E요일")
    var getQRScanDate:String = sdf1!!.format(mDate)
}