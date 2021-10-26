package com.example.albazip.src.register.common

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.albazip.src.main.MainActivity
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.FragmentInputInfoBinding
import com.example.albazip.src.register.common.custom.AgeBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InputInfoFragment : BaseFragment<FragmentInputInfoBinding>(
    FragmentInputInfoBinding::bind,
    R.layout.fragment_input_info
), AgeBottomSheetDialog.BottomSheetClickListener {

    var first_name_flags = false
    var last_name_flags = false
    var age_flags = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 메인 화면으로 이동
        binding.btnNext.setOnClickListener {

            // 회원가입 하기 ///////////////////////////////////////서버통신 here



            val mainIntent = Intent(context, MainActivity::class.java)
            startActivity(mainIntent)
            activity?.finish()
        }

        // 이전 화면으로 이동(비밀번호 입력)
        binding.btnBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        // 포커스 여부 감지
        binding.apply {
            etFirstName.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus)
                    binding.rlFirstName.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.rectagnle_yellow_radius
                    ) else {
                    binding.rlFirstName.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.rectangle_custom_white_radius
                    )
                }
            }
            etName.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus)
                    binding.rlName.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.rectagnle_yellow_radius
                    ) else {
                    binding.rlName.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.rectangle_custom_white_radius
                    )
                }
            }
        }

        // 입력감지(first-name)
        binding.etFirstName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                first_name_flags = s?.length!! >= 1
                checkIntentState()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 입력감지(last-name)
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                last_name_flags = s?.length!! >= 1
                checkIntentState()
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        // 생년월일 picker dialog 생성
        binding.tvInputAge.setOnClickListener {
            AgeBottomSheetDialog().show(childFragmentManager, "agePicker")
        }


        // 성별 선택
        binding.apply {
            // 남자 선택
            rlMan.setOnClickListener {
                rlMan.isSelected = true
                rlWoman.isSelected = false
                checkIntentState()
            }

            // 여자 선택
            rlWoman.setOnClickListener {
                rlWoman.isSelected = true
                rlMan.isSelected = false
                checkIntentState()
            }
        }
    }

    private fun checkIntentState() {
        if ((first_name_flags && last_name_flags && age_flags) && (binding.rlMan.isSelected || binding.rlWoman.isSelected)) {
            activateBtn()
        } else {
            deActivateBtn()
        }
    }

    // 버튼 활성화 함수
    private fun activateBtn() {
        binding.btnNext.isEnabled = true
        binding.btnNext.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.btn_main_yellow_fill_rounded)
    }

    // 버튼 비활성화 함수
    private fun deActivateBtn() {
        binding.btnNext.isEnabled = false
        binding.btnNext.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.btn_disable_yellow_fill_rounded)
    }

    // bottomsheet dialog에서 생년월일 받아오기
    override fun onItemSelected(text: String) {
        binding.tvInputAge.text = text
        binding.tvInputAge.setTextColor(Color.parseColor("#343434"))
        age_flags = true
        checkIntentState()
    }
}