package com.playground.albazip.src.register.manager.moreinfo

import android.content.Intent
import android.os.Bundle
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityInputPlaceMoreBetaBinding
import com.playground.albazip.src.update.runtime.UpdateRunningTimeActivity

class InputPlaceMoreBetaActivity: BaseActivity<ActivityInputPlaceMoreBetaBinding>(ActivityInputPlaceMoreBetaBinding::inflate){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initRunningTimeBtn()
    }

    // 매장영업시간 설정 화면으로 이동
    private fun initRunningTimeBtn(){
        binding.tvInputPlaceSetRunTimeBtn.setOnClickListener {
            startActivity(Intent(this, UpdateRunningTimeActivity::class.java))
        }
    }
}