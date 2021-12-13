package com.playground.albazip.src.home.worker.opened

import android.os.Bundle
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityQrScanBinding
import com.journeyapps.barcodescanner.CaptureManager
import com.playground.albazip.util.GetCurrentTime

class QRScanningActivity:BaseActivity<ActivityQrScanBinding>(ActivityQrScanBinding::inflate) {

    private lateinit var captureManager: CaptureManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 닫기
        binding.ibtnClose.setOnClickListener{
            finish()
        }

        //qr 스캔 날짜
        binding.tvScanDate.text = GetCurrentTime().getQRScanDate

        captureManager = CaptureManager(this,binding.barcodeScanner)
        captureManager.initializeFromIntent(intent, savedInstanceState)
        captureManager.decode()
    }

    override fun onResume() {
        super.onResume()
        captureManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        captureManager.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        captureManager.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        captureManager.onSaveInstanceState(outState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        captureManager.onRequestPermissionsResult(requestCode,permissions,grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}