package com.playground.albazip.src.home.common.ui

import android.os.Bundle
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityHomeAlarmBinding

class HomeAlarmActivity:BaseActivity<ActivityHomeAlarmBinding>(ActivityHomeAlarmBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.ibtnClose.setOnClickListener {
            finish()
        }
    }
}