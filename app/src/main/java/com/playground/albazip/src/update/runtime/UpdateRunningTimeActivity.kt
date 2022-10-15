package com.playground.albazip.src.update.runtime

import RunTimeCancelBottomSheetDialog
import android.os.Bundle
import android.view.View
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityRunningTimeBinding
import com.playground.albazip.src.register.manager.moreinfo.custom.AllTimeBottomSheetDialog
import com.playground.albazip.src.update.runtime.adater.RunningTimeAdapter
import com.playground.albazip.src.update.runtime.custom.Confirm24HourBottomSheetDialog
import com.playground.albazip.src.update.runtime.custom.RunningTimePickerBottomSheetDialog
import com.playground.albazip.src.update.runtime.data.RunningTimeData
import com.playground.albazip.util.GetTimeDiffUtil

class UpdateRunningTimeActivity :
    BaseActivity<ActivityRunningTimeBinding>(ActivityRunningTimeBinding::inflate),
    RunningTimePickerBottomSheetDialog.BottomSheetClickListener,
    AllTimeBottomSheetDialog.BottomSheetClickListener {

    private lateinit var runTimeAdapter: RunningTimeAdapter
    private var selectedItemPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAllSettingEvent()
        initAdapter()
        initBackEvent()
    }

    // 뒤로가기 이벤트
    private fun initBackEvent() {
        binding.ivRunningTimeBackBtn.setOnClickListener {
            RunTimeCancelBottomSheetDialog().show(supportFragmentManager,"go_back")
        }
    }

    // 일괄 적용 이벤트
    private fun initAllSettingEvent() {
        binding.apply {
            viewRunningCheck.setOnClickListener { // 막는 뷰를 눌렀을 때
                // 바텀시트 띄우기
                AllTimeBottomSheetDialog().show(supportFragmentManager, "allTimePicker")
            }

            cbRunningTimeCheckbox.setOnClickListener{ // 껐을 때
                // 막는 뷰 생성
                viewRunningCheck.visibility = View.VISIBLE
            }
        }
    }

    // 어댑터 초기화
    private fun initAdapter() {
        runTimeAdapter = RunningTimeAdapter{setAllCheckBtnOff()}
        runTimeAdapter.runningTimeItemList.addAll(
            listOf(
                RunningTimeData("월"), RunningTimeData("화"),
                RunningTimeData("수"), RunningTimeData("목"),
                RunningTimeData("금"), RunningTimeData("토"),
                RunningTimeData("일")
            )
        )

        // 아이템 클릭 이벤트
        runTimeAdapter.setItemClickListener(object : RunningTimeAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int, flags: Int) {
                if (flags == 0) { // 오픈 시간 바텀 시트 불러오기
                    RunningTimePickerBottomSheetDialog(flags).show(
                        supportFragmentManager,
                        "set_open_hour"
                    )
                } else { // 마감 시간 바텀 시트 불러오기
                    RunningTimePickerBottomSheetDialog(flags).show(
                        supportFragmentManager,
                        "set_close_hour"
                    )
                }
                selectedItemPosition = position
            }
        })

        binding.rvRunningTimeDays.adapter = runTimeAdapter
    }

    /** 여기서 선택한 시간을 받아와서 adapter 와 연결해준다. */
    override fun onTimeSelected(h: String, m: String, flag: Int) {

        var displayHour = "00"
        var displayMinute = "00"

        fun getTransTime(): String {
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

            return "$displayHour:$displayMinute"
        }

        if (flag == 0) { // 오픈시간 텍스트 설정
            runTimeAdapter.runningTimeItemList[selectedItemPosition].apply {

                // 마감타임이 00:00이 아닐 때
                // getTransTime() == 마감타임이면
                // 24시간 체크 여부를 묻는 다이얼로그를 띄운다.
                if (closeTime != "00:00") {
                    if (getTransTime() == closeTime) {
                        val check24ConfirmDialog = Confirm24HourBottomSheetDialog(0,
                            { doAfterConfirm() },
                            { doAfterCancelOpen() },
                            { doAfterCancelClose() })

                        check24ConfirmDialog.isCancelable = false
                        check24ConfirmDialog.show(
                            supportFragmentManager,
                            "check_open_24_confirm"
                        )
                    }
                }

                openTime = getTransTime()
                openInputFlag = true

                // 수동으로 "00:00"을 택하여 24시간을 설정했을때 (단, 마감역시 활성화 된 상태여야함)
                if (closeInputFlag) {
                    if (openTime == "00:00" && closeTime == "00:00") {
                        runTimeAdapter.set24Hour(selectedItemPosition)
                    } else {
                        runTimeAdapter.runningTimeItemList[selectedItemPosition].totalTime =
                            GetTimeDiffUtil().getTimeDiffTxt(getTransTime(), closeTime!!)
                    }
                }
            }
        } else { // 마감시간 텍스트 설정
            runTimeAdapter.runningTimeItemList[selectedItemPosition].apply {

                if (openTime != "00:00") {
                    if (getTransTime() == openTime) {
                        val check24ConfirmDialog = Confirm24HourBottomSheetDialog(1,
                            { doAfterConfirm() },
                            { doAfterCancelOpen() },
                            { doAfterCancelClose() })

                        check24ConfirmDialog.isCancelable = false
                        check24ConfirmDialog.show(
                            supportFragmentManager,
                            "check_close_24_confirm"
                        )
                    }
                }

                closeTime = getTransTime()
                closeInputFlag = true

                // 수동으로 "00:00"을 택하여 24시간을 설정했을때 (단, 오픈 역시 활성화된 상태여야 함)
                if (openInputFlag) {
                    if (openTime == "00:00" && closeTime == "00:00") {
                        runTimeAdapter.set24Hour(selectedItemPosition)
                    } else {
                        runTimeAdapter.runningTimeItemList[selectedItemPosition].totalTime =
                            GetTimeDiffUtil().getTimeDiffTxt(openTime!!, getTransTime())
                    }
                }
            }

        }

        setAllCheckBtnOff()
        runTimeAdapter.notifyItemChanged(selectedItemPosition)
    }

    // 24시간으로 설정하기
    private fun doAfterConfirm() {
        runTimeAdapter.set24Hour(selectedItemPosition)
        runTimeAdapter.notifyItemChanged(selectedItemPosition)
    }

    // 오픈 시간 재설정 -> 바텀시트 다시 띄우기
    private fun doAfterCancelOpen() {
        RunningTimePickerBottomSheetDialog(0).show(
            supportFragmentManager,
            "set_open_hour"
        )
        showCustomToast("마감 시간과 같아요. 시간을 다시 설정해주세요.")
    }

    // 마감 시간 재설정 -> 바텀시트 다시 띄우기
    private fun doAfterCancelClose() {
        RunningTimePickerBottomSheetDialog(1).show(
            supportFragmentManager,
            "set_close_hour"
        )

        showCustomToast("오픈 시간과 같아요. 시간을 다시 설정해주세요.")
    }

    // 전체 설정 뷰 바텀 클릭 이벤트
    override fun onTimeAllTimeSelected(
        oTime: String,
        cTime: String,
        tTime: String,
    ) {
        if (tTime == "24시간") {
            for (i in runTimeAdapter.runningTimeItemList.indices) {
                runTimeAdapter.set24Hour(i)
                runTimeAdapter.runningTimeItemList[i].time24State = true
                runTimeAdapter.runningTimeItemList[i].restState = false
            }
        } else {
            for (i in runTimeAdapter.runningTimeItemList.indices) {
                runTimeAdapter.runningTimeItemList[i].apply {
                    openTime = oTime
                    closeTime = cTime
                    totalTime = tTime

                    openInputFlag = true
                    openInputFlag = true

                    time24State = false
                    restState = false
                }
            }
        }

        binding.viewRunningCheck.visibility = View.GONE
        binding.cbRunningTimeCheckbox.isSelected = true

        runTimeAdapter.notifyDataSetChanged()
    }

    fun setAllCheckBtnOff() {
        binding.viewRunningCheck.visibility = View.VISIBLE
        binding.cbRunningTimeCheckbox.isSelected = false
    }
}