package com.example.albazip.src.register.manager.custom

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import com.example.albazip.databinding.DialogFragmentAgeBinding
import com.example.albazip.databinding.DialogFragmentTypeBinding
import com.example.albazip.src.register.common.custom.AgeBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class TypeBottomSheetDialog : BottomSheetDialogFragment(),View.OnClickListener {

    private lateinit var binding: DialogFragmentTypeBinding
    private lateinit var selectedType:String
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
        binding = DialogFragmentTypeBinding.inflate(inflater, container, false)

        binding.rlRowOne.setOnClickListener(this)
        binding.rlRowTwo.setOnClickListener(this)
        binding.rlRowThree.setOnClickListener(this)
        binding.rlRowFour.setOnClickListener(this)
        binding.rlRowFive.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v) {
                rlRowOne -> {
                    selectedType = "카페"
                    selectEvent()
                }

                rlRowTwo -> {
                    selectedType = "음식점"
                    selectEvent()
                }

                rlRowThree -> {
                    selectedType = "판매업"
                    selectEvent()
                }

                rlRowFour -> {
                    selectedType = "서비스업"
                    selectEvent()
                }

                rlRowFive -> {
                    selectedType = "기타"
                    selectEvent()
                }
            }
        }


    }

    // select 이벤트 -> 선택 후 창 닫기
    fun selectEvent(){
        bottomSheetClickListener.onItemSelected(selectedType)
        dismiss()
    }

    interface BottomSheetClickListener{
        fun onItemSelected(text:String)
    }
}