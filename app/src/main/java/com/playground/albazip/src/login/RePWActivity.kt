package com.playground.albazip.src.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import com.playground.albazip.R
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.databinding.ActivityResetPwBinding
import com.playground.albazip.src.login.data.PostNewPWRequest
import com.playground.albazip.src.login.network.ChangePWFragmentView
import com.playground.albazip.src.login.network.ChangePWService
import com.playground.albazip.src.main.MainActivity

class RePWActivity : BaseActivity<ActivityResetPwBinding>(
    ActivityResetPwBinding::inflate
), ChangePWFragmentView {

    var btnEnabled: Boolean = false

    var firstFlags: Boolean = false
    var secondFlags: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 이전 화면으로 이동(번호 입력)
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 비밀번호 변경 완료
        binding.btnNext.setOnClickListener {
            val postRequest = PostNewPWRequest(phone=intent.getStringExtra("phoneNum")!!, pwd=binding.etConfirmPw.text.toString())
            ChangePWService(this).tryPostNewPW(postRequest)
            showLoadingDialog(this)
        }

        // 포커스 여부 감지
        binding.etInputPw.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.rlPwInput.background = ContextCompat.getDrawable(
                    this@RePWActivity,
                    R.drawable.rectagnle_yellow_radius
                ) else {
                binding.rlPwInput.background = ContextCompat.getDrawable(
                    this@RePWActivity,
                    R.drawable.rectangle_custom_white_radius
                )
            }
        }

        // 포커스 여부 감지
        binding.etConfirmPw.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && binding.etConfirmPw.text.toString().isEmpty())
                binding.rlConfirmPw.background =
                    ContextCompat.getDrawable(this@RePWActivity, R.drawable.rectagnle_yellow_radius)
            else if (hasFocus && binding.etInputPw.text.toString() == binding.etConfirmPw.text.toString()) {
                binding.rlConfirmPw.background =
                    ContextCompat.getDrawable(this@RePWActivity, R.drawable.rectagnle_yellow_radius)
            } else {
                binding.rlConfirmPw.background = ContextCompat.getDrawable(
                    this@RePWActivity,
                    R.drawable.rectangle_custom_white_radius
                )
            }
        }

        // 비밀번호 정상 입력 감지
        binding.etInputPw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                // 비밀번호 입력란 자릿수 감지
                if (binding.etInputPw.text?.length!! >= 6) { // 6자리 이상 입력 되었을 때
                    binding.ivInputCheck.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@RePWActivity,
                            R.drawable.ic_checked_correct
                        )
                    )
                    firstFlags = true
                } else {
                    binding.ivInputCheck.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@RePWActivity,
                            R.drawable.ic_checked_normal
                        )
                    )
                    firstFlags = false
                }
            }

            // 비밀번호 일치여부 감지(1)
            override fun afterTextChanged(s: Editable?) {
                if (binding.etConfirmPw.text.toString()
                        .isNotEmpty() && binding.etInputPw.text.toString().isNotEmpty()
                ) { // 확인란이 공백이 아닐 때
                    binding.rlConfirmPw.background =
                        ContextCompat.getDrawable(
                            this@RePWActivity,
                            R.drawable.rectangle_custom_white_radius
                        )

                    if (binding.etInputPw.text.toString() != binding.etConfirmPw.text.toString()) { // 일치하지 않을 때
                        binding.rlConfirmPw.background = ContextCompat.getDrawable(
                            this@RePWActivity,
                            R.drawable.rectagnle_red_radius
                        )
                        binding.ivConfirmCheck.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@RePWActivity,
                                R.drawable.ic_checked_normal
                            )
                        )
                        binding.warningTv.visibility = View.VISIBLE
                        secondFlags = false

                    } else {
                        binding.rlConfirmPw.background = ContextCompat.getDrawable(
                            this@RePWActivity,
                            R.drawable.rectangle_custom_white_radius
                        )
                        binding.ivConfirmCheck.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@RePWActivity,
                                R.drawable.ic_checked_correct
                            )
                        )

                        binding.warningTv.visibility = View.INVISIBLE
                        secondFlags = true
                    }
                }

                allChecked()
            }
        })

        // 비밀번호 일치 여부 감지(2)
        binding.etConfirmPw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) { // 불일치
                if (binding.etConfirmPw.text.toString().isNotEmpty()) { // 공백이 아닐 때
                    if (binding.etInputPw.text.toString() != binding.etConfirmPw.text.toString()) { // 불일치
                        binding.rlConfirmPw.background =
                            ContextCompat.getDrawable(
                                this@RePWActivity,
                                R.drawable.rectagnle_red_radius
                            )
                        binding.ivConfirmCheck.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@RePWActivity,
                                R.drawable.ic_checked_normal
                            )
                        )

                        binding.warningTv.visibility = View.VISIBLE
                        secondFlags = false

                    } else { // 일치
                        binding.rlConfirmPw.background = ContextCompat.getDrawable(
                            this@RePWActivity,
                            R.drawable.rectagnle_yellow_radius
                        )
                        binding.ivConfirmCheck.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@RePWActivity,
                                R.drawable.ic_checked_correct
                            )
                        )
                        binding.warningTv.visibility = View.INVISIBLE
                        secondFlags = true

                    }
                } else { // 공백일 때
                    binding.warningTv.visibility = View.INVISIBLE
                }
                allChecked()
            }
        })
    }

    // 버튼 활성화 함수
    private fun activateBtn() {
        binding.btnNext.isEnabled = true
        binding.btnNext.background =
            ContextCompat.getDrawable(this@RePWActivity, R.drawable.btn_main_yellow_fill_rounded)
        binding.btnNext.setTextColor(Color.parseColor("#343434"))
    }

    // 버튼 비활성화 함수
    private fun deActivateBtn() {
        binding.btnNext.isEnabled = false
        binding.btnNext.background =
            ContextCompat.getDrawable(this@RePWActivity, R.drawable.btn_disable_yellow_fill_rounded)
        binding.btnNext.setTextColor(Color.parseColor("#adadad"))
    }

    private fun allChecked() {
        if (firstFlags == true && secondFlags == true) {
            activateBtn()
        } else {
            deActivateBtn()
        }
    }

    // 버튼 상태 저장
    override fun onPause() {
        super.onPause()
        btnEnabled = binding.btnNext.isEnabled == true
    }

    // 버튼 상태 반환(화면 돌아왔을 때)
    override fun onResume() {
        super.onResume()

        if (btnEnabled == true) {
            activateBtn()
        } else {
            deActivateBtn()
        }
    }

    override fun onNewPWPostSuccess(response: BaseResponse) {
        dismissLoadingDialog()
        showCustomToast("비밀번호 변경 완료")
        val nextIntent = Intent(this, MainActivity::class.java)
        startActivity(nextIntent)
        finishAffinity()
    }

    override fun onNewPWPostFailure(message: String) {
        dismissLoadingDialog()
    }


}