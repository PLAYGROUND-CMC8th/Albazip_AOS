package com.playground.albazip.src.register.common

import android.os.Bundle
import com.playground.albazip.R
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityRegisterBinding

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(ActivityRegisterBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 약관동의 화면 fragment 올리기
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, AgreementFragment()).commitAllowingStateLoss()
    }


}