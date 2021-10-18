package com.example.albazip.ui.register

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.FragmentInputInfoBinding

class InputInfoFragment : BaseFragment<FragmentInputInfoBinding>(
    FragmentInputInfoBinding::bind,
    R.layout.fragment_input_info
) {

    var first_name_flags = true
    var last_name_flags = true
    var age_flags = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 메인 화면으로 이동
        binding.btnNext.setOnClickListener {
            activity?.finish()
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
            etAge.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus)
                    binding.rlAge.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.rectagnle_yellow_radius
                    ) else {
                    binding.rlAge.background = ContextCompat.getDrawable(
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
                first_name_flags = s?.length!! >= 1
                checkIntentState()
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        // 입력감지(나이)
        binding.etAge.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                age_flags = s?.length!! == 4
                checkIntentState()
            }

            override fun afterTextChanged(s: Editable?) {}
        })



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

    private fun checkIntentState(){
        if(first_name_flags && last_name_flags && age_flags && (binding.rlMan.isSelected||binding.rlWoman.isSelected)){
            activateBtn()
        }else{
            deActivateBtn()
        }
    }

    // 버튼 활성화 함수
    private fun activateBtn() {
        binding.btnNext.isEnabled = true
        binding.btnNext.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_main_yellow_fill_rounded)
    }

    // 버튼 비활성화 함수
    private fun deActivateBtn() {
        binding.btnNext.isEnabled = false
        binding.btnNext.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_disable_yellow_fill_rounded)
    }
}