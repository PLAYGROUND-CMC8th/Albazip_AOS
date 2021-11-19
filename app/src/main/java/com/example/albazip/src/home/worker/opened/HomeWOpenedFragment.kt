package com.example.albazip.src.home.worker.opened

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentHomeWOpenedBinding
import com.example.albazip.src.home.common.HomeAlarmActivity
import com.example.albazip.src.home.common.HomeShopListActivity
import com.example.albazip.src.home.worker.opened.worklist.ui.HomeWTodayToDoListActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class HomeWOpenedFragment:BaseFragment<ChildFragmentHomeWOpenedBinding>(ChildFragmentHomeWOpenedBinding::bind,
    R.layout.child_fragment_home_w_opened) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 알림 팝업창 닫기
        binding.btnDelPopUp.setOnClickListener{
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
            nextIntent.putExtra("tabFlags",0)
            startActivity(nextIntent)
        }

        // 개인(포지션)업무 확인 화면으로 이동
        binding.rlPersonalWork.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeWTodayToDoListActivity::class.java)
            nextIntent.putExtra("tabFlags",1)
            startActivity(nextIntent)
        }
    }

    // qr 코드 스캔 시작
    private fun callQRActivity(){
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