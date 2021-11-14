package com.example.albazip.src.mypage.common

import android.graphics.Color
import android.os.Bundle
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivitySettingAlarmBinding

class AlarmSettingActivity:BaseActivity<ActivitySettingAlarmBinding>(ActivitySettingAlarmBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.switchAlarm.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked == true){
                binding.tvAlarmStatus.setTextColor(Color.parseColor("#ffc400"))
                binding.tvAlarmStatus.text = "ON"
            }else{
                binding.tvAlarmStatus.setTextColor(Color.parseColor("#a3a3a3"))
                binding.tvAlarmStatus.text = "OFF"
            }
        }
    }
}