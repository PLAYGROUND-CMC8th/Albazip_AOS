package com.example.albazip.src.home.manager.closed

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentHomePrepareBinding
import com.example.albazip.src.home.common.ui.HomeAlarmActivity
import com.example.albazip.src.home.common.ui.HomeShopListActivity
import com.example.albazip.src.home.manager.custom.AddWorkBottomSheetDialog
import com.example.albazip.src.home.manager.data.AllHomeMResult
import com.example.albazip.src.home.manager.opened.ui.QRShowingActivity

class HomePreParingChildFragment(data: AllHomeMResult):BaseFragment<ChildFragmentHomePrepareBinding>(ChildFragmentHomePrepareBinding::bind,
    R.layout.child_fragment_home_prepare) {

    private var resultData = data

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 매장이름
        binding.tvShopName.text =  resultData.shopInfo.name
        // 요일
        binding.tvCurrentDay.text = resultData.todayInfo.month.toString() + "/" + resultData.todayInfo.date.toString() +" "+ resultData.todayInfo.day+"요일" // 오늘 날짜

        // qr 조회 화면으로 이동
        binding.ibtnQrScan.setOnClickListener {
            val nextIntent = Intent(requireContext(), QRShowingActivity::class.java)
            nextIntent.putExtra("shop_name",binding.tvShopName.text)
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

        // 매장 선택화면으로 이동
        binding.rlChooseShop.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeShopListActivity::class.java)
            startActivity(nextIntent)
        }
    }
}