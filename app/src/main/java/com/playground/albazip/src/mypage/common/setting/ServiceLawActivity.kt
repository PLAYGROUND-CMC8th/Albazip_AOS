package com.playground.albazip.src.mypage.common.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityServiceLawBinding

class ServiceLawActivity:BaseActivity<ActivityServiceLawBinding>(ActivityServiceLawBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 닫기
        binding.ibtnClose.setOnClickListener {
            finish()
        }

        binding.rlServiceLaw.setOnClickListener {
            val uriIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://bronzed-balaur-143.notion.site/42041221d1a6413f84542f571bee6b9c"))
            startActivity(uriIntent)
        }

        binding.rlInfoLaw.setOnClickListener {
            val uriIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://bronzed-balaur-143.notion.site/02e8b5a9cf514702977b4e01b82651ca"))
            startActivity(uriIntent)
        }
    }
}