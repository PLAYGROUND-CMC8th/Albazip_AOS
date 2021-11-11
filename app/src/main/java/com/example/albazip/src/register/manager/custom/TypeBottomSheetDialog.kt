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


class TypeBottomSheetDialog(currentSelect:String) : BottomSheetDialogFragment(),View.OnClickListener {

    private lateinit var binding: DialogFragmentTypeBinding
    private lateinit var selectedType:String
    lateinit var bottomSheetClickListener: BottomSheetClickListener
    val myCurrentSelect = currentSelect

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

        // 가장 최근에 선택한 배경색 받아오기
        when(myCurrentSelect){
            "카페" -> {binding.rlRowOne.setBackgroundColor(Color.parseColor("#fffaea"))}
            "음식점" -> {binding.rlRowTwo.setBackgroundColor(Color.parseColor("#fffaea"))}
            "판매업" -> {binding.rlRowThree.setBackgroundColor(Color.parseColor("#fffaea"))}
            "서비스업" -> {binding.rlRowFour.setBackgroundColor(Color.parseColor("#fffaea"))}
            "기타" -> {binding.rlRowFive.setBackgroundColor(Color.parseColor("#fffaea"))}
        }

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