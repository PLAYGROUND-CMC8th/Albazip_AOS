package com.playground.albazip.src.update.runtime

import android.os.Bundle
import android.util.Log
import android.view.View
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityRunningTimeBinding
import com.playground.albazip.src.update.runtime.adater.RunningTimeAdapter
import com.playground.albazip.src.update.runtime.custom.RunningTimePickerBottomSheetDialog
import com.playground.albazip.src.update.runtime.data.RunningTimeData

class UpdateRunningTimeActivity :
    BaseActivity<ActivityRunningTimeBinding>(ActivityRunningTimeBinding::inflate),
RunningTimePickerBottomSheetDialog.BottomSheetClickListener{

    private lateinit var runTimeAdapter: RunningTimeAdapter
    private var selectedItemPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAdapter()
    }

    // 어댑터 초기화
    private fun initAdapter() {
        runTimeAdapter = RunningTimeAdapter()
        runTimeAdapter.runningTimeItemList.addAll(
            listOf(
                RunningTimeData("월"), RunningTimeData("화"),
                RunningTimeData("수"), RunningTimeData("목"),
                RunningTimeData("금"), RunningTimeData("토"),
                RunningTimeData("일")
            )
        )

        // 아이템 클릭 이벤트
        runTimeAdapter.setItemClickListener(object :RunningTimeAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int, flags: Int) {
                if (flags == 0) { // 오픈 시간 바텀 시트 불러오기
                    RunningTimePickerBottomSheetDialog (flags) .show(supportFragmentManager, "set_open_hour")
                } else { // 마감 시간 바텀 시트 불러오기
                    RunningTimePickerBottomSheetDialog (flags) .show(supportFragmentManager, "set_close_hour")
                }
                selectedItemPosition = position
            }
        })

        binding.rvRunningTimeDays.adapter = runTimeAdapter
    }

    /** 여기서 선택한 시간을 받아와서 adapter 와 연결해준다. */
    override fun onTimeSelected(h: String, m: String, flag: Int) {
        if (flag == 0) { // 오픈시간 텍스트 설정
            runTimeAdapter.runningTimeItemList[selectedItemPosition].apply {
                openTime = "뿌잉"
                openInputState = true
            }
        } else { // 마감시간 텍스트 설정
            runTimeAdapter.runningTimeItemList[selectedItemPosition].apply {
                closeTime = "아잉"
                closeInoutState = true
            }
        }

        runTimeAdapter.notifyItemChanged(selectedItemPosition)
    }
}