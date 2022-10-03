package com.playground.albazip.src.update.setworker.custom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.playground.albazip.databinding.DialogFragmentTimeBinding
import java.lang.Exception


class SetWorkerTimePickerBottomSheetDialog(// 타이틀 선택
    var timeFlag: Int, // 오픈 0, 마감 1
    var position:Int, // 몇 번째 요소를 선택한건지
    private val doAfterOk: () -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogFragmentTimeBinding
    private var selectedHour: Int = 0 // 초기값 설정(h)
    private var selectedMinute: Int = 0 // 초기값 설정
    lateinit var bottomSheetClickListener: BottomSheetClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        bottomSheetClickListener = try {
            context as BottomSheetClickListener
        } catch (e: Exception) {
            parentFragment as BottomSheetClickListener
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentTimeBinding.inflate(inflater, container, false)


        initUI() // UI 초기화
        callTimePicker() // 타임피커 생성

        // 취소 버튼
        binding.btnCancel.setOnClickListener {

            dismiss()
        }

        // 확인 버튼
        binding.btnOk.setOnClickListener {
            // adapter 에 값 전달
            bottomSheetClickListener.onTimeSelected(
                selectedHour.toString(),
                selectedMinute.toString(),
                timeFlag,
                position
            )

            doAfterOk()
            dismiss()
        }

        return binding.root
    }

    private fun initUI() {

        if (timeFlag == 0) { // 오픈이면
            binding.tvDialogTitle.text = "출근시간"
        } else {
            binding.tvDialogTitle.text = "마감시간"
        }

        // 타임피커 24시간 형식으로 설정
        binding.timePicker.setIs24HourView(true)
    }

    private fun callTimePicker() {

        // 피커 생성
        binding.timePicker.apply {
            descendantFocusability = TimePicker.FOCUS_BLOCK_DESCENDANTS
            hour = 0
            minute = 0
        }


        // 선택한 시간을 받아오는 함수
        binding.timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            selectedHour = hourOfDay
            selectedMinute = minute
        }
    }

    interface BottomSheetClickListener {
        fun onTimeSelected(h: String, m: String, flag: Int, position: Int)
    }


}