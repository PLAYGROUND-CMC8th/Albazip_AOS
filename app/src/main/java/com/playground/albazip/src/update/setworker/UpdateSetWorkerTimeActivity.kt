package com.playground.albazip.src.update.setworker

import android.os.Bundle
import android.util.Log
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityUpdateSetWorkerTimeBinding
import com.playground.albazip.src.update.setworker.adapter.WorkerTimeAdapter
import com.playground.albazip.src.update.setworker.custom.WorkTimeCancelBottomSheetDialog
import com.playground.albazip.src.update.setworker.data.WorkerTimeData
import okhttp3.internal.notify

class UpdateSetWorkerTimeActivity :
    BaseActivity<ActivityUpdateSetWorkerTimeBinding>(ActivityUpdateSetWorkerTimeBinding::inflate) {

    private lateinit var workerTimeAdapter: WorkerTimeAdapter
    var workerTimeList = mutableListOf<WorkerTimeData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initRvAdapter()
        initBackEvent()
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

        workerTimeAdapter = WorkerTimeAdapter(workerTimeList)
        binding.rvWorkerTime.adapter = workerTimeAdapter
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

}