package com.example.albazip.src.mypage.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.albazip.databinding.DialogFragmentLogoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LogoutBottomSheetDialog: BottomSheetDialogFragment() {
    private lateinit var binding:DialogFragmentLogoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentLogoutBinding.inflate(inflater, container, false)


        // 취소 버튼
        binding.btnCancel.setOnClickListener {

            dismiss()
        }

        // 로그아웃 버튼
        binding.btnLogout.setOnClickListener {
            // activity에 값 전달

            dismiss()
        }

        return binding.root
    }
}