package com.playground.albazip.src.update.setworker

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.playground.albazip.R
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityUpdateSetWorkerTimeBinding
import com.playground.albazip.src.update.setworker.adapter.WorkerTimeAdapter
import com.playground.albazip.src.update.setworker.custom.SetAllWorkTimePickerBottomSheetDialog
import com.playground.albazip.src.update.setworker.custom.SetWorkerTimePickerBottomSheetDialog
import com.playground.albazip.src.update.setworker.custom.WorkTimeCancelBottomSheetDialog
import com.playground.albazip.src.update.setworker.data.WorkerTimeData

class UpdateSetWorkerTimeActivity :
    BaseActivity<ActivityUpdateSetWorkerTimeBinding>(ActivityUpdateSetWorkerTimeBinding::inflate),
    SetWorkerTimePickerBottomSheetDialog.BottomSheetClickListener,
    SetAllWorkTimePickerBottomSheetDialog.BottomSheetClickListener {

    private lateinit var workerTimeAdapter: WorkerTimeAdapter
    var workerTimeList = mutableListOf<WorkerTimeData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initRvAdapter()
        initBackEvent()

        setEachWorkerTime()

        initAllTimeBtnEvent()
    }

    // Adapter 초기화 (요일 입력)
    private fun initRvAdapter() {

        workerTimeList.addAll(
            listOf(
                WorkerTimeData("월요일"),
                WorkerTimeData("화요일"),
                WorkerTimeData("수요일"),
                WorkerTimeData("목요일"),
                WorkerTimeData("금요일"),
                WorkerTimeData("토요일"),
                WorkerTimeData("일요일")
            )
        )

        workerTimeAdapter = WorkerTimeAdapter(workerTimeList, this)
        binding.rvWorkerTime.adapter = workerTimeAdapter

        binding.rvWorkerTime.itemAnimator = null // 리사이클러뷰 애니메이션 없애기
    }


    /** 뒤로가기 버튼 클릭
     * 만약에 리스트들이 모두 비어있으면 경고 바텀시트를 띄워준다.*/
    private fun initBackEvent() {
        binding.ivRunningTimeBackBtn.setOnClickListener {

            if (workerTimeAdapter.checkIfListNull()) { // 체크된게 없으면 리스트 띄우기
                WorkTimeCancelBottomSheetDialog { finishEvent() }.show(
                    supportFragmentManager,
                    "set_worker_time_cancel"
                )
            }
        }
    }

    private fun finishEvent() {
        finish()
    }

    /** 개별 시간 선택 이벤트
     *  */
    private fun setEachWorkerTime() {
        workerTimeAdapter.setOnWorkerTimeItemClickListener(object :
            WorkerTimeAdapter.OnWorkerTimeItemClickListener {
            override fun onWorkerTimeItemClick(view: View, position: Int, timeFlags: Int) {
                if (timeFlags == 0) { // 오픈 시간 선택
                    SetWorkerTimePickerBottomSheetDialog(0, position).show(
                        supportFragmentManager,
                        "set_open_hour"
                    )
                } else { // 마감 시간 선택
                    SetWorkerTimePickerBottomSheetDialog(1, position).show(
                        supportFragmentManager,
                        "set_open_hour"
                    )
                }
            }
        })
    }

    override fun onTimeSelected(h: String, m: String, flag: Int, position: Int) {

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

        workerTimeAdapter.setLayoutAfterTimeSelected(displayTime, flag, position)
    }

    /** 근무시간 일괄적용 클릭 이벤트
     * 체크된 요일이 하나도 없으면 "시간을 설정할 근무일을 선택해주세요." 토스트 메세지
     * 정상이벤트면 바텀시트 띄우기*/
    private fun initAllTimeBtnEvent() {
        binding.ivCheckboxRunningTimeCheckbox.setOnClickListener {
            if (workerTimeAdapter.checkIfListNull()) {
                showCustomToast("시간을 설정할 근무일을 선택해주세요.")
            } else {
                SetAllWorkTimePickerBottomSheetDialog().show(
                    supportFragmentManager,
                    "all_time_event"
                )
            }
        }
    }

    override fun onTimeAllTimeSelected(
        allOpenHour: String,
        allCloseHour: String,
        allTotalTime: String,
        checkBoxState: Boolean
    ) {
        // 일괄 적용이 완료된 이벤트면
        if (checkBoxState) { // 체크된 요소들에게 데이터 넣어주기
            workerTimeAdapter.setSelectedList(allOpenHour, allCloseHour, allTotalTime)
        }

        // 체크 뷰 켜놓기
        Glide.with(binding.root).load(R.drawable.ic_circle_check_active)
            .into(binding.ivCheckboxRunningTimeCheckbox)

    }

}