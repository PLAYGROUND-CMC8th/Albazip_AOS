package com.example.albazip.src.home.common

import android.os.Bundle
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity<ActivitySettingBinding>(ActivitySettingBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // close 버튼
        binding.ibtnClose.setOnClickListener {
            finish()
        }
    }
}