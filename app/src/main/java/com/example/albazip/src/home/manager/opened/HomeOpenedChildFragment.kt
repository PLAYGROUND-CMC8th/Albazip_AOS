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
import com.example.albazip.src.home.manager.data.AllHomeMResult
import com.example.albazip.src.home.manager.opened.ui.QRShowingActivity
import com.example.albazip.src.home.manager.opened.ui.TodaysWorkerListActivity

class HomeOpenedChildFragment(data:AllHomeMResult): BaseFragment<ChildFragmentHomeOpenedBinding>(
    ChildFragmentHomeOpenedBinding::bind,
    R.layout.child_fragment_home_opened) {

    val resultData = data

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 화면 정보 받아오기
        binding.tvShopName.text = resultData.shopInfo.name // 매장명
        binding.tvDay.text = resultData.todayInfo.month.toString() + "/" + resultData.todayInfo.date.toString() +" "+ resultData.todayInfo.day+"요일" // 오늘 날짜

        // 공동업무 완료
        binding.tvDoneCntTogether.text = resultData.taskInfo.coTask.completeCount.toString()
        binding.tvTotalCntTogehter.text = "/ " + resultData.taskInfo.coTask.totalCount.toString()
        binding.progressBarTogether.progress = (((resultData.taskInfo.coTask.completeCount).toDouble() / (resultData.taskInfo.coTask.totalCount).toDouble()) * 100).toInt()

        // 개인업무 완료
        binding.tvDoneCntAlone.text = resultData.taskInfo.perTask.completeCount.toString()
        binding.tvTotalCntAlone.text = "/ "+ resultData.taskInfo.perTask.totalCount.toString()
        binding.progressBarAlone.progress = (((resultData.taskInfo.perTask.completeCount).toDouble() / (resultData.taskInfo.perTask.totalCount).toDouble()) * 100).toInt()


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