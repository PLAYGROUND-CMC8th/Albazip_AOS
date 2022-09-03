package com.playground.albazip.src.register.manager.moreinfo

import android.os.Bundle
import android.view.View
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityRunningTimeBinding
import com.playground.albazip.src.register.manager.moreinfo.adater.RunningTimeAdapter
import com.playground.albazip.src.register.manager.moreinfo.custom.AllTimeBottomSheetDialog
import com.playground.albazip.src.register.manager.moreinfo.data.TimeData

class RunningTimeActivity :
    BaseActivity<ActivityRunningTimeBinding>(ActivityRunningTimeBinding::inflate),
    AllTimeBottomSheetDialog.BottomSheetClickListener {

    private val dayList = mutableListOf<TimeData>()
    private val runningTimeAdapter = RunningTimeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBackBtn()
        initAdapter()
        initAllTimeCheck()
    }

    // 뒤로가기 버튼 초기화
    private fun initBackBtn() {
        binding.ivRunningTimeBackBtn.setOnClickListener {
            finish()
        }
    }

    // 요일별 RV 초기화
    private fun initAdapter() {

        setDayList() // 요일 리스트 생성
        runningTimeAdapter.submitList(dayList)

        binding.rvRunningTimeDays.adapter = runningTimeAdapter

    }

    // 요일 리스트 생성
    private fun setDayList() {
        //val dayList = listOf(TimeData("월요일",0,0,"00:00","00:00","0시간"))
        dayList.add(0, TimeData("월요일"))
        dayList.add(1, TimeData("화요일"))
        dayList.add(2, TimeData("수요일"))
        dayList.add(3, TimeData("목요일"))
        dayList.add(4, TimeData("금요일"))
        dayList.add(5, TimeData("토요일"))
        dayList.add(6, TimeData("일요일"))
    }

    // 모든 영업시간 동일 함수
    private fun initAllTimeCheck() {

        setCheckViewVisibility()
        binding.viewRunningCheck.setOnClickListener {
            AllTimeBottomSheetDialog().show(supportFragmentManager, "allTimePicker")
        }

        binding.cbRunningTimeCheckbox.setOnCheckedChangeListener { it, isChecked ->
            if (it.isChecked) {
               // AllTimeBottomSheetDialog().show(supportFragmentManager, "allTimePicker")
            } else{ // 체크박스 view 재생성
                setCheckViewVisibility()
            }
        }
    }

    // 모든 매장시간 동일하게 설정하기 (리사이클러뷰 모든 값 업데이트)
    override fun onTimeAllTimeSelected(
        h: String,
        m: String,
        totalTime: String,
        checkBoxState: Boolean
    ) {
        for (i in 0..6) {
            dayList[i].openTimeTxt = h
            dayList[i].closeTimeTxt = m
            dayList[i].totalTimeTxt = totalTime
        }

        if (totalTime == "24시간") {

            // 모든 rv item 24시간 체크해주기
            for (i in 0..6) {
                dayList[i].allDayState = true
                dayList[i].textActivate = false
                dayList[i].restState = false
            }

            // 완료 버튼 활성화
            binding.tvRunningTimeDone.isEnabled = true
        } else { // 24시간이 아닐 때
            // text 넣기
            for (i in 0..6) {
                dayList[i].allDayState = false
                dayList[i].restState = false
                dayList[i].textActivate = true
            }
            // 완료 버튼 비활성화
            binding.tvRunningTimeDone.isEnabled = false
        }

        // 체크박스 체크해주고
        binding.cbRunningTimeCheckbox.isChecked = true
        // view 는 숨긴다
        setCheckViewVisibility()

        runningTimeAdapter.submitList(null)
        runningTimeAdapter.submitList(dayList)

    }

    private fun setCheckViewVisibility(){
        if (binding.cbRunningTimeCheckbox.isChecked) {
            binding.viewRunningCheck.visibility = View.GONE
        } else {
            binding.viewRunningCheck.visibility = View.VISIBLE
            binding.cbRunningTimeCheckbox.isChecked = false
        }
    }

}