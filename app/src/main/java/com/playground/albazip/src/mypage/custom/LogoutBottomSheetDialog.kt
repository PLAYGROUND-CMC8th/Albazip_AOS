package com.playground.albazip.src.mypage.custom

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.playground.albazip.config.ApplicationClass.Companion.prefs
import com.playground.albazip.databinding.DialogFragmentLogoutBinding
import com.playground.albazip.src.main.MainActivity
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

            // 로그인 flag 변경(로그아웃)
            prefs.setInt("loginFlags",0)
            // token 비우기
            prefs.setString("X-ACCESS-TOKEN","")

            // 메인 화면으로 이동
            val mainIntent = Intent(requireContext(),MainActivity::class.java)
            startActivity(mainIntent)
            // 이전 엑티비티 모두 종료
            activity?.finishAffinity()
        }

        return binding.root
    }
}