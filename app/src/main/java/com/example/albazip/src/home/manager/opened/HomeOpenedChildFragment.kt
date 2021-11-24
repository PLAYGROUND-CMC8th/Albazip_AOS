package com.example.albazip.src.home.manager.opened

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentHomeOpenedBinding
import com.example.albazip.src.home.common.HomeAlarmActivity
import com.example.albazip.src.home.common.HomeShopListActivity
import com.example.albazip.src.home.manager.custom.AddWorkBottomSheetDialog
import com.example.albazip.src.home.manager.opened.ui.QRShowingActivity
import com.example.albazip.src.home.manager.opened.ui.TodaysWorkerListActivity
import com.example.albazip.src.register.manager.custom.TimePickerBottomSheetDialog

class HomeOpenedChildFragment: BaseFragment<ChildFragmentHomeOpenedBinding>(
    ChildFragmentHomeOpenedBinding::bind,
    R.layout.child_fragment_home_opened) {

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

        // 오늘의 근무자 리스트 보기
        binding.tvTodayWorkers.setOnClickListener {
            val nextIntent = Intent(requireContext(), TodaysWorkerListActivity::class.java)
            startActivity(nextIntent)
        }
        binding.llWorkerList.setOnClickListener{
            val nextIntent = Intent(requireContext(), TodaysWorkerListActivity::class.java)
            startActivity(nextIntent)
        }

        // 업무 추가 다이얼로그
        binding.ibtnAdd.setOnClickListener {
            AddWorkBottomSheetDialog().show(parentFragmentManager, "addwork")
        }

        // 매장 선택화면으로 이동
        binding.rlChooseShop.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeShopListActivity::class.java)
            startActivity(nextIntent)
        }

    }
}