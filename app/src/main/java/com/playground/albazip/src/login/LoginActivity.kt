package com.playground.albazip.src.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.playground.albazip.R
import com.playground.albazip.config.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.playground.albazip.config.ApplicationClass.Companion.prefs
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityLoginBinding
import com.playground.albazip.src.home.common.ui.HomeActivity
import com.playground.albazip.src.login.data.PostSignInRequest
import com.playground.albazip.src.login.data.SignInResponse
import com.playground.albazip.src.login.network.SignInFragmentView
import com.playground.albazip.src.login.network.SignInService
import com.playground.albazip.src.main.ManagerMainActivity
import com.playground.albazip.src.main.WorkerMainActivity
import com.playground.albazip.src.onboard.manager.ManagerOnBoardingActivity
import com.playground.albazip.src.onboard.worker.WorkerOnBoardingActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate),
    SignInFragmentView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            finish()
        }

        // focus 감지
        onFocus()

        var _beforeLength: Int = 0
        var _afterLength: Int = 0

        // 휴대폰 번호 입력 (자동 띄어씌기)
        binding.etInputPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                _beforeLength = s!!.length
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                // 텍스트 크기 동적 변경
                if (s!!.isEmpty()) {
                    binding.etInputPhone.typeface =
                        ResourcesCompat.getFont(this@LoginActivity, R.font.roboto_medium_otf)
                    binding.etInputPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
                } else {
                    binding.etInputPhone.typeface =
                        ResourcesCompat.getFont(this@LoginActivity, R.font.roboto_bold_otf)
                    binding.etInputPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
                }

                _afterLength = s.length

                // 삭제중
                if (_beforeLength > _afterLength) {
                    // 삭제 중에 마지막에 -는 자동으로 지우기
                    if (s.toString().endsWith(" ")) {
                        binding.etInputPhone.setText(s.toString().substring(0, s.length - 1))
                    }
                }

                // 입력중
                else if (_beforeLength < _afterLength) {
                    if (_afterLength == 5 && s.toString().indexOf("-") < 0) {
                        binding.etInputPhone.setText(
                            s.toString().substring(0, 3) + " " + s.toString().substring(3, s.length)
                        )
                    } else if (_afterLength == 10) {
                        binding.etInputPhone.setText(
                            s.toString().substring(0, 8) + " " + s.toString().substring(8, s.length)
                        )
                    } else if (_afterLength == 15) {
                        binding.etInputPhone.setText(
                            s.toString().substring(0, 13) + " " + s.toString()
                                .substring(13, s.length)
                        )
                    }
                }
                binding.etInputPhone.setSelection(binding.etInputPhone.length())

                // 휴대폰 번호 입력완료시 전송 버튼 활성화
                if (s.length == 13) { // 활성화
                    binding.btnNext.isEnabled = true
                    binding.btnNext.background =
                        ContextCompat.getDrawable(
                            this@LoginActivity,
                            R.drawable.btn_main_yellow_fill_rounded
                        )
                    binding.btnNext.setTextColor(Color.parseColor("#343434"))
                } else { // 비활성화
                    binding.btnNext.isEnabled = false
                    binding.btnNext.background =
                        ContextCompat.getDrawable(
                            this@LoginActivity,
                            R.drawable.btn_disable_yellow_fill_rounded
                        )
                    binding.btnNext.setTextColor(Color.parseColor("#adadad"))
                }

            }

            override fun afterTextChanged(s: Editable?) {}

        })

        // 로그인하기
        binding.btnNext.setOnClickListener {

            // 비밀번호 입력란이 공백일 때
            if (binding.etPw.text.toString().isEmpty()) {
                showCustomToast("비밀번호를 입력해주세요.")
            }

            // 비밀번호 입력란이 공백이 아닐 때
            if (binding.etPw.text.toString().isNotEmpty()) {
                val phone = binding.etInputPhone.text.toString().replace(" ", "")
                val postRequest =
                    PostSignInRequest(phone = phone, pwd = binding.etPw.text.toString())
                showLoadingDialog(this@LoginActivity)

                SignInService(this).tryPostLogin(postRequest)
            }

        }

        // 비밀번호 변경
        binding.tvChangePw.setOnClickListener {
            val nextIntent = Intent(this, ReInputPhoneActivity::class.java)
            startActivity(nextIntent)
        }
    }

    // focus 감지
    fun onFocus() {
        binding.etInputPhone.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.rlPhoneInput.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectagnle_yellow_radius
                ) else {
                binding.rlPhoneInput.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectangle_custom_white_radius
                )
            }
        }

        binding.etPw.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.rlPw.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectagnle_yellow_radius
                ) else {
                binding.rlPw.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectangle_custom_white_radius
                )
            }
        }
    }


    override fun onPostSingInSuccess(response: SignInResponse) {
        dismissLoadingDialog()
        when (response.code) {
            200 -> { // 로그인 성공

                // jwt 토큰 header 에 저장
                prefs.setString(X_ACCESS_TOKEN, response.data.token.token)

                // login 상태 저장
                prefs.setInt("loginFlags", 1)

                // job 상태 저장
                prefs.setInt("jobFlags", response.data.job)

                // 토큰 등록하기
                if (response.data.job == 0) { // 기본 가입만 완료된 상태일 때
                    val nextIntent = Intent(this@LoginActivity, HomeActivity::class.java)
                    prefs.setString("userFirstName", response.data.userFirstName) // 유저이름 받아오기
                    startActivity(nextIntent)
                    finishAffinity()
                } else if (response.data.job == 1) { // 관리자 가입이 완료된 상태일 때

                    // 온보딩 pref 상태 받기
                    val mBoardingFlags = prefs.getInt("mBoardingFlags", 0)

                    if (mBoardingFlags == 0) { // 온보딩 확인이 안된 기종이라면
                        startActivity(
                            Intent(
                                this@LoginActivity,
                                ManagerOnBoardingActivity::class.java
                            )
                        ) // 온보딩 확인
                    } else { // 관리자 메인으로로 바로 이동
                        startActivity(Intent(this@LoginActivity, ManagerMainActivity::class.java))
                    }
                    finishAffinity()
                } else { // 근무자 가입이 완료된 상태일 때

                    // 온보딩 pref 상태 받기
                    val wBoardingFlags = prefs.getInt("wBoardingFlags", 0)

                    if (wBoardingFlags == 0) { // 온보딩 확인이 안된 기종이라면
                        startActivity(
                            Intent(
                                this@LoginActivity,
                                WorkerOnBoardingActivity::class.java
                            )
                        ) // 온보딩 확인
                    } else { // 근무자 메인으로 바로 이동
                        startActivity(Intent(this@LoginActivity, WorkerMainActivity::class.java))
                    }
                    finishAffinity()
                }

            }

            202 -> {
                if (response.message == "존재하지 않는 계정입니다.") {
                    showCustomToast("존재하지 않는 계정입니다.")
                } else if (response.message == "비밀번호가 다릅니다.") {
                    showCustomToast("비밀번호가 다릅니다.")
                } else {
                    showCustomToast("휴대폰번호와 비밀번호를 입력해주세요.")
                }
            }
        }
    }

    override fun onPostSignInFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast("로그인 실패")
    }
}