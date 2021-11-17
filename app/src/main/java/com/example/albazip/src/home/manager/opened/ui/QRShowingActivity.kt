package com.example.albazip.src.home.manager.opened.ui

import android.os.Bundle
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityQrShowingBinding

class QRShowingActivity:BaseActivity<ActivityQrShowingBinding>(ActivityQrShowingBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 다운받기 버튼 클릭
        binding.btnDownload.setOnClickListener {
            showCustomToast("이미지를 다운로드 했습니다.")
        }
    }
}