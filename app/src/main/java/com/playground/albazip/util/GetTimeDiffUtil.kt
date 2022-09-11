package com.playground.albazip.util

import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class GetTimeDiffUtil() {

    // 시간 차 계산을 위한 데이터 포맷 선언
    val f: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREA)

    fun getTimeDiff(openTime:TextView, closeTime:TextView, totalTime:TextView) {
        var sDate = f.parse(openTime.text.toString() + ":00")
        var eDate = f.parse(closeTime.text.toString() + ":00")
        var diff = eDate.time - sDate.time

        var sec = diff / 1000 // 총 시간(초) 받아오기


        if (eDate.time >= sDate.time) {

            var showHour = sec / (60 * 60)
            var showMin = sec / 60 - (showHour * 60)

            if (showMin == 0L) {
                totalTime.text = showHour.toString() + "시간"
            } else {
                totalTime.text = showHour.toString() + "시간 " + showMin.toString() + "분"
            }


        } else { // 24시간을 더해서 빼준다.

            var sec = diff / 1000 + 86400// 총 시간(초) 받아오기(24시간 더해서)

            var showHour = sec / (60 * 60)
            var showMin = sec / 60 - (showHour * 60)

            if (showMin == 0L) {
                totalTime.text = showHour.toString() + "시간"
            } else {
                totalTime.text = showHour.toString() + "시간 " + showMin.toString() + "분"
            }
        }

    }
}