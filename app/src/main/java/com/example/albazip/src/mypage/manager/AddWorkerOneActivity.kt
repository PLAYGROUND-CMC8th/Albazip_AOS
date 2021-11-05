package com.example.albazip.src.mypage.manager

import android.os.Bundle
import android.view.View
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityAddWorkerOneBinding

class AddWorkerOneActivity:BaseActivity<ActivityAddWorkerOneBinding>(ActivityAddWorkerOneBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.tvNext.setOnClickListener {
            binding.clVisibleLayout.visibility = View.VISIBLE
        }

        binding.btnAlba.setOnClickListener {
            binding.clVisibleLayout.visibility = View.GONE
        }
    }
}