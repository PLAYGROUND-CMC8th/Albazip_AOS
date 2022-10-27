package com.playground.albazip.src.update.runtime

import android.os.Bundle
import android.view.View
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityRunningTimeBinding
import com.playground.albazip.src.update.runtime.adater.RunningTimeAdapter
import com.playground.albazip.src.update.runtime.custom.Confirm24HourBottomSheetDialog
import com.playground.albazip.src.update.runtime.custom.RunningTimePickerBottomSheetDialog
import com.playground.albazip.src.update.runtime.data.RunningTimeData
import com.playground.albazip.util.GetTimeDiffUtil

class UpdateRunningTimeActivity :
    BaseActivity<ActivityRunningTimeBinding>(ActivityRunningTimeBinding::inflate),
RunningTimePickerBottomSheetDialog.BottomSheetClickListener {

    private lateinit var runningTimeAdapter: RunningTimeAdapter

    var selectedPosition = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAdapter()
    }

    private fun initAdapter() {
        runningTimeAdapter = RunningTimeAdapter()
        runningTimeAdapter.runningTimeItemList.addAll(
            listOf(
                RunningTimeData("월"),
                RunningTimeData("화"),
                RunningTimeData("수"),
                RunningTimeData("목"),
                RunningTimeData("금"),
                RunningTimeData("토"),
                RunningTimeData("일"),
            )
        )
        binding.rvRunningTimeDays.itemAnimator = null
        binding.rvRunningTimeDays.adapter = runningTimeAdapter

        setRvItemClickEvent()
    }

    private fun setRvItemClickEvent() {
        runningTimeAdapter.setItemClickListener(object : RunningTimeAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int, tag: String) {

                selectedPosition = position // 어떤 아이템이 선택되었는지 포시션 받기

                when (tag) {
                    "SET_OPEN_HOUR" -> RunningTimePickerBottomSheetDialog(0).show(supportFragmentManager,"SET_OPEN_HOUR")
                    "SET_CLOSE_HOUR" -> RunningTimePickerBottomSheetDialog(1).show(supportFragmentManager,"SET_CLOSE_HOUR")
                }

            }
        })
    }

    // 오픈. 마감 시간 설정
    override fun onTimeSelected(h: String, m: String, flag: Int) {

        val displayTime = GetTimeDiffUtil().getDisplayTime(h, m)

        if (flag == 0) { // 오픈시간 설정
            runningTimeAdapter.setItemTimeUI(0, selectedPosition, displayTime)

            // 마감시간이 활성화 되어 있을 때 -> 24시간 여부 체크
            if (runningTimeAdapter.runningTimeItemList[selectedPosition].closeFlag) {
                areYou24Hour(0)
            }

        } else { // 마감시간 설정
            runningTimeAdapter.setItemTimeUI(1, selectedPosition, displayTime)

            // 오픈시간이 활성화 되어 있을 때 -> 24시간 여부 체크
            if (runningTimeAdapter.runningTimeItemList[selectedPosition].openFlag) {
                areYou24Hour(1)
            }
        }
    }

    // 시간이 같을 때 24시간 설정 여부 묻기
    private fun areYou24Hour(flags:Int) {
        // 시간이 같을 때 24시간 여부 묻기
        if (runningTimeAdapter.runningTimeItemList[selectedPosition].totalTime == "0시간") {
            if (flags == 0) {
                Confirm24HourBottomSheetDialog({set24Hour()},{showOpenDialogAgain()}).show(supportFragmentManager, "ARE_YOU_24")
            }else {
                Confirm24HourBottomSheetDialog({set24Hour()},{showCloseDialogAgain()}).show(supportFragmentManager, "ARE_YOU_24")
            }
        } else {
            runningTimeAdapter.runningTimeItemList[selectedPosition].time24State = false
            runningTimeAdapter.runningTimeItemList[selectedPosition].restState = false
        }
    }

    // 24시간 확인 여부를 묻는 다이얼로그 대응 함수
    private fun showOpenDialogAgain() {
        RunningTimePickerBottomSheetDialog(0).show(supportFragmentManager,"SET_OPEN_HOUR")
    }

    private fun showCloseDialogAgain() {
        RunningTimePickerBottomSheetDialog(1).show(supportFragmentManager,"SET_CLOSE_HOUR")
    }

    // 총시간 24 시간으로 설정하기
    private fun set24Hour() {
        runningTimeAdapter.init24HourUI(selectedPosition)
    }

}