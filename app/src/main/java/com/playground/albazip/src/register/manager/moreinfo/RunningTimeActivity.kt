package com.playground.albazip.src.register.manager.moreinfo

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityRunningTimeBinding
import com.playground.albazip.src.register.manager.moreinfo.adater.RunningTimeAdapter
import com.playground.albazip.src.register.manager.moreinfo.data.TimeData

class RunningTimeActivity:BaseActivity<ActivityRunningTimeBinding>(ActivityRunningTimeBinding::inflate) {

    private val dayList = mutableListOf<TimeData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAdapter()
    }

    // 요일별 RV 초기화
    private fun initAdapter(){

        setDayList() // 요일 리스트 생성

        val runningTimeAdapter = RunningTimeAdapter()
        runningTimeAdapter.submitList(dayList)

        binding.rvRunningTimeDays.adapter = runningTimeAdapter

    }

    // 요일 리스트 생성
    private fun setDayList(){
        //val dayList = listOf(TimeData("월요일",0,0,"00:00","00:00","0시간"))
        dayList.add(0, TimeData("월요일"))
        dayList.add(1,TimeData("화요일"))
        dayList.add(2,TimeData("수요일"))
        dayList.add(3,TimeData("목요일"))
        dayList.add(4,TimeData("금요일"))
        dayList.add(5,TimeData("토요일"))
        dayList.add(6,TimeData("일요일"))
    }
}