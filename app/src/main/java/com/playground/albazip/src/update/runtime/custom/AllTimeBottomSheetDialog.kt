package com.playground.albazip.src.update.runtime.custom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.playground.albazip.databinding.DialogFragment24HourBinding
import com.playground.albazip.util.GetTimeDiffUtil

class AllTimeBottomSheetDialog() : BottomSheetDialogFragment(),
    RunningTimeAllTimePickerBottomSheetDialog.BottomSheetClickListener {

    private lateinit var binding: DialogFragment24HourBinding
    lateinit var bottomSheetClickListener: BottomSheetClickListener

    var openFlag = false
    var closeFlag = false


    override fun onAttach(context: Context) {
        super.onAttach(context)
        bottomSheetClickListener = context as BottomSheetClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragment24HourBinding.inflate(inflater, container, false)

        init24HourBtn() // 24시간 영업함수
        initTimeDialog() // 시간 설정 다이얼로그
        initConfirmBtn() // 확인버튼 이벤트

        return binding.root
    }


    // 시간 설정 다이얼로그
    private fun initTimeDialog() {
        // 오픈시간 설정
        binding.apply {
            clOpen.setOnClickListener {
                RunningTimeAllTimePickerBottomSheetDialog(0).show(
                    childFragmentManager,
                    "ALL_OPEN_TIME_PICKER"
                )
            }

            clClose.setOnClickListener {
                RunningTimeAllTimePickerBottomSheetDialog(1).show(
                    childFragmentManager,
                    "ALL_CLOSE_TIME_PICKER"
                )
            }
        }
    }


    interface BottomSheetClickListener {
        fun onTimeAllTimeSelected(oTime: String, eTime: String, totalTime: String)
    }

    // 개별 시간 요소 설정
    override fun onGetTimeSelected(h: String, m: String, flag: Int) {
        val displayTime = GetTimeDiffUtil().getDisplayTime(h, m)

        if (flag == 0) { // 오픈시간 설정
            binding.tvOpenHour.text = displayTime
            binding.tvOpenHour.isEnabled = true
            openFlag = true

            if (closeFlag) {
                GetTimeDiffUtil().getTimeDiff(
                    binding.tvOpenHour,
                    binding.tvCloseHour,
                    binding.tvTotalTime
                )
            }

            // 마감시간이 활성화 되어 있을 때 -> 24시간 여부 체크
            if (binding.tvCloseHour.isEnabled) {
                areYou24Hour(0)
            }

        } else { // 마감시간 설정
            binding.tvCloseHour.text = displayTime
            binding.tvCloseHour.isEnabled = true
            closeFlag = true

            if (openFlag) {
                GetTimeDiffUtil().getTimeDiff(
                    binding.tvOpenHour,
                    binding.tvCloseHour,
                    binding.tvTotalTime
                )
            }

            // 오픈시간이 활성화 되어 있을 때 -> 24시간 여부 체크
            if (binding.tvOpenHour.isEnabled) {
                areYou24Hour(1)
            }
        }

        checkBtnState()
    }

    // 시간이 같을 때 24시간 설정 여부 묻기
    private fun areYou24Hour(flags: Int) {
        // 시간이 같을 때 24시간 여부 묻기
        if (binding.tvTotalTime.text == "0시간") {
            if (flags == 0) {
                Confirm24HourBottomSheetDialog({ set24Hour() }, { showOpenDialogAgain() }).show(
                    childFragmentManager,
                    "ARE_YOU_24"
                )
            } else {
                Confirm24HourBottomSheetDialog({ set24Hour() }, { showCloseDialogAgain() }).show(
                    childFragmentManager,
                    "ARE_YOU_24"
                )
            }
        }

        checkBtnState()
    }

    // 24 시간 설정 함수
    private fun set24Hour() {
        binding.apply {
            // 시간 설정
            tvOpenHour.text = "00:00"
            tvCloseHour.text = "00:00"
            tvTotalTime.text = "24시간"

            // 배경 설정
            clOpen.isEnabled = false
            clClose.isEnabled = false

            // 텍스트 설정
            tvOpenHour.isEnabled = false
            tvCloseHour.isEnabled = false
        }

        binding.cb24Hour.isSelected = true

        checkBtnState()
    }

    // 오픈 시간 재설정 함수
    private fun showOpenDialogAgain() {
        RunningTimeAllTimePickerBottomSheetDialog(0).show(
            childFragmentManager,
            "ALL_OPEN_TIME_PICKER"
        )
    }

    // 마감 시간 재설정 함수
    private fun showCloseDialogAgain() {
        RunningTimeAllTimePickerBottomSheetDialog(1).show(
            childFragmentManager,
            "ALL_CLOSE_TIME_PICKER"
        )
    }


    // 24시 설정함수
    private fun init24HourBtn() {
        binding.cb24Hour.setOnClickListener {
            if (it.isSelected) {
                it.isSelected = false

                binding.apply {
                    // 배경 설정
                    clOpen.isEnabled = true
                    clClose.isEnabled = true

                    // 텍스트 설정
                    tvOpenHour.isEnabled = true
                    tvCloseHour.isEnabled = true
                }

            } else {
                it.isSelected = true
                set24Hour()
            }

            checkBtnState()
        }
    }

    private fun checkBtnState() {
        binding.btnConfirm.isEnabled = binding.tvTotalTime.text != "0시간"
    }

    private fun initConfirmBtn() {
        binding.btnConfirm.setOnClickListener {
            bottomSheetClickListener.onTimeAllTimeSelected(
                binding.tvOpenHour.text.toString(),
                binding.tvCloseHour.text.toString(),
                binding.tvTotalTime.text.toString()
            )
            dismiss()
        }
    }
}