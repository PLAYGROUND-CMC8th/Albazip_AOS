package com.example.albazip.src.mypage.worker.position

import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentPosInfoBinding
import com.example.albazip.src.mypage.worker.init.data.PositionInfo

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

        // 근무시간 여쭤봐야겠다. ㅎ 월급도 ! ^^

        // 평일마감 데이터 받아오기


        // 근무시간
        binding.tvWorkTime.text = getPositionInfo.startTime.substring(0,2) + ":" + getPositionInfo.startTime.substring(2,4) + " ~ " + getPositionInfo.endTime.substring(0,2) +
                getPositionInfo.endTime.substring(2,4) + " 까지 (총" + getPositionInfo.workTime + " 시간)"

        // 휴게시간
        binding.tvRestTime.text = "휴게시간 " + getPositionInfo.breakTime

        // 근무요일
        binding.tvWorkingDay.text = getPositionInfo.workDay

        // 급여
        binding.tvSalary.text = getPositionInfo.salaryType.toString() + " " + getPositionInfo.salary + "원"

    }
}