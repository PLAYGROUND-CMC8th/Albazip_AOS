package com.playground.albazip.src.update.runtime.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.playground.albazip.databinding.DialogFragmentConfirm24HourBinding

class Confirm24HourBottomSheetDialog(
    private val doAfterConfirm: () -> Unit,
    private val doAfterCancel: () -> Unit,
) : BottomSheetDialogFragment() {
    private lateinit var binding: DialogFragmentConfirm24HourBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentConfirm24HourBinding.inflate(inflater, container, false)

        // 다시 설정 버튼
        binding.btnCancel.setOnClickListener {
            doAfterCancel()
            dismiss()
        }

        // 확인
        binding.btnComfirm.setOnClickListener {
            doAfterConfirm() // 24시간 설정
            dismiss()
        }


        return binding.root
    }
}