package com.playground.albazip.src.mypage.common.setting.editinfo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.playground.albazip.R
import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.databinding.ActivityEditPhoneNumBinding
import com.playground.albazip.src.main.MainActivity
import com.playground.albazip.src.mypage.common.setting.editinfo.network.EditPhoneNumFragmentView
import com.playground.albazip.src.mypage.common.setting.editinfo.network.EditPhoneNumRequest
import com.playground.albazip.src.mypage.common.setting.editinfo.network.EditPhoneNumService
import com.playground.albazip.src.register.common.data.remote.PhoneCheckResponse
import com.playground.albazip.src.register.common.network.PhoneCheckFragmentView
import com.playground.albazip.src.register.common.network.PhoneCheckService
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class EditPhoneNumActivity :
    BaseActivity<ActivityEditPhoneNumBinding>(ActivityEditPhoneNumBinding::inflate),
    PhoneCheckFragmentView, EditPhoneNumFragmentView {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var credential: PhoneAuthCredential

    // 인증 콜백 함수
    private val callbacks by lazy {
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            // 사용자 UI 입력 대기
            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)

                // 여기서 타이머 대기
                showCustomToast("입력시간 초과")

                // 버튼 비활성화
                binding.tvDone.isEnabled = false
                binding.tvDone.setTextColor(Color.parseColor("#adadad"))
            }

            // 다른 기타 인증이 완료된 상태
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            }

            // 번호 인증 실패 상태
            override fun onVerificationFailed(e: FirebaseException) {
                //  Log.w(ReInputPhoneActivity.TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            // 전화번호는 확인되었으나 코드 인증이 필요한 상태
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // Log.d(ReInputPhoneActivity.TAG, "onCodeSent:$verificationId")
                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
            }
        }
        // [END phone_auth_callbacks]

    }

    // 버튼 활성화 상태
    var btnEnabled: Boolean = false

    // 타이머 설정
    var timer = 120

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]

        // 기본 정보 입력 화면으로 이동////////////////////////////////////////////////////////////////////
        binding.tvDone.setOnClickListener {

            // 인증번호 입력
            if (binding.etCertify.text.toString().isNotEmpty()) {

                verifyPhoneNumberWithCode(storedVerificationId, binding.etCertify.text.toString())
            }

        }

        // 뒤로가기 버튼
        binding.ibtnClose.setOnClickListener {
            mCountDown.cancel()
            finish()
        }

        // focus 감지
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

        binding.etCertify.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.rlCertify.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectagnle_yellow_radius
                ) else {
                binding.rlCertify.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectangle_custom_white_radius
                )
            }
        }

        // 버튼 활성화여부
        binding.etCertify.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 6) { // 버튼활성화
                    binding.tvDone.isEnabled = true
                    binding.tvDone.setTextColor(Color.parseColor("#ffc400"))
                } else { // 버튼비활성화
                    binding.tvDone.isEnabled = false
                    binding.tvDone.setTextColor(Color.parseColor("#adadad"))
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })

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
                        ResourcesCompat.getFont(this@EditPhoneNumActivity, R.font.roboto_medium_otf)
                    binding.etInputPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
                } else {
                    binding.etInputPhone.typeface =
                        ResourcesCompat.getFont(this@EditPhoneNumActivity, R.font.roboto_bold_otf)
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
                    binding.rlClickableCertify.isEnabled = true
                    binding.tvCertify.setTextColor(Color.parseColor("#343434"))
                } else { // 비활성화
                    binding.rlClickableCertify.isEnabled = false
                    binding.tvCertify.setTextColor(Color.parseColor("#adadad"))
                }

            }

            override fun afterTextChanged(s: Editable?) {}

        })

        // 인증하기 버튼 클릭 시 (메세지 전송)
        binding.rlClickableCertify.setOnClickListener {

            // edittext에 있는 번호를 받아온다.
            val getPhoneNum = binding.etInputPhone.text.toString().replace(" ", "")
            val inputPhoneNum = "+82 " + getPhoneNum

            // 휴대폰 번호 중복 체크
            showLoadingDialog(this)
            PhoneCheckService(this).tryGetPhoneCheck(getPhoneNum, inputPhoneNum)
        }

        // 재전송 버튼
        binding.rlClickableResend.setOnClickListener {
            val getPhoneNum = binding.etInputPhone.text.toString().replace(" ", "")
            val inputPhoneNum = "+82 " + getPhoneNum

            showCustomToast("인증번호가 재전송되었습니다.")

            // 카운트 다운 재시작
            mCountDown.cancel()
            timer = 120
            mCountDown.start()

            // 인증번호 재전송
            resendVerificationCode(inputPhoneNum, resendToken)
        }
    }

    // 버튼 상태 저장
    override fun onPause() {
        super.onPause()
        btnEnabled = binding.tvDone.isEnabled == true
    }


    // 버튼 상태 반환(화면 돌아왔을 때)
    override fun onResume() {
        super.onResume()

        if (btnEnabled == true) {
            binding.rlClickableCertify.isEnabled = true
            binding.tvCertify.setTextColor(Color.parseColor("#343434"))
        } else {
            binding.rlClickableCertify.isEnabled = false
            binding.tvCertify.setTextColor(Color.parseColor("#adadad"))
        }
    }

    // 카운트 다운 함수
    private val mCountDown: CountDownTimer = object : CountDownTimer(120000, 1000) {
        override fun onTick(p0: Long) {

            if (timer == 120) {
                binding.tvCountDown.text = "2:00"
                timer--
            } else if (timer >= 70) {
                binding.tvCountDown.text = "1:${timer - 60}"
                timer--
            } else if (timer > 59) {
                binding.tvCountDown.text = "1:0${timer - 60}"
                timer--
            } else if (timer >= 10) {
                binding.tvCountDown.text = "0:${timer}"
                timer--
            } else { // "ui" 적으로 통일성을 맞추기 위함
                timer--
                binding.tvCountDown.text = "0:0${timer}"

                if (binding.tvCountDown.text == "0:00") {
                    onFinish()
                }
            }
        }

        // 타이머가 종료되면 재인증이나 뒤로가기를 대비하기 위해
        // 타이머 초기화
        override fun onFinish() {
            timer = 120
        }
    }

    // 화면 전환 시 타이머 초기화 및 정지
    override fun onDestroy() {
        mCountDown.cancel()
        timer = 120
        super.onDestroy()
    }

    // 핸드폰 인증 시작 함수
    private fun startPhoneNumberVerification(phoneNumber: String) {
        showCustomToast("인증번호가 전송되었습니다.")
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this@EditPhoneNumActivity)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }

    // 인증번호 확인
    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]

        // 계정 생성
        signInWithPhoneAuthCredential(credential)
    }

    // 인증번호 재전송 함수
    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this@EditPhoneNumActivity)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    // [START sign_in_with_phone] // 아이디 생성
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this@EditPhoneNumActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    task.result?.user

                    // 타이머 정지
                    mCountDown.cancel()

                    // 파베 계정 삭제 (더미데이터 삭제)
                    deleteAccount()

                } else {
                    // 계정 생성 실패
                    // Sign in failed, display a message and update the UI
                    showCustomToast("인증 실패")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }


            }
    }

    private fun deleteAccount() {

        val user = Firebase.auth.currentUser!!

        user.delete().addOnCompleteListener {
            if (it.isSuccessful) {
                // 유저 전화번호 prefs
                ApplicationClass.prefs.setString(
                    "phone",
                    binding.etInputPhone.text.toString().replace(" ", "")
                )

                // 서버 통신 시작
                val postRequest = EditPhoneNumRequest(
                    phone = binding.etInputPhone.text.toString().replace(" ", "")
                )
                EditPhoneNumService(this).tryEditInfo(postRequest)
                showLoadingDialog(this)

            } else {
                Toast.makeText(this@EditPhoneNumActivity, "유저정보 삭제 실패 ", Toast.LENGTH_LONG).show()
            }
        }
    }

    // 중복체크 api
    override fun onGetCheckSuccess(
        response: PhoneCheckResponse,
        serverCheckPhoneNum: String,
        inputPhoneNum: String
    ) {
        dismissLoadingDialog()

        binding.rlCertify.visibility = View.VISIBLE

        // 인증 메세지 전송하기
        startPhoneNumberVerification(inputPhoneNum)

        // 카운트 다운 시작
        if (timer != 120) { // 재전송 후 전송 클릭 시 동작 버튼
            mCountDown.cancel()
            timer = 120
        }
        mCountDown.start()
        // 다음(인증)버튼 활성화
        binding.tvDone.isEnabled = true

        // 성공 시
        // 휴대폰 입력란과 인증버튼 비활성화
        binding.etInputPhone.isEnabled = false
        binding.rlClickableCertify.isEnabled = false
        binding.tvCertify.setTextColor(Color.parseColor("#cecece"))
    }

    override fun onGetCheckfailure(message: String) {
        dismissLoadingDialog()
        Log.d("error", message)
    }

    // 휴대폰 번호 변경 성공!
    override fun onEditPhoneNumPostSuccess(response: BaseResponse) {
        dismissLoadingDialog()
        // 화면 전환
        showCustomToast("전화번호 변경 성공")
        // 로그인 flag 변경(로그아웃)
        ApplicationClass.prefs.setInt("loginFlags",0)
        // token 비우기
        ApplicationClass.prefs.setString("X-ACCESS-TOKEN","")

        // 메인 화면으로 이동
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        // 이전 엑티비티 모두 종료
        finishAffinity()
    }

    override fun onEditPhoneNumFailure(message: String) {
        dismissLoadingDialog()
    }


}