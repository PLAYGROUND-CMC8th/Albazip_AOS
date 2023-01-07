package com.playground.albazip.src.update.runtime.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.playground.albazip.databinding.DialogFragmentCancelRunningTimeBinding

class RunningTimeCancelBottomSheetDialog() :
    BottomSheetDialogFragment() {

    private var _binding: DialogFragmentCancelRunningTimeBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogFragmentCancelRunningTimeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initTxtView()
        initBtnEvent()
    }

    /*private fun initTxtView(){
        var noSelectedDayString = ""

        for (text in noSelectedList) {
            noSelectedDayString += ("$text, ")
        }

        noSelectedDayString = noSelectedDayString.substring(0,noSelectedDayString.lastIndex-1)
        binding.tvDate.text = "설정하지 않은 요일:$noSelectedDayString"
    }*/

    // 버튼이벤트
    private fun initBtnEvent() {

        // 나가기
        binding.btnOut.setOnClickListener {
            dismiss()
            activity?.finish()
        }

        // 취소
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}