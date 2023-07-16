package com.playground.albazip.src.mypage.manager.workerlist.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.playground.albazip.databinding.DialogFragmentTodoHelpBinding

class TodoHelpBottomSheet() : BottomSheetDialogFragment() {

    private lateinit var binding: DialogFragmentTodoHelpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentTodoHelpBinding.inflate(inflater, container, false)

        // 취소 버튼
        binding.btnConfirm.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}