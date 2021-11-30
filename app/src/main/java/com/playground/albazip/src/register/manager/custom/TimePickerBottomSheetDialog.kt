package com.playground.albazip.src.register.manager.custom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import com.playground.albazip.databinding.DialogFragmentTimeBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TimePickerBottomSheetDialog(titleTxt:String,flag: Int) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogFragmentTimeBinding
    private var selectedHour:Int = 0 // 초기값 설정(h)
    private var selectedMinute:Int = 0 // 초기값 설정
    lateinit var bottomSheetClickListener:BottomSheetClickListener
    var titleTxt = titleTxt // 타이틀 선택
    var flag = flag // 오픈 or 마감인지 선택

    override fun onAttach(context: Context) {
        super.onAttach(context)

        bottomSheetClickListener = context as BottomSheetClickListener

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentTimeBinding.inflate(inflater, container, false)

        binding.tvDialogTitle.text = titleTxt

        // 24시간 형식으로 설정
        binding.timePicker.setIs24HourView(true)

        callTimePicker()

        // 취소 버튼
        binding.btnCancel.setOnClickListener {

            dismiss()
        }

        // 확인 버튼
        binding.btnOk.setOnClickListener {
            // activity에 값 전달
            bottomSheetClickListener.onTimeSelected(selectedHour.toString(),selectedMinute.toString(),flag)

            dismiss()
        }

        return binding.root
    }


    private fun callTimePicker(){

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

    interface BottomSheetClickListener{
        fun onTimeSelected(h:String,m:String,flag:Int)
    }



}