package com.example.albazip.src.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.albazip.config.ApplicationClass.Companion.prefs
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivitySplashBinding
import com.example.albazip.src.home.common.HomeActivity
import com.example.albazip.src.main.MainActivity
import com.example.albazip.src.main.ManagerMainActivity
import com.example.albazip.src.main.WorkerMainActivity
import com.example.albazip.src.register.common.RegisterActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //로그인 상태 불러오기
        val loginFlags = prefs.getInt("loginFlags", 0)

        // login 상태면 -> job 상태 불어오기
        val jobFlags = prefs.getInt("jobFlags", 0)

        Handler(Looper.getMainLooper()).postDelayed({
            // 자동로그인
            if (loginFlags == 1) {

                when (jobFlags) {
                    // 기본 login -> 포지션 가입 화면으로 이동
                    0 -> {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }

                    // 관리자 login -> 관리자 home
                    1 -> {
                        startActivity(Intent(this, ManagerMainActivity::class.java))
                        finish()
                    }

                    // 근무자 login -> 근무자 home
                    2 -> {
                        startActivity(Intent(this, WorkerMainActivity::class.java))
                        finish()
                    }
                }

            } else { // 로그아웃 상태면 -> 초기화면으로 이동
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, 1500)
    }
}