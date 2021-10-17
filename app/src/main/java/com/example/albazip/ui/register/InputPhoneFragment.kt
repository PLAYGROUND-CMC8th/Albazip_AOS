package com.example.albazip.ui.register

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.FragmentInputPhoneBinding
import kotlin.math.roundToInt

class InputPhoneFragment : BaseFragment<FragmentInputPhoneBinding>(
    FragmentInputPhoneBinding::bind,
    R.layout.fragment_input_phone
) {

    // 타이머 설정
    var timer = 165

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        // 휴대폰 번호 입력 (자동 띄어씌기)
        binding.etInputPhone.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {}

        })

        // 인증 버튼 클릭
        binding.rlClickableCertify.setOnClickListener {

            binding.rlCertify.visibility = View.VISIBLE

            // 카운트 다운 시작
            mCountDown.start()
        }

        // 기본 정보 입력 화면으로 이동
        binding.btnNext.setOnClickListener {
            mCountDown.cancel()
            parentFragmentManager.beginTransaction().replace(R.id.main_fragment,InputPWFragment()).commit()
        }

    }

    private val mCountDown: CountDownTimer = object : CountDownTimer(99000, 1000) {
        override fun onTick(millisUntilFinished: Long) {

            // 0초면 버튼활성화
            if(timer>119){
                binding.tvCountDown.text="2:${timer-120}"
                timer--
            }else if(timer>59){
                binding.tvCountDown.text="1:${timer-60}"
                timer--
            }else{
                binding.tvCountDown.text="${timer}"
                timer--
            }

            if (timer == 0){
                onFinish()
            }

        }

        override fun onFinish() {
            //countdown finish
            //onClickStart()
        }
    }

}