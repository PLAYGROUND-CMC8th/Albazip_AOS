package com.example.albazip.src.home.common.ui

import android.content.Intent
import android.os.Bundle
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivitySettingBinding
import com.example.albazip.src.mypage.common.setting.AlarmSettingActivity
import com.example.albazip.src.mypage.common.setting.AppNoticeActivity
import com.example.albazip.src.mypage.common.setting.ServiceLawActivity
import com.example.albazip.src.mypage.custom.LogoutBottomSheetDialog
import com.example.albazip.src.withdraw.ui.DefaultWithDrawActivity

class SettingActivity : BaseActivity<ActivitySettingBinding>(ActivitySettingBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // close 버튼
        binding.ibtnClose.setOnClickListener {
            finish()
        }

        // 알림설정
        binding.rlAlarmSet.setOnClickListener {
            val nextIntent = Intent(this,AlarmSettingActivity::class.java)
            startActivity(nextIntent)
        }

        // 내 정보 관리


        // 공지사항
        binding.rlNoticeSet.setOnClickListener {
            val nextIntent = Intent(this,AppNoticeActivity::class.java)
            startActivity(nextIntent)
        }

        // 서비스 이용약관
        binding.rlHowToUseSet.setOnClickListener {
            val nextIntent = Intent(this,ServiceLawActivity::class.java)
            startActivity(nextIntent)
        }

        // 로그아웃
        binding.rlLogoutSet.setOnClickListener {
            LogoutBottomSheetDialog().show(supportFragmentManager, "logoutAlert")
        }

        // 회원탈퇴
        binding.rlWithdrawSet.setOnClickListener {
            val nextIntent = Intent(this,DefaultWithDrawActivity::class.java)
            startActivity(nextIntent)
        }
    }
}