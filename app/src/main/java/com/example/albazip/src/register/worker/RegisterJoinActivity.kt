package com.example.albazip.src.register.worker

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.example.albazip.R
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityRegisterJoinBinding

class RegisterJoinActivity:BaseActivity<ActivityRegisterJoinBinding>(ActivityRegisterJoinBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            finish()
        }

        // focus 감지
        binding.etWorkerCode.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.rlWorkerCode.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectagnle_yellow_radius
                ) else {
                binding.rlWorkerCode.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectangle_custom_white_radius
                )
            }
        }

        // 텍스트 굵기 및 버튼 활성화 여부 설정
        binding.etWorkerCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s?.length!! >= 1){
                    // 텍스트 색 변경
                    binding.etWorkerCode.setTextColor(Color.parseColor("#343434"))
                    // 텍스트 굵기 변경
                    binding.etWorkerCode.setTypeface(null,Typeface.BOLD)

                }else{

                    // 텍스트 색 변경
                    binding.etWorkerCode.setTextColor(Color.parseColor("#c8c8c8"))
                    // 텍스트 굵기 변경
                    binding.etWorkerCode.setTypeface(null,Typeface.NORMAL)
                }

                if(s?.length == 9){
                    // 버튼 활성화
                    binding.btnNext.isEnabled = true
                    binding.btnNext.background =
                        ContextCompat.getDrawable(this@RegisterJoinActivity, R.drawable.btn_main_yellow_fill_rounded)
                    binding.btnNext.setTextColor(Color.parseColor("#343434"))
                }else{
                    // 버튼 비활성화
                    binding.btnNext.isEnabled = false
                    binding.btnNext.background =
                        ContextCompat.getDrawable(this@RegisterJoinActivity, R.drawable.btn_disable_yellow_fill_rounded)
                    binding.btnNext.setTextColor(Color.parseColor("#ADADAD"))
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }
}