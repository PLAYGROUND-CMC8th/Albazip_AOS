package com.playground.albazip.src.update.setworker.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.playground.albazip.databinding.DialogUpdateTimeSetCancelBinding

class WorkTimeCancelBottomSheetDialog(private val doAfterEvent: () -> Unit) :
    BottomSheetDialogFragment() {

    private var _binding: DialogUpdateTimeSetCancelBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = DialogUpdateTimeSetCancelBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBtnEvent()
    }

    // 버튼이벤트
    private fun initBtnEvent() {

        // 나가기
        binding.btnOut.setOnClickListener {
            doAfterEvent()
            dismiss()
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