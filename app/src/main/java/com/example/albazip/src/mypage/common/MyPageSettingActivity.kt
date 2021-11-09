package com.example.albazip.src.mypage.common

import android.content.Intent
import android.os.Bundle
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivitySettingMyPageBinding
import com.example.albazip.src.mypage.custom.LogoutBottomSheetDialog

class MyPageSettingActivity:BaseActivity<ActivitySettingMyPageBinding>(ActivitySettingMyPageBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 구독 신청 화면으로 이동
        binding.rlSubscribeBtn.setOnClickListener {
            val nextIntent = Intent(this, SubScribeActivity::class.java)
            startActivity(nextIntent)
        }

        // 로그아웃 다이얼로그
        binding.rlLogout.setOnClickListener {
            LogoutBottomSheetDialog().show(supportFragmentManager, "logoutAlert")
        }

        // 공지사항 화면
        binding.rlNotice.setOnClickListener {
            val nextIntent = Intent(this, AppNoticeActivity::class.java)
            startActivity(nextIntent)
        }

        // 서비스 이용약관
        binding.rlService.setOnClickListener {
            val nextIntent = Intent(this, ServiceLawActivity::class.java)
            startActivity(nextIntent)
        }

        // 내 정보관리
        binding.rlManageMyinfo.setOnClickListener {
            val nextIntent = Intent(this, ManageMyInfoActivity::class.java)
            startActivity(nextIntent)
        }

        // 회원탈퇴
        binding.rlWithdraw.setOnClickListener {
            val nextIntent = Intent(this, WithDrawMemberActivity::class.java)
            startActivity(nextIntent)
        }
    }
}