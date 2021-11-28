package com.example.albazip.src.home.common.ui

import android.os.Bundle
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityHomeAlarmBinding

class HomeAlarmActivity:BaseActivity<ActivityHomeAlarmBinding>(ActivityHomeAlarmBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.ibtnClose.setOnClickListener {
            finish()
        }
    }
}