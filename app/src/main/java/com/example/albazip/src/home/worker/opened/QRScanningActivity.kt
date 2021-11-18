package com.example.albazip.src.home.worker.opened

import android.os.Bundle
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityQrScanBinding
import com.journeyapps.barcodescanner.CaptureManager

class QRScanningActivity:BaseActivity<ActivityQrScanBinding>(ActivityQrScanBinding::inflate) {

    private lateinit var captureManager: CaptureManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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