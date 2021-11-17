package com.example.albazip.src.home.manager.closed

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentHomePrepareBinding
import com.example.albazip.src.home.common.HomeAlarmActivity
import com.example.albazip.src.home.manager.custom.AddWorkBottomSheetDialog
import com.example.albazip.src.home.manager.opened.ui.QRShowingActivity

class HomePreParingChildFragment:BaseFragment<ChildFragmentHomePrepareBinding>(ChildFragmentHomePrepareBinding::bind,
    R.layout.child_fragment_home_prepare) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // qr 조회 화면으로 이동
        binding.ibtnQrScan.setOnClickListener {
            val nextIntent = Intent(requireContext(), QRShowingActivity::class.java)
            startActivity(nextIntent)
        }

        // 알림 화면으로 이동
        binding.ibtnBell.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeAlarmActivity::class.java)
            startActivity(nextIntent)
        }

        // 업무추가 다이얼로그
        // 업무 추가
        binding.btnAddWork.setOnClickListener {
            AddWorkBottomSheetDialog().show(parentFragmentManager, "addwork")
        }
    }
}