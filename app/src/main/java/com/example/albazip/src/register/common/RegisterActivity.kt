package com.example.albazip.src.register.common

import android.os.Bundle
import com.example.albazip.R
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityRegisterBinding

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(ActivityRegisterBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 약관동의 화면 fragment 올리기
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, AgreementFragment()).commitAllowingStateLoss()
    }
}