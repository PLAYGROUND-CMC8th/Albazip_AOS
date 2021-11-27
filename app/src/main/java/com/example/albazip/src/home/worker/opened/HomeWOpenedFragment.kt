package com.example.albazip.src.home.worker.opened

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentHomeWOpenedBinding
import com.example.albazip.src.home.common.HomeAlarmActivity
import com.example.albazip.src.home.common.HomeShopListActivity
import com.example.albazip.src.home.worker.data.AllHomeWResult
import com.example.albazip.src.home.worker.opened.worklist.ui.HomeWTodayToDoListActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class HomeWOpenedFragment(data: AllHomeWResult) : BaseFragment<ChildFragmentHomeWOpenedBinding>(
    ChildFragmentHomeWOpenedBinding::bind,
    R.layout.child_fragment_home_w_opened
) {

    var resultData = data

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 화면 정보 받아오기
        binding.tvShopName.text = resultData.shopInfo.name // 매장명
        binding.tvDay.text =
            resultData.todayInfo.month.toString() + "/" + resultData.todayInfo.date.toString() + " " + resultData.todayInfo.day + "요일" // 오늘 날짜

        // 공동업무 완료
        binding.tvDoneCntTogether.text = resultData.taskInfo.coTask.completeCount.toString()
        binding.tvTotalCntTogehter.text = "/ " + resultData.taskInfo.coTask.totalCount.toString()
        binding.progressBarTogether.progress =
            (((resultData.taskInfo.coTask.completeCount).toDouble() / (resultData.taskInfo.coTask.totalCount).toDouble()) * 100).toInt()

        // 개인업무 완료
        binding.tvDoneCntAlone.text = resultData.taskInfo.perTask.completeCount.toString()
        binding.tvTotalCntAlone.text = "/ " + resultData.taskInfo.perTask.totalCount.toString()
        binding.progressBarAlone.progress =
            (((resultData.taskInfo.perTask.completeCount).toDouble() / (resultData.taskInfo.perTask.totalCount).toDouble()) * 100).toInt()


        // 포지션
        binding.tvWorkerPosition.text = resultData.scheduleInfo.positionTitle

        val positionTime = resultData.scheduleInfo.positionTitle.substring(2, 5).replace(" ","")
        showCustomToast(positionTime)
        when (positionTime) {
            "오픈" -> {  Glide.with(requireContext()).load(R.drawable.ic_dot_open_position).into(binding.ivPositionIcon) }
            "미들" -> { Glide.with(requireContext()).load(R.drawable.ic_dot_middle_position).into(binding.ivPositionIcon)}
            "마감" -> { Glide.with(requireContext()).load(R.drawable.ic_dot_end_position).into(binding.ivPositionIcon)}
        }


        // 알림 팝업창 닫기
        binding.btnDelPopUp.setOnClickListener {
            binding.rlAlarm.visibility = View.GONE
        }

        // 알림 화면으로 이동
        binding.ibtnBell.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeAlarmActivity::class.java)
            startActivity(nextIntent)
        }

        // qr 스캔 화면으로 이동 - 이이콘
        binding.ibtnQrScan.setOnClickListener {
            callQRActivity()
        }

        // 매장 선택화면으로 이동
        binding.rlChooseShop.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeShopListActivity::class.java)
            startActivity(nextIntent)
        }

        // 공동업무 확인 화면으로 이동
        binding.rlTogetherBg.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeWTodayToDoListActivity::class.java)
            nextIntent.putExtra("tabFlags", 0)
            startActivity(nextIntent)
        }

        // 개인(포지션)업무 확인 화면으로 이동
        binding.rlPersonalWork.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeWTodayToDoListActivity::class.java)
            nextIntent.putExtra("tabFlags", 1)
            startActivity(nextIntent)
        }
    }

    // qr 코드 스캔 시작
    private fun callQRActivity() {
        val options = ScanOptions()
        // qr 스캔을 위한 코드 (= code for QR Scanning)
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)

        options.setPrompt("Scan a barcode")
        options.setCameraId(0) // Use a specific camera of the device
        options.captureActivity = QRScanningActivity::class.java
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        barcodeLauncher.launch(options)
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Scanned: " + result.contents, Toast.LENGTH_LONG)
                .show()
        }
    }
}