package com.example.albazip.ui.register.common.custom

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import com.example.albazip.R
import com.example.albazip.databinding.DialogFragmentAgeBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AgeBottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding:DialogFragmentAgeBinding
    private var selectedAge:Int = 1950

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentAgeBinding.inflate(inflater, container, false)

        callNumberPicker()

        // 취소 버튼
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        // 확인 버튼
        binding.btnOk.setOnClickListener {

            Toast.makeText(requireContext(),selectedAge.toString(),Toast.LENGTH_LONG).show()

            dismiss()
        }

        return binding.root
    }


    private fun callNumberPicker(){

        val yearMin = 1950
        val yearMax = 2021

        // 피커 생성
        binding.numberPicker.apply {
            // age 범위 설정
            minValue = yearMin
            maxValue = yearMax
            wrapSelectorWheel = false
            value = 1998
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        }

        // 선택한 날짜 받아오는 함수
        binding.numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            selectedAge = newVal
        }
    }



}