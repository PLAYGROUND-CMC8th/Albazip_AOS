package com.playground.albazip.src.mypage.worker.position

import android.os.Bundle
import android.view.View
import com.playground.albazip.R
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.ChildFragmentPosInfoBinding
import com.playground.albazip.src.mypage.worker.init.data.PositionInfo
import java.text.DecimalFormat

// 근무자 > 포지션 탭
class PosInfoChildFragment(val positionInfo:PositionInfo,val intentPosition:String) : BaseFragment<ChildFragmentPosInfoBinding>(
    ChildFragmentPosInfoBinding::bind,
    R.layout.child_fragment_pos_info
) {

    // 서버에서 정보 받아오기
    val getPositionInfo = positionInfo
    val getIntentPositionTxt = intentPosition

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // 상단 텍스트 포지션 정보 받아오기
        binding.tvPosition.text = getIntentPositionTxt

        // 월급 타입
        var salaryType = ""
        when(positionInfo.salaryType){
            0 -> { salaryType = "시급"}
            1 -> {salaryType = "주급"}
            2 -> {salaryType = "월급"}
        }

        // 총 근무시간
        var workTime = ""
        if(getPositionInfo.workTime.substring(0,1) == "0"){ // 0100, 0130
            if(getPositionInfo.workTime.substring(2,4) == "00"){ // 1시간
                workTime = getPositionInfo.workTime.substring(1,2) + "시간)"
            }else{ // 1시간 30분
                workTime = getPositionInfo.workTime.substring(1,2) + "시간 " + getPositionInfo.workTime.substring(3,4)+"분)"
            }
        }else{ // 1030, 1000
            if(getPositionInfo.workTime.substring(2,4) == "00"){ // 10시간
                workTime = getPositionInfo.workTime.substring(0,2) + "시간)"
            }else{ // 10시간 30분
                workTime = getPositionInfo.workTime.substring(0,2) + "시간 " + getPositionInfo.workTime.substring(3,4)+"분)"
            }
        }

        // 급여 단위 표시
        //DecimalFormat 객체 선언 실시 (소수점 표시 안함)
        val t_dec_up = DecimalFormat("#,###")
        var salary = t_dec_up.format(getPositionInfo.salary)


        // 근무시간
        binding.tvWorkTime.text = getPositionInfo.startTime.substring(0,2) + ":" + getPositionInfo.startTime.substring(2,4) + " ~ " + getPositionInfo.endTime.substring(0,2) +
                ":"+getPositionInfo.endTime.substring(2,4) + " 까지 (총 " + workTime

        // 휴게시간
        binding.tvRestTime.text = "휴게시간 " + getPositionInfo.breakTime

        // 근무요일
        binding.tvWorkingDay.text = getPositionInfo.workDay

        // 급여
        binding.tvSalary.text = salaryType + " " + salary + "원"

    }
}