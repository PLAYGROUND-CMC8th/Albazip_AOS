package com.example.albazip.src.mypage.manager.custom

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.albazip.databinding.DialogFragmentPayUnitBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PayUnitBottomSheetDialog(currentSelect:String) : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: DialogFragmentPayUnitBinding
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
        binding = DialogFragmentPayUnitBinding.inflate(inflater, container, false)

        // 가장 최근에 선택한 배경색 받아오기
        when(myCurrentSelect){
            "시급" -> {binding.rlRowOne.setBackgroundColor(Color.parseColor("#fffaea"))}
            "주급" -> {binding.rlRowTwo.setBackgroundColor(Color.parseColor("#fffaea"))}
            "월급" -> {binding.rlRowThree.setBackgroundColor(Color.parseColor("#fffaea"))}
        }

        binding.rlRowOne.setOnClickListener(this)
        binding.rlRowTwo.setOnClickListener(this)
        binding.rlRowThree.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v) {
                rlRowOne -> {
                    selectedType = "시급"
                    selectEvent()
                }

                rlRowTwo -> {
                    selectedType = "주급"
                    selectEvent()
                }

                rlRowThree -> {
                    selectedType = "월급"
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