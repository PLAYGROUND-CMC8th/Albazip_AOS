package com.example.albazip.src.mypage.common

import android.os.Bundle
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivitySettingWMyPageBinding
import com.example.albazip.src.mypage.custom.LogoutBottomSheetDialog

class WMyPageSettingActivity:BaseActivity<ActivitySettingWMyPageBinding>(ActivitySettingWMyPageBinding::inflate){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 로그아웃 다이얼로그
        binding.rlLogout.setOnClickListener {
            LogoutBottomSheetDialog().show(supportFragmentManager, "logoutAlert")
        }
    }
}