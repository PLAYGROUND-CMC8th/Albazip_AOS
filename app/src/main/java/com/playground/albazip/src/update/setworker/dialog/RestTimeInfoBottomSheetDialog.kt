package com.playground.albazip.src.update.setworker.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.playground.albazip.databinding.DialogFragmentRestTimeInfoBinding

class RestTimeInfoBottomSheetDialog() :
    BottomSheetDialogFragment() {

    private var _binding: DialogFragmentRestTimeInfoBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogFragmentRestTimeInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBottomSheetTxt()
        initBtnEvent()
    }

    private fun initBottomSheetTxt() {
        binding.btnCancel.text = "확인"
    }

    // 버튼이벤트
    private fun initBtnEvent() {
        // 확인
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}