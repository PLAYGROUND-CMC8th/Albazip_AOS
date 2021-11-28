package com.example.albazip.src.home.worker.closed

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentHomeWRestBinding
import com.example.albazip.src.home.common.ui.HomeAlarmActivity
import com.example.albazip.src.home.common.ui.HomeShopListActivity
import com.example.albazip.src.home.worker.data.AllHomeWResult
import com.example.albazip.src.home.worker.opened.QRScanningActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class HomeWRestFragment(data: AllHomeWResult): BaseFragment<ChildFragmentHomeWRestBinding>(
    ChildFragmentHomeWRestBinding::bind,
    R.layout.child_fragment_home_w_rest) {

    private var resultData = data

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 매장이름
        binding.tvShopName.text =  resultData.shopInfo.name
        // 요일
        binding.tvCurrentDay.text = resultData.todayInfo.month.toString() + "/" + resultData.todayInfo.date.toString() +" "+ resultData.todayInfo.day+"요일" // 오늘 날짜

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