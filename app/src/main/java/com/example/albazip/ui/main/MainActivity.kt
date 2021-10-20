package com.example.albazip.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityMainBinding
import com.example.albazip.ui.login.LoginActivity
import com.example.albazip.ui.register.common.RegisterActivity
import com.example.albazip.util.ScreenToFull

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 전체화면으로 만들기 (상태바 제거)
        ScreenToFull().fullScreen(this)

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
    }


}