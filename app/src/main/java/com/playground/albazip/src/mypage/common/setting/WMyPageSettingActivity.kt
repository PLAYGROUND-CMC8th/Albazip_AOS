package com.playground.albazip.src.mypage.common.setting

import android.content.Intent
import android.os.Bundle
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivitySettingWMyPageBinding
import com.playground.albazip.src.mypage.common.setting.editinfo.ManageMyInfoActivity
import com.playground.albazip.src.mypage.custom.LogoutBottomSheetDialog
import com.playground.albazip.src.withdraw.ui.DefaultWithDrawActivity

class WMyPageSettingActivity:BaseActivity<ActivitySettingWMyPageBinding>(ActivitySettingWMyPageBinding::inflate){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뒤로가기 화면
        binding.ibtnClose.setOnClickListener {
            finish()
        }

        // 알림 설정 화면
        binding.rlAlarmSettingBtn.setOnClickListener {
            val nextIntent = Intent(this, AlarmSettingActivity::class.java)
            startActivity(nextIntent)
        }

        // 공지사항 화면
        binding.rlNotice.setOnClickListener {
            val nextIntent = Intent(this, AppNoticeActivity::class.java)
            startActivity(nextIntent)
        }

        // 서비스 이용약관
        binding.rlService.setOnClickListener {
            val nextIntent = Intent(this,ServiceLawActivity::class.java)
            startActivity(nextIntent)
        }

        // 로그아웃 다이얼로그
        binding.rlLogout.setOnClickListener {
            LogoutBottomSheetDialog().show(supportFragmentManager, "logoutAlert")
        }

        // 내정보관리 화면
        binding.rlManageMyinfo.setOnClickListener {
            val nextIntent = Intent(this, ManageMyInfoActivity::class.java)
            startActivity(nextIntent)
        }

        // 회원탈퇴
        binding.rlWithdraw.setOnClickListener {
            val nextIntent = Intent(this, DefaultWithDrawActivity::class.java)
            startActivity(nextIntent)
        }
    }
}