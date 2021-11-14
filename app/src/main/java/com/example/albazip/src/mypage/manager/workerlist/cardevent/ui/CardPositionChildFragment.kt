package com.example.albazip.src.mypage.manager.workerlist.cardevent.ui

import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentCardPositionInfoBinding
import com.example.albazip.src.mypage.manager.workerlist.cardevent.data.WorkerInfo
import com.example.albazip.src.mypage.worker.init.data.PositionInfo

class CardPositionChildFragment(positionInfo: PositionInfo): BaseFragment<ChildFragmentCardPositionInfoBinding>(ChildFragmentCardPositionInfoBinding::bind,
    R.layout.child_fragment_card_position_info) {

    private val getPositionInfo = positionInfo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 근무시간 여쭤봐야겠다. ㅎ 월급도 ! ^^

        // 평일마감 데이터 받아오기

        // 근무시간
        binding.tvWorkTime.text = getPositionInfo.startTime.substring(0,2) + ":" + getPositionInfo.startTime.substring(2,4) + " ~ " + getPositionInfo.endTime.substring(0,2) +
               ":"+ getPositionInfo.endTime.substring(2,4) + " 까지 (총" + getPositionInfo.workTime + " 시간)"

        // 휴게시간
        binding.tvRestTime.text = "휴게시간 " + getPositionInfo.breakTime

        // 근무요일
        binding.tvWorkingDay.text = getPositionInfo.workDay

        // 급여
        binding.tvSalary.text = getPositionInfo.salaryType.toString() + " " + getPositionInfo.salary + "원"
        //"startTime": "0800",
        //"endTime": "1700",
        //"workTime": "0800",
        //"breakTime": "0100",
        //"workDay": "화 금",
        //"salaryType": 2,
        //"salary": "2000000"
    }
}