package com.playground.albazip.src.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityMainBinding
import com.playground.albazip.src.login.LoginActivity
import com.playground.albazip.src.login.ReInputPhoneActivity
import com.playground.albazip.src.register.common.RegisterActivity

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate){

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        // 시스템바 색상 변경
        window.statusBarColor = Color.parseColor("#ffffff")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 전체화면으로 만들기 (상태바 제거)
        // ScreenToFull().fullScreen(this)

        // 회원가입 화면으로 이동
        binding.btnRegister.setOnClickListener {
            val nextIntent = Intent(this, RegisterActivity::class.java)
            startActivity(nextIntent)
        }

        // 로그인 화면으로 이동
        binding.btnLogin.setOnClickListener {
            val nextIntent = Intent(this, LoginActivity::class.java)
            startActivity(nextIntent)
        }

        // 비밀번호 변경 화면으로 이동
        binding.tvPwSet.setOnClickListener {
            val nextIntent = Intent(this,ReInputPhoneActivity::class.java)
            startActivity(nextIntent)
        }
    }

}