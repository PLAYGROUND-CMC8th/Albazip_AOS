package com.playground.albazip.src.mypage.common.setting

import android.os.Bundle
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivitySubscribeBinding

class SubScribeActivity:BaseActivity<ActivitySubscribeBinding>(ActivitySubscribeBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.ibtnBack.setOnClickListener {
            finish()
        }
    }
}