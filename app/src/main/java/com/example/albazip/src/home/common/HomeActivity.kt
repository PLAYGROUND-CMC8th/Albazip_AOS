package com.example.albazip.src.home.common

import android.content.Intent
import android.os.Bundle
import com.example.albazip.config.ApplicationClass.Companion.prefs
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityHomeBinding
import com.example.albazip.src.register.manager.SearchPlaceActivity
import com.example.albazip.src.register.worker.ui.RegisterJoinActivity

class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 유저 이름 받아오기
        val userFirstName = prefs.getString("userFirstName","유저")
        binding.tvWelcome.text = userFirstName + "님 반가워요 :)\n포지션을 선택해주세요!"

        // 설정 창 띄우기
        binding.ibtnSetting.setOnClickListener {
            val nextIntent = Intent(this,SettingActivity::class.java)
            startActivity(nextIntent)
        }

        // 관리자 가입
        binding.clManager.setOnClickListener {
            val nextIntent = Intent(this,SearchPlaceActivity::class.java)
            startActivity(nextIntent)
        }

        // 근무자 가입
        binding.clWorker.setOnClickListener {
            val nextIntent = Intent(this, RegisterJoinActivity::class.java)
            startActivity(nextIntent)
        }

    }


}