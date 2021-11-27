package com.example.albazip.src.mypage.common.setting

import android.os.Bundle
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityAppNoticeBinding

class AppNoticeActivity:BaseActivity<ActivityAppNoticeBinding>(ActivityAppNoticeBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뒤로가기
        binding.ibtnClose.setOnClickListener {
            finish()
        }
    }
}