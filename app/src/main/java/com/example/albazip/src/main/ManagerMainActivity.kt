package com.example.albazip.src.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.albazip.R
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityManagerMainBinding

// 관리자 시작화면
class ManagerMainActivity :
    BaseActivity<ActivityManagerMainBinding>(ActivityManagerMainBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}