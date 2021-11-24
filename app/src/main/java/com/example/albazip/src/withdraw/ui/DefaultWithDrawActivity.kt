package com.example.albazip.src.withdraw.ui

import android.content.Intent
import android.os.Bundle
import com.example.albazip.config.ApplicationClass.Companion.prefs
import com.example.albazip.config.BaseActivity
import com.example.albazip.config.BaseResponse
import com.example.albazip.databinding.ActivityWithDrawMemberBinding
import com.example.albazip.src.main.MainActivity
import com.example.albazip.src.splash.SplashActivity
import com.example.albazip.src.withdraw.network.WithDrawFragmentView
import com.example.albazip.src.withdraw.network.WithDrawService

class DefaultWithDrawActivity:BaseActivity<ActivityWithDrawMemberBinding>(ActivityWithDrawMemberBinding::inflate),WithDrawFragmentView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뒤로가기 버튼
        binding.ibtnBack.setOnClickListener {
            finish()
        }

        // 취소하기
        binding.btnCancel.setOnClickListener {
            finish()
        }

        // 탈퇴하기
        binding.btnWithdraw.setOnClickListener {
            WithDrawService(this).tryDeletePersonInfo()
            showLoadingDialog(this)
        }

    }

    override fun onDeleteSuccess(response: BaseResponse) {
        dismissLoadingDialog()
        showCustomToast("탈퇴 성공")

        // 로그인 flag 변경(로그아웃)
        prefs.setInt("loginFlags",0)
        // token 비우기
        prefs.setString("X-ACCESS-TOKEN","")

        // 메인 화면으로 이동
        val mainIntent = Intent(this, SplashActivity::class.java)
        startActivity(mainIntent)
        // 이전 엑티비티 모두 종료
        finishAffinity()
    }

    override fun onDeleteFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }
}