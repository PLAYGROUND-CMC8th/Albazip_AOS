package com.playground.albazip.src.home.manager.closed

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.playground.albazip.R
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.ChildFragmentHomeRestBinding
import com.playground.albazip.src.home.common.ui.HomeAlarmActivity
import com.playground.albazip.src.home.common.ui.HomeShopListActivity
import com.playground.albazip.src.home.manager.data.AllHomeMResult
import com.playground.albazip.src.home.manager.opened.ui.QRShowingActivity

class HomeRestChildFragment(data:AllHomeMResult): BaseFragment<ChildFragmentHomeRestBinding>(
    ChildFragmentHomeRestBinding::bind,
    R.layout.child_fragment_home_rest) {

    var resultData = data

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

        // 매장 선택화면으로 이동
        binding.rlChooseShop.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeShopListActivity::class.java)
            startActivity(nextIntent)
        }
    }
}