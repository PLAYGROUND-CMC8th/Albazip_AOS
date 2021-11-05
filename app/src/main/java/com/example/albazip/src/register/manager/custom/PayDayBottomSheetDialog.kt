package com.example.albazip.src.register.manager.custom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.example.albazip.databinding.DialogFragmentAgeBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PayDayBottomSheetDialog() : BottomSheetDialogFragment() {

    private lateinit var binding: DialogFragmentAgeBinding
    private var selectedDay:Int = 1 // 초기값 설정
    private lateinit var bottomSheetClickListener:BottomSheetClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        bottomSheetClickListener = context as BottomSheetClickListener

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentAgeBinding.inflate(inflater, container, false)

        binding.tvDialogTitle.text = "급여일 선택"

        callNumberPicker()

        // 취소 버튼
        binding.btnCancel.setOnClickListener {

            dismiss()
        }

        // 확인 버튼
        binding.btnOk.setOnClickListener {
            // activity에 값 전달
            bottomSheetClickListener.onItemSelected(selectedDay.toString())

            dismiss()
        }

        return binding.root
    }


    private fun callNumberPicker(){

        val dayMin = 1
        val dayMax = 31

        // 피커 생성
        binding.numberPicker.apply {
            // age 범위 설정
            minValue = dayMin
            maxValue = dayMax
            wrapSelectorWheel = false
            value = 1
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        }

        // 선택한 날짜 받아오는 함수
        binding.numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            selectedDay = newVal
        }
    }

    interface BottomSheetClickListener{
        fun onItemSelected(text:String)
    }



}