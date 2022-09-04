package com.playground.albazip.src.register.manager.moreinfo.custom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.playground.albazip.R
import com.playground.albazip.databinding.DialogFragment24HourBinding
import com.playground.albazip.util.GetTimeDiffUtil

class AllTimeBottomSheetDialog() : BottomSheetDialogFragment(),
    RunningTimePickerBottomSheetDialog.BottomSheetClickListener {

    private lateinit var binding: DialogFragment24HourBinding
    lateinit var bottomSheetClickListener: BottomSheetClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bottomSheetClickListener = context as BottomSheetClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragment24HourBinding.inflate(inflater, container, false)

        init24HourBtn() // 24시간 영업함수
        initConfirmBtn() // 확인버튼
        initTimeDialog() // 시간 설정 다이얼로그

        return binding.root
    }

    // 24시간 버튼 클릭
    private fun init24HourBtn() {

        binding.cb24Hour.setOnCheckedChangeListener { buttonView, isChecked ->
            // 다이얼로그 선택 비활성화
            if (binding.clOpen.isEnabled) {
                binding.clOpen.isEnabled = false
                binding.clClose.isEnabled = false
                binding.tvTotalTime.text = "24시간"
            } else {
                binding.clOpen.isEnabled = true
                binding.clClose.isEnabled = true
                binding.tvTotalTime.text = "0시간"
            }

            // 텍스트 설정
            binding.tvOpenHour.text = "00:00"
            binding.tvCloseHour.text = "00:00"

            // 텍스트 색상 변경
            binding.tvOpenHour.setTextColor(requireContext().getColor(R.color.gray5_e2e2e2))
            binding.tvCloseHour.setTextColor(requireContext().getColor(R.color.gray5_e2e2e2))

            // 버튼 활성화 여부 설정
            binding.btnConfirm.isEnabled = buttonView.isChecked
        }

    }

    // 시간 다이얼로그 초기화(바텀시트 열기)
    private fun initTimeDialog() {
        // 오픈 선택
        binding.clOpen.setOnClickListener {
            RunningTimePickerBottomSheetDialog(0).show(childFragmentManager, "openTimePicker")
        }

        // 마감 선택
        binding.clClose.setOnClickListener {
            RunningTimePickerBottomSheetDialog(1).show(childFragmentManager, "closeTimePicker")
        }
    }


    // 확인 버튼 클릭
    private fun initConfirmBtn() {
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
            binding.tvOpenHour.text = "$displayHour:$displayMinute"
        } else { // 마감시간
            binding.tvCloseHour.text = "$displayHour:$displayMinute"
        }

        // 시간차 텍스트 설정
        GetTimeDiffUtil().getTimeDiff(binding.tvOpenHour, binding.tvCloseHour, binding.tvTotalTime)

        if (binding.tvTotalTime.text != "0시간") {
            binding.apply { // 시간 차 o -> 텍스트 활성화
                tvOpenHour.setTextColor(requireContext().getColor(R.color.text4_343434))
                tvCloseHour.setTextColor(requireContext().getColor(R.color.text4_343434))
                btnConfirm.isEnabled = true // -> 버튼 활성화
            }
        } else {
            binding.apply { // 시간 차 x -> 텍스트 비활성화
                tvOpenHour.setTextColor(requireContext().getColor(R.color.gray5_e2e2e2))
                tvCloseHour.setTextColor(requireContext().getColor(R.color.gray5_e2e2e2))
                btnConfirm.isEnabled = false // -> 버튼 비활성화
            }
        }
    }

    interface BottomSheetClickListener {
        fun onTimeAllTimeSelected(h: String, m: String, totalTime: String, checkBoxState: Boolean)
    }
}