package com.example.albazip.src.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivitySplashBinding
import com.example.albazip.src.main.MainActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            // 자동로그인
            // token 이 존재할 때
            // 기본 상태이면 설정화면으로 이동
            // 관리자 상태이면 관리자 홈으로 바로 이동
            // 근무자 상태이면 근무자 홈으로 바로 이동

            // 로그아웃 상태면
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1500)
    }
}