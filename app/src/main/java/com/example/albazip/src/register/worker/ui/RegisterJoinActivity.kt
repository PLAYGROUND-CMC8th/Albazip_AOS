package com.example.albazip.src.register.worker.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.example.albazip.R
import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityRegisterJoinBinding
import com.example.albazip.src.main.WorkerMainActivity
import com.example.albazip.src.register.common.data.remote.PositionRegisterResponse
import com.example.albazip.src.register.worker.data.PostSignInWorkerRequest
import com.example.albazip.src.register.worker.network.WSignUpFragmentView
import com.example.albazip.src.register.worker.network.WSignUpService

class RegisterJoinActivity:BaseActivity<ActivityRegisterJoinBinding>(ActivityRegisterJoinBinding::inflate),WSignUpFragmentView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 근무자 가입하기
        binding.btnWorkerRegister.setOnClickListener {

            val postRequest = PostSignInWorkerRequest(
                code = binding.etWorkerCode.text.toString()
            )

            showLoadingDialog(this)

            WSignUpService(this).tryPostNewPW(postRequest)
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
                    binding.btnWorkerRegister.isEnabled = true
                    binding.btnWorkerRegister.background =
                        ContextCompat.getDrawable(this@RegisterJoinActivity, R.drawable.btn_main_yellow_fill_rounded)
                    binding.btnWorkerRegister.setTextColor(Color.parseColor("#343434"))
                }else{
                    // 버튼 비활성화
                    binding.btnWorkerRegister.isEnabled = false
                    binding.btnWorkerRegister.background =
                        ContextCompat.getDrawable(this@RegisterJoinActivity, R.drawable.btn_disable_yellow_fill_rounded)
                    binding.btnWorkerRegister.setTextColor(Color.parseColor("#ADADAD"))
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    override fun onWorkerPostSuccess(response: PositionRegisterResponse) {
        dismissLoadingDialog()

        if(response.code == 200) {
            // 토큰 갱신
            ApplicationClass.prefs.setString(
                ApplicationClass.X_ACCESS_TOKEN,
                response.tokenData.token
            )

            val wBoardingFlags = ApplicationClass.prefs.getInt("wBoardingFlags", 0)
            // 저장된 Flag값이 0이면 온보딩
            if (wBoardingFlags == 0) {
                ApplicationClass.prefs.setInt("jobFlags", 2)
                val nextIntent = Intent(this, WorkerOnBoardingActivity::class.java)
                startActivity(nextIntent)
                finishAffinity()

            } else { // 저장된 Flag 1이면 관리자 홈으로 바로 이동
                ApplicationClass.prefs.setInt("jobFlags", 2)
                val nextIntent = Intent(this, WorkerMainActivity::class.java)
                startActivity(nextIntent)
                finishAffinity()
            }

            showCustomToast("근무자 가입 성공")
        }
    }

    override fun onWorkerPostFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }
}