package com.playground.albazip.util

import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class GetTimeDiffUtil() {

    // 시간 차 계산을 위한 데이터 포맷 선언
    val f: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREA)

    // 보여줄 오픈 시간 , 마감시간 설정
    fun getDisplayTime(h:String, m:String):String {
        var displayHour = "00"
        var displayMinute = "00"

        // ui에 보여지는 시간과 분
        if (h.length == 1) { // 한자리 숫자일 때는 앞에 "0"을 붙여준다.
            displayHour = "0$h"
        } else {
            displayHour = h
        }

        if (m.length == 1) {
            displayMinute = "0$m"
        } else {
            displayMinute = m
        }

        var displayTime = "$displayHour:$displayMinute"
        return displayTime
    }

    fun getTimeDiffTxt(openTimeTxt: String, closeTimeTxt: String): String {

        var totalTimeTxt = ""

        val sDate = f.parse("$openTimeTxt:00")
        val eDate = f.parse("$closeTimeTxt:00")
        val diff = eDate.time - sDate.time

        val sec = diff / 1000 // 총 시간(초) 받아오기


        if (eDate.time >= sDate.time) {

            val showHour = sec / (60 * 60)
            val showMin = sec / 60 - (showHour * 60)

            totalTimeTxt = if (showMin == 0L) {
                showHour.toString() + "시간"
            } else {
                showHour.toString() + "시간 " + showMin.toString() + "분"
            }


        } else { // 24시간을 더해서 빼준다.

            var sec = diff / 1000 + 86400// 총 시간(초) 받아오기(24시간 더해서)

            var showHour = sec / (60 * 60)
            var showMin = sec / 60 - (showHour * 60)

            totalTimeTxt = if (showMin == 0L) {
                showHour.toString() + "시간"
            } else {
                showHour.toString() + "시간 " + showMin.toString() + "분"
            }
        }

        return totalTimeTxt
    }


    fun getTimeDiff(openTime: TextView, closeTime: TextView, totalTime: TextView) {
        val sDate = f.parse(openTime.text.toString() + ":00")
        val eDate = f.parse(closeTime.text.toString() + ":00")
        val diff = eDate.time - sDate.time

        val sec = diff / 1000 // 총 시간(초) 받아오기


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