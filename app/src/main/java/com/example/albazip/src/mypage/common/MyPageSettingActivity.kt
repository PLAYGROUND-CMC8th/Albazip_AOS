package com.example.albazip.src.mypage.common

import android.os.Bundle
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivitySettingMyPageBinding
import com.example.albazip.src.mypage.custom.LogoutBottomSheetDialog

class MyPageSettingActivity:BaseActivity<ActivitySettingMyPageBinding>(ActivitySettingMyPageBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.rlLogout.setOnClickListener {
            LogoutBottomSheetDialog().show(supportFragmentManager, "logoutAlert")
        }
    }
}