package com.playground.albazip.src.register.manager.moreinfo.custom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.playground.albazip.databinding.DialogFragment24HourBinding
import com.playground.albazip.src.update.runtime.custom.Confirm24HourBottomSheetDialog
import com.playground.albazip.src.update.runtime.custom.RunningTimePickerBottomSheetDialog
import com.playground.albazip.util.GetTimeDiffUtil

class AllTimeBottomSheetDialog() : BottomSheetDialogFragment(),
    RunningTimePickerBottomSheetDialog.BottomSheetClickListener {

    private lateinit var binding: DialogFragment24HourBinding
    lateinit var bottomSheetClickListener: BottomSheetClickListener

    private var get24HourState: Boolean = false

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
        initTimeDialog() // 시간 설정 다이얼로그
        initConfirmBtn() // 확인버튼 이벤트

        return binding.root
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

    // 24시간 영업 함수
    private fun init24HourBtn() {
        binding.apply {
            cb24Hour.setOnClickListener {
                if (!get24HourState) { // 활성화 안된 상태면
                    cb24Hour.isSelected = true
                    get24HourState = true
                    set24HourUI(get24HourState)
                } else {
                    cb24Hour.isSelected = false
                    get24HourState = false
                    set24HourUI(get24HourState)
                }

                checkConfirmBtnEvent()
            }
        }
    }

    // 24시간 UI 설정함수
    private fun set24HourUI(type: Boolean = true) {
        binding.apply {
            tvOpenHour.text = "00:00"
            tvCloseHour.text = "00:00"
            tvTotalTime.text = "24시간"

            if (type == true) {
                tvOpenHour.isEnabled = false
                tvCloseHour.isEnabled = false

                clOpen.isEnabled = false
                clClose.isEnabled = false

                cb24Hour.isSelected = true
            } else {
                tvOpenHour.isEnabled = true
                tvCloseHour.isEnabled = true

                clOpen.isEnabled = true
                clClose.isEnabled = true

                cb24Hour.isSelected = false
            }
        }
    }

    // 시간 설정 다이얼로그
    private fun initTimeDialog() {
        // 오픈시간 설정
        binding.apply {
            clOpen.setOnClickListener {
                RunningTimePickerBottomSheetDialog(0).show(childFragmentManager, "openTimePicker")
            }

            clClose.setOnClickListener {
                RunningTimePickerBottomSheetDialog(1).show(childFragmentManager, "closeTimePicker")
            }
        }
    }

    // 확인 버튼
    private fun checkConfirmBtnEvent() {
        binding.btnConfirm.isEnabled = binding.tvTotalTime.text != "0시간"
    }

    interface BottomSheetClickListener {
        fun onTimeAllTimeSelected(oTime:String, eTime:String, totalTime: String)
    }

    // 개별 시간 선택 함수
    override fun onTimeSelected(h: String, m: String, flag: Int) {

        var displayHour = "00"
        var displayMinute = "00"

        // ui에 보여지는 시간과 분
        displayHour = if (h.length == 1) { // 한자리 숫자일 때는 앞에 "0"을 붙여준다.
            "0$h"
        } else {
            h
        }

        displayMinute = if (m.length == 1) {
            "0$m"
        } else {
            m
        }

        if (flag == 0) { // 오픈시간
            binding.tvOpenHour.text = "$displayHour:$displayMinute"

            // 마감타임이 00:00이 아닐 때
            // 오픈타임 == 마감타임이면
            // 24시간 체크 여부를 묻는 다이얼로그를 띄운다.
            binding.apply {
                if (tvCloseHour.text != "00:00") {
                    if (tvOpenHour.text == tvCloseHour.text) {
                        val check24ConfirmDialog = Confirm24HourBottomSheetDialog(0,
                            { doAfterConfirm() },
                            { doAfterCancelOpen() },
                            { doAfterCancelClose() })

                        check24ConfirmDialog.isCancelable = false
                        check24ConfirmDialog.show(
                            childFragmentManager,
                            "check_open_24_confirm"
                        )
                    }
                }

                tvOpenHour.isEnabled = true
            }


        } else { // 마감시간
            binding.tvCloseHour.text = "$displayHour:$displayMinute"

            binding.apply {
                if (tvCloseHour.text != "00:00") {
                    if (tvCloseHour.text == tvOpenHour.text) {
                        val check24ConfirmDialog = Confirm24HourBottomSheetDialog(1,
                            { doAfterConfirm() },
                            { doAfterCancelOpen() },
                            { doAfterCancelClose() })

                        check24ConfirmDialog.isCancelable = false
                        check24ConfirmDialog.show(
                            childFragmentManager,
                            "check_close_24_confirm"
                        )
                    }
                }

                tvCloseHour.isEnabled = true
            }
        }

        // 시간차 텍스트 설정
        binding.apply {
            if (tvCloseHour.isEnabled && tvOpenHour.isEnabled) {
                GetTimeDiffUtil().getTimeDiff(
                    binding.tvOpenHour,
                    binding.tvCloseHour,
                    binding.tvTotalTime
                )
            }
        }

        checkConfirmBtnEvent()
    }


    // 24시간으로 설정하기
    private fun doAfterConfirm() {
        binding.apply {
            set24HourUI()
            checkConfirmBtnEvent()
        }
    }

    // 오픈 시간 재설정 -> 바텀시트 다시 띄우기
    private fun doAfterCancelOpen() {
        RunningTimePickerBottomSheetDialog(0).show(
            childFragmentManager,
            "set_open_hour"
        )
        Toast.makeText(requireContext(), "마감 시간과 같아요. 시간을 다시 설정해주세요.", Toast.LENGTH_SHORT).show()
    }

    // 마감 시간 재설정 -> 바텀시트 다시 띄우기
    private fun doAfterCancelClose() {
        RunningTimePickerBottomSheetDialog(1).show(
            childFragmentManager,
            "set_close_hour"
        )

        Toast.makeText(requireContext(), "오픈시간과 같아요. 시간을 다시 설정해주세요.", Toast.LENGTH_SHORT).show()
    }
}