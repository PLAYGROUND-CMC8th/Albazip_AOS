package com.playground.albazip.src.mypage.worker.position

import android.os.Bundle
import android.view.View
import com.playground.albazip.R
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.ChildFragmentPosInfoBinding
import com.playground.albazip.src.mypage.worker.init.data.PositionInfo
import com.playground.albazip.src.mypage.worker.position.adapter.WorkScheduleAdapter
import java.text.DecimalFormat

// 근무자 > 포지션 탭
class PosInfoChildFragment(val positionInfo: PositionInfo, private val intentPosition: String) :
    BaseFragment<ChildFragmentPosInfoBinding>(
        ChildFragmentPosInfoBinding::bind,
        R.layout.child_fragment_pos_info
    ) {

    private lateinit var workScheduleAdapter: WorkScheduleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setWorkingDayUI()
        setRestTimeUI()
        setSalaryUI()
    }

    // 근무일
    private fun setWorkingDayUI() {
        workScheduleAdapter = WorkScheduleAdapter()
        workScheduleAdapter.scheduleList = positionInfo.workSchedule
        binding.rvWorkSchedule.adapter = workScheduleAdapter
    }

    // 쉬는시간
    private fun setRestTimeUI() {
        binding.tvRestDay.text = positionInfo.breakTime
    }

    // 페이
    private fun setSalaryUI() {
        var salaryType = ""
        when (positionInfo.salaryType) {
            0 -> {
                salaryType = "시급"
            }
            1 -> {
                salaryType = "주급"
            }
            2 -> {
                salaryType = "월급"
            }
        }

        binding.tvSalary.text =
            salaryType + DecimalFormat("#,###").format(positionInfo.salary.toInt()) + "원"
    }
}