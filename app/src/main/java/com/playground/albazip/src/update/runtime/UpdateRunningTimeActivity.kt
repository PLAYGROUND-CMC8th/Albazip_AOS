package com.playground.albazip.src.update.runtime

import android.os.Bundle
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityRunningTimeBinding
import com.playground.albazip.src.update.runtime.adater.RunningTimeAdapter
import com.playground.albazip.src.update.runtime.data.RunningTimeData

class UpdateRunningTimeActivity :
    BaseActivity<ActivityRunningTimeBinding>(ActivityRunningTimeBinding::inflate) {

    private lateinit var runningTimeAdapter: RunningTimeAdapter

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
    }

}