package com.example.albazip.src.mypage.common

import android.content.Intent
import android.os.Bundle
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivitySettingWMyPageBinding
import com.example.albazip.src.mypage.custom.LogoutBottomSheetDialog

class WMyPageSettingActivity:BaseActivity<ActivitySettingWMyPageBinding>(ActivitySettingWMyPageBinding::inflate){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 알림 설정 화면
        binding.rlAlarmSettingBtn.setOnClickListener {
            val nextIntent = Intent(this, AlarmSettingActivity::class.java)
            startActivity(nextIntent)
        }

        // 로그아웃 다이얼로그
        binding.rlLogout.setOnClickListener {
            LogoutBottomSheetDialog().show(supportFragmentManager, "logoutAlert")
        }
    }
}