package com.example.albazip.src.home.common

import android.os.Bundle
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityHomeShopListBinding

class HomeShopListActivity:BaseActivity<ActivityHomeShopListBinding>(ActivityHomeShopListBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 화면 닫기
        binding.btnOut.setOnClickListener {
            finish()
        }
    }
}