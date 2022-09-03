package com.playground.albazip.src.register.manager.moreinfo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.SimpleItemAnimator
import com.playground.albazip.R
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityRunningTimeBinding
import com.playground.albazip.src.register.manager.moreinfo.adater.RunningTimeAdapter
import com.playground.albazip.src.register.manager.moreinfo.custom.AllTimeBottomSheetDialog
import com.playground.albazip.src.register.manager.moreinfo.custom.RunningTimePickerBottomSheetDialog
import com.playground.albazip.src.register.manager.moreinfo.data.TimeData

class RunningTimeActivity :
    BaseActivity<ActivityRunningTimeBinding>(ActivityRunningTimeBinding::inflate),
    AllTimeBottomSheetDialog.BottomSheetClickListener,
    RunningTimePickerBottomSheetDialog.BottomSheetClickListener {

    private val dayList = mutableListOf<TimeData>()
    private lateinit var runningTimeAdapter:RunningTimeAdapter

    // 마지막으로 선택한 요일의 포지션
    private var currentPosition = 0

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

        runningTimeAdapter = RunningTimeAdapter { checkDoneTxtState() }

        setDayList() // 요일 리스트 생성
        runningTimeAdapter.submitList(dayList)

        // 개별 아이템 이벤트 생성
        runningTimeAdapter.setItemClickListener(object : RunningTimeAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {

                when (v.id) {
                    // 오픈 시간을 클릭 했을 때
                    R.id.cl_open -> RunningTimePickerBottomSheetDialog(0).show(
                        supportFragmentManager,
                        "openTimePicker"
                    )

                    // 마감 시간을 클릭 했을 때
                    R.id.cl_close -> RunningTimePickerBottomSheetDialog(1).show(
                        supportFragmentManager,
                        "closeTimePicker"
                    )
                }

                currentPosition = position
            }
        })

        binding.rvRunningTimeDays.itemAnimator = null
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
            } else { // 체크박스 view 재생성
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

        } else { // 24시간이 아닐 때
            // text 넣기
            for (i in 0..6) {
                dayList[i].allDayState = false
                dayList[i].restState = false
                dayList[i].textActivate = true
            }
        }

        for (i in 0..6) {
            dayList[i].inputState = true
            /** 동일한 영업시간일 때 모든 입력 true*/
        }
        // 완료 버튼 활성화 여부 체크 - 전체 입력 (1)
        checkDoneTxtState()

        // 체크박스 체크해주고
        binding.cbRunningTimeCheckbox.isChecked = true
        // view 는 숨긴다
        setCheckViewVisibility()

        runningTimeAdapter.submitList(null)
        runningTimeAdapter.submitList(dayList)
    }

    private fun setCheckViewVisibility() {
        if (binding.cbRunningTimeCheckbox.isChecked) {
            binding.viewRunningCheck.visibility = View.GONE
        } else {
            binding.viewRunningCheck.visibility = View.VISIBLE
            binding.cbRunningTimeCheckbox.isChecked = false
        }
    }

    // 텍스트 설정
    override fun onTimeSelected(h: String, m: String, flag: Int) {

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


        if (flag == 0) { // 오픈시간
            dayList[currentPosition].openTimeTxt = "$displayHour:$displayMinute"
        } else { // 마감시간
            dayList[currentPosition].closeTimeTxt = "$displayHour:$displayMinute"
        }

        /** 동일한 영업시간일 때 모든 입력 true*/
        for (i in 0..6) {
            dayList[i].inputState = true
        }
        checkDoneTxtState()

        runningTimeAdapter.submitList(null)
        runningTimeAdapter.submitList(dayList)
    }

    // 완료 여부 체크해주는 함수
    private fun checkDoneTxtState() {
        val stateList = mutableListOf<Boolean>()
        for (i in 0..6) {
            stateList.add(runningTimeAdapter.currentList[i].inputState)
        }

        val filteredList = stateList.filter { !it }

        // 리스트가 비어있다면 버튼 활성화 else 비활성화
        binding.tvRunningTimeDone.isEnabled = filteredList.isEmpty()
    }

}