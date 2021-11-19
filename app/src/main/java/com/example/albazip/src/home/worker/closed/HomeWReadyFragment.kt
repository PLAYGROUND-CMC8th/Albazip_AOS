package com.example.albazip.src.home.worker.closed

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentHomeWReadyBinding
import com.example.albazip.src.home.common.HomeAlarmActivity
import com.example.albazip.src.home.common.HomeShopListActivity
import com.example.albazip.src.home.manager.opened.ui.QRShowingActivity
import com.example.albazip.src.home.worker.opened.HomeWOpenedFragment
import com.example.albazip.src.home.worker.opened.QRScanningActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.android.Intents
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


class HomeWReadyFragment: BaseFragment<ChildFragmentHomeWReadyBinding>(
    ChildFragmentHomeWReadyBinding::bind,
    R.layout.child_fragment_home_w_ready) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // qr 조회 화면으로 이동
       // binding.ibtnQrScan.setOnClickListener {
       //     val nextIntent = Intent(requireContext(), QRShowingActivity::class.java)
       //     startActivity(nextIntent)
        //}

        // 알림 화면으로 이동
        binding.ibtnBell.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeAlarmActivity::class.java)
            startActivity(nextIntent)
        }

        // qr 스캔 화면으로 이동(1) - 이이콘
        binding.ibtnQrScan.setOnClickListener {
            callQRActivity()
        }

        // qr 스캔 화면으로 이동(2) - 버튼
        binding.btnGoWork.setOnClickListener {
            callQRActivity()
        }

        // 매장 선택화면으로 이동
        binding.rlChooseShop.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeShopListActivity::class.java)
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
        if (result.contents == null) { // 출근 취소
            Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_LONG).show()
        } else { // 출근 완료
            Toast.makeText(requireContext(), "Scanned: " + result.contents, Toast.LENGTH_LONG)
                .show()

            // 화면 변경(근무중 fragment로 교체)
            parentFragmentManager.beginTransaction().replace(R.id.home_child_frame_layout, HomeWOpenedFragment())
                .commitAllowingStateLoss()
        }
    }

}