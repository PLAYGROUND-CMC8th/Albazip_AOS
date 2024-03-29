package com.playground.albazip.src.register.common

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
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.FragmentInputPhoneBinding
import com.playground.albazip.src.register.common.data.remote.PhoneCheckResponse
import com.playground.albazip.src.register.common.network.PhoneCheckFragmentView
import com.playground.albazip.src.register.common.network.PhoneCheckService
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.playground.albazip.src.register.common.network.CommonSignUpService
import com.playground.albazip.src.update.runtime.network.RegisterService
import com.playground.albazip.util.enqueueUtil
import java.util.concurrent.TimeUnit

class InputPhoneFragment : BaseFragment<FragmentInputPhoneBinding>(
    FragmentInputPhoneBinding::bind,
    R.layout.fragment_input_phone
), PhoneCheckFragmentView {

    companion object {
        private const val TAG = "PhoneAuthActivity"
    }

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var credential: PhoneAuthCredential

    // 인증 콜백 함수
    private val callbacks by lazy {
        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            // 사용자 UI 입력 대기
            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)

                // 여기서 타이머 대기
                showCustomToast("입력시간 초과")
                Log.d(TAG, "입력시간 초과")

                // 버튼 비활성화
                binding.btnNext.isEnabled = false
                binding.btnNext.background =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.btn_disable_yellow_fill_rounded
                    )
                binding.btnNext.setTextColor(Color.parseColor("#adadad"))
            }

            // 다른 기타 인증이 완료된 상태
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            }

            // 번호 인증 실패 상태
            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)

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
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // log - [전화번호를 입력해주세요] 진입
        ApplicationClass.firebaseAnalytics.logEvent("signupPhoneNumber", null)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]


        // 기본 정보 입력 화면으로 이동
        binding.btnNext.setOnClickListener {

            // 인증번호 입력
            if (binding.etCertify.text.toString().isNotEmpty()) {

                verifyPhoneNumberWithCode(storedVerificationId, binding.etCertify.text.toString())
                showLoadingDialog(requireContext())
            }

        }

        // 뒤로가기 버튼(약관동의)
        binding.btnBack.setOnClickListener {
            mCountDown.cancel()
            activity?.supportFragmentManager?.popBackStack()
        }

        // focus 감지
        binding.etInputPhone.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.rlPhoneInput.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.rectagnle_yellow_radius
                ) else {
                binding.rlPhoneInput.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.rectangle_custom_white_radius
                )
            }
        }

        binding.etCertify.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.rlCertify.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.rectagnle_yellow_radius
                ) else {
                binding.rlCertify.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.rectangle_custom_white_radius
                )
            }
        }

        // 버튼 활성화여부
        binding.etCertify.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 6) { // 버튼활성화
                    binding.btnNext.isEnabled = true
                    binding.btnNext.background =
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.btn_main_yellow_fill_rounded
                        )
                    binding.btnNext.setTextColor(Color.parseColor("#343434"))
                } else { // 버튼비활성화
                    binding.btnNext.isEnabled = false
                    binding.btnNext.background =
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.btn_disable_yellow_fill_rounded
                        )
                    binding.btnNext.setTextColor(Color.parseColor("#adadad"))
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
                        ResourcesCompat.getFont(requireContext(), R.font.roboto_medium_otf)
                    binding.etInputPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
                } else {
                    binding.etInputPhone.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.roboto_bold_otf)
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
                    binding.tvCertify.setTextColor(Color.parseColor("#cecece"))
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
            showLoadingDialog(requireContext())
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
            showLoadingDialog(requireContext())
            tryGetPhoneNumCheck(binding.etInputPhone.text.toString().replace(" ",""),inputPhoneNum,1)
        }
    }

    // 버튼 상태 저장
    override fun onPause() {
        super.onPause()
        btnEnabled = binding.btnNext.isEnabled == true
    }

    override fun onDetach() {
        super.onDetach()

    }

    // 버튼 상태 반환(화면 돌아왔을 때)
    override fun onResume() {
        super.onResume()

        if (btnEnabled == true) {
            binding.rlClickableCertify.isEnabled = true
            binding.tvCertify.setTextColor(Color.parseColor("#343434"))
        } else {
            binding.rlClickableCertify.isEnabled = false
            binding.tvCertify.setTextColor(Color.parseColor("#cecece"))
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
    override fun onDestroyView() {
        mCountDown.cancel()
        timer = 120
        super.onDestroyView()
    }


    // 핸드폰 인증 시작 함수
    private fun startPhoneNumberVerification(phoneNumber: String) {
        showCustomToast("인증번호가 전송되었습니다.")
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
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
        dismissLoadingDialog()
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    // [START sign_in_with_phone] // 아이디 생성
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    Log.d(TAG, "계정 생성 완료")
                    task.result?.user

                    showCustomToast("인증 성공")

                    // 타이머 정지
                    mCountDown.cancel()

                    // 파베 계정 삭제 (더미데이터 삭제)
                    deleteAccount()

                } else {
                    // 계정 생성 실패
                    // Sign in failed, display a message and update the UI
                    // showCustomToast("인증 실패")

                    Log.w(TAG, "signInWithCredential:failure", task.exception)
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

                // 화면 전환
                prevFragment =
                    activity?.supportFragmentManager?.findFragmentById(R.id.main_fragment)

                val transaction = activity?.supportFragmentManager?.beginTransaction()?.add(
                    R.id.main_fragment,
                    InputPWFragment()
                )

                transaction?.detach(prevFragment!!)

                transaction?.addToBackStack(null)
                transaction?.commit()
            } else {
                Toast.makeText(requireContext(), "유저정보 삭제 실패 ", Toast.LENGTH_LONG).show()
            }
        }

        dismissLoadingDialog()

//        FirebaseAuth.getInstance().currentUser?.reauthenticate(credential)?.addOnCompleteListener {
//            if(it.isSuccessful){
//                FirebaseAuth.getInstance().currentUser?.delete() // 유저 정보 삭제
//
//                // 유저 전화번호 prefs
//                ApplicationClass.prefs.setString("phone",binding.etInputPhone.text.toString().replace(" ",""))
//
//                // 화면 전환
//                prevFragment =
//                    activity?.supportFragmentManager?.findFragmentById(R.id.main_fragment)
//
//                val transaction = activity?.supportFragmentManager?.beginTransaction()?.add(
//                    R.id.main_fragment,
//                    InputPWFragment()
//                )
//
//                transaction?.detach(prevFragment!!)
//
//                transaction?.addToBackStack(null)
//                transaction?.commit()
//
//            }else{
//                Toast.makeText(requireContext(),"유저정보 삭제 실패 ",Toast.LENGTH_LONG).show()
//            }
//        }
    }

    // 중복체크 api
    override fun onGetCheckSuccess(
        response: PhoneCheckResponse,
        serverCheckPhoneNum: String,
        inputPhoneNum: String
    ) {
        dismissLoadingDialog()

        // 새로운 휴대폰 번호입니다.
        if (response.code == 200) {
            binding.tvOverlap.visibility = View.GONE // 경고 텍스트 감추기

            binding.rlCertify.visibility = View.VISIBLE

            // 인증번호 횟수 체크
            tryGetPhoneNumCheck(binding.etInputPhone.text.toString().replace(" ",""),inputPhoneNum,0)

        } else if (response.code == 202) { // 중복 체크됨
            binding.tvOverlap.visibility = View.VISIBLE // 경고 텍스트 띄우기
        }
    }

    override fun onGetCheckfailure(message: String) {
        dismissLoadingDialog()
        Log.d("error", message)
    }


    private fun tryGetPhoneNumCheck(serverCheckPhoneNum: String,phoneNumber: String, flag:Int) {
        val commonSignUpService: CommonSignUpService =
            ApplicationClass.sRetrofit.create(CommonSignUpService::class.java)
        val call = commonSignUpService.getPhoneNumCheck(serverCheckPhoneNum)
        call.enqueueUtil(
            getResultCode = { it.code },
            onSuccess200 = {
                // 인증 메세지 전송하기
                if (flag == 1) { // 재전송
                    resendVerificationCode(phoneNumber, resendToken)
                } else {
                    startPhoneNumberVerification(phoneNumber)
                }

                showCustomToast(it.message.toString())

                // 카운트 다운 시작
                if (timer != 120) { // 재전송 후 전송 클릭 시 동작 버튼
                    mCountDown.cancel()
                    timer = 120
                }
                mCountDown.start()
                // 다음(인증)버튼 활성화
                binding.btnNext.isEnabled = true

                // 성공 시
                // 휴대폰 입력란과 인증버튼 비활성화
                binding.etInputPhone.isEnabled = false
                binding.rlClickableCertify.isEnabled = false
                binding.tvCertify.setTextColor(Color.parseColor("#cecece"))

            }, // 5번 가능
            onError200 = { showCustomToast(it.message.toString()) } // 모두 소진
        )
    }

}