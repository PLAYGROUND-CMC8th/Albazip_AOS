package com.playground.albazip.src.update.setworker.custom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.playground.albazip.R
import com.playground.albazip.databinding.DialogFragment24HourBinding
import com.playground.albazip.util.GetTimeDiffUtil

class SetAllWorkTimePickerBottomSheetDialog : BottomSheetDialogFragment(),
    SetAllWorkNextTimePickerBottomSheetDialog.BottomSheetClickListener {
    private lateinit var binding: DialogFragment24HourBinding
    lateinit var bottomSheetClickListener: BottomSheetClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        bottomSheetClickListener =
            context as BottomSheetClickListener
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragment24HourBinding.inflate(inflater, container, false)

        setLayout()
        initTimeDialog()
        initConfirmEvent()

        return binding.root
    }

    private fun setLayout() {

        binding.apply {
            tv24Hour.visibility = View.GONE
            cb24Hour.visibility = View.GONE

            tv24HourTitle.text = "근무시간을 입력해주세요."
            tv24HourSubTitle.text = "기존에 입력했던 근무시간은 모두 사라져요."
        }
    }

    // 시간 다이얼로그 초기화(바텀시트 열기)
    private fun initTimeDialog() {
        // 오픈 선택
        binding.clOpen.setOnClickListener {
            SetAllWorkNextTimePickerBottomSheetDialog(0).show(
                childFragmentManager,
                "openAllTimePicker"
            )
        }

        // 마감 선택
        binding.clClose.setOnClickListener {
            SetAllWorkNextTimePickerBottomSheetDialog(1).show(
                childFragmentManager,
                "closeAllTimePicker"
            )
        }
    }

    /** 다음 바텀시트 클릭 이벤트에서 시간을 받아온 후 레이아웃을 세팅해준다.*/
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

        var displayTime = "$displayHour:$displayMinute"

        setTimeLayOut(displayTime, flag)
    }

    private fun setTimeLayOut(displayTime: String, flag: Int) {
        if (flag == 0) {
            binding.tvOpenHour.text = displayTime
        } else {
            binding.tvCloseHour.text = displayTime
        }

        // 텍스트 설정
        if (binding.tvOpenHour.text != "00:00") {
            binding.tvOpenHour.setTextColor(
                requireContext().resources.getColor(
                    R.color.text4_343434,
                    null
                )
            )
        } else {
            binding.tvOpenHour.setTextColor(
                requireContext().resources.getColor(
                    R.color.gray5_e2e2e2,
                    null
                )
            )
        }

        if (binding.tvCloseHour.text != "00:00") {
            binding.tvCloseHour.setTextColor(
                requireContext().resources.getColor(
                    R.color.text4_343434,
                    null
                )
            )
        } else {
            binding.tvCloseHour.setTextColor(
                requireContext().resources.getColor(
                    R.color.gray5_e2e2e2,
                    null
                )
            )
        }

        // totalTime 설정
        if (binding.tvOpenHour.text != "00:00" && binding.tvCloseHour.text != "00:00") {
            GetTimeDiffUtil().getTimeDiff(
                binding.tvOpenHour,
                binding.tvCloseHour,
                binding.tvTotalTime
            )

            binding.btnConfirm.isEnabled = true // 다음버튼 활성화
        } else {
            binding.btnConfirm.isEnabled = false // 다음버튼 비활성화
        }

    }

    //  확인 버튼 이벤트
    private fun initConfirmEvent() {
        binding.btnConfirm.setOnClickListener {
            val allOpenHour = binding.tvOpenHour.text.toString()
            val allCloseHour = binding.tvCloseHour.text.toString()
            val allTotalTime = binding.tvTotalTime.text.toString()
            // 데이터를 액티비티에 넘기기
            bottomSheetClickListener.onTimeAllTimeSelected(
                allOpenHour,
                allCloseHour,
                allTotalTime,
                true,
            )
            // 종료
            dismiss()
        }
    }

    // activity 에 전달된 변수들
    interface BottomSheetClickListener {
        fun onTimeAllTimeSelected(allOpenHour: String, allCloseHour: String, allTotalTime: String, checkBoxState: Boolean)
    }
}