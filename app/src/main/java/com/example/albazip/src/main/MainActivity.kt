package com.example.albazip.src.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import com.example.albazip.R
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityMainBinding
import com.example.albazip.src.login.LoginActivity
import com.example.albazip.src.login.ReInputPhoneActivity
import com.example.albazip.src.register.common.RegisterActivity
import com.example.albazip.util.ScreenToFull
import com.google.android.gms.tasks.OnCompleteListener

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