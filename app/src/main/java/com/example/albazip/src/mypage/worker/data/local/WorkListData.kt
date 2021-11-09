package com.example.albazip.src.mypage.worker.data.local

// 근무자 > 내정보 > 완료한 업무
// 근무자 > 내정보 > 완료한 업무 > 월별 업무
data class WorkListData (val monthTxt:String, val doneCnt:Int,val allCnt:Int)