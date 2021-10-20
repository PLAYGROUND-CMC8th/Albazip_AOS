package com.example.albazip.ui.register.common

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.FragmentInputPhoneBinding

class InputPhoneFragment : BaseFragment<FragmentInputPhoneBinding>(
    FragmentInputPhoneBinding::bind,
    R.layout.fragment_input_phone
) {

    var btnEnabled:Boolean = false

    // 타이머 설정
    var timer = 165

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 기본 정보 입력 화면으로 이동
        binding.btnNext.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, InputInfoFragment()).commit()
        }

        // 뒤로가기 버튼(약관동의)
        binding.btnBack.setOnClickListener {
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
                        ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)
                    binding.etInputPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
                } else {
                    binding.etInputPhone.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.roboto_bold)
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

        // 인증 버튼 클릭
        binding.rlClickableCertify.setOnClickListener {

            binding.rlCertify.visibility = View.VISIBLE

            // 카운트 다운 시작
            mCountDown.start()
        }

        // 기본 정보 입력 화면으로 이동
        binding.btnNext.setOnClickListener {
            mCountDown.cancel()
            parentFragmentManager.beginTransaction().replace(R.id.main_fragment, InputPWFragment())
                .commit()
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

        if(btnEnabled == true){
            binding.rlClickableCertify.isEnabled = true
            binding.tvCertify.setTextColor(Color.parseColor("#343434"))
        }else{
            binding.rlClickableCertify.isEnabled = false
            binding.tvCertify.setTextColor(Color.parseColor("#cecece"))
        }
    }

    private val mCountDown: CountDownTimer = object : CountDownTimer(99000, 1000) {
        override fun onTick(millisUntilFinished: Long) {

            // 0초면 버튼활성화
            if (timer > 119) {
                binding.tvCountDown.text = "2:${timer - 120}"
                timer--
            } else if (timer > 59) {
                binding.tvCountDown.text = "1:${timer - 60}"
                timer--
            } else {
                binding.tvCountDown.text = "${timer}"
                timer--
            }

            if (timer == 0) {
                onFinish()
            }

        }

        override fun onFinish() {}
    }

}