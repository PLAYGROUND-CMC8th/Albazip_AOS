package com.example.albazip.src.register.manager

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.albazip.R
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityInputPlaceMoreBinding

class InputPlaceMoreActivity :
    BaseActivity<ActivityInputPlaceMoreBinding>(ActivityInputPlaceMoreBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 온보딩 화면으로 이동
        binding.btnNext.setOnClickListener {
            val nextIntent = Intent()
            startActivity(nextIntent)
            finish()
        }
    }
}