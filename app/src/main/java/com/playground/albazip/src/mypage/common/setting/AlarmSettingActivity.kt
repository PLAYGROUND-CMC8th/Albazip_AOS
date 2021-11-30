package com.playground.albazip.src.mypage.common.setting

import android.graphics.Color
import android.os.Bundle
import com.playground.albazip.config.ApplicationClass.Companion.prefs
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivitySettingAlarmBinding

class AlarmSettingActivity:BaseActivity<ActivitySettingAlarmBinding>(ActivitySettingAlarmBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.ibtnClose.setOnClickListener {
            finish()
        }

        val alarmCheckState = prefs.getInt("alarmState",0)

        if(alarmCheckState == 0){ // off
            binding.tvAlarmStatus.setTextColor(Color.parseColor("#a3a3a3"))
            binding.switchAlarm.isChecked = false
            binding.tvAlarmStatus.text = "OFF"
        }else{
            binding.tvAlarmStatus.setTextColor(Color.parseColor("#ffc400"))
            binding.tvAlarmStatus.text = "ON"
            binding.switchAlarm.isChecked = true
        }

        binding.switchAlarm.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked == true){
                binding.tvAlarmStatus.setTextColor(Color.parseColor("#ffc400"))
                binding.tvAlarmStatus.text = "ON"
                prefs.setInt("alarmState",1)

            }else{
                binding.tvAlarmStatus.setTextColor(Color.parseColor("#a3a3a3"))
                binding.tvAlarmStatus.text = "OFF"
                prefs.setInt("alarmState",0)
            }
        }
    }
}