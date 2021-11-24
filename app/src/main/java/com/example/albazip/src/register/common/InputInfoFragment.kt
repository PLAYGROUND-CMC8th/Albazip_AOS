package com.example.albazip.src.register.common

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.albazip.src.main.MainActivity
import com.example.albazip.R
import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.example.albazip.config.ApplicationClass.Companion.prefs
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.FragmentInputInfoBinding
import com.example.albazip.src.register.common.custom.AgeBottomSheetDialog
import com.example.albazip.src.register.common.data.remote.PostSignUpRequest
import com.example.albazip.src.register.common.data.remote.SignUpResponse
import com.example.albazip.src.register.common.network.SignUpFragmentView
import com.example.albazip.src.register.common.network.SignUpService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InputInfoFragment : BaseFragment<FragmentInputInfoBinding>(
    FragmentInputInfoBinding::bind,
    R.layout.fragment_input_info
), AgeBottomSheetDialog.BottomSheetClickListener, SignUpFragmentView {

    var first_name_flags = false
    var last_name_flags = false
    var age_flags = false

    // 성별 받아오기
    var gender = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 메인 화면으로 이동
        binding.btnNext.setOnClickListener {

            ////////////////////// 회원 가입 서버 통신 /////////////////////////////////
            // 데이터 조회
            val phone = ApplicationClass.prefs.getString("phone", null)
            val pwd = ApplicationClass.prefs.getString("pwd", null)
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val birthyear = binding.tvInputAge.text.toString()

            // 성별 받아오기
            if (binding.rlMan.isSelected) {
                gender = "M"
            } else {
                gender = "F"
            }

            val postRequest = PostSignUpRequest(
                phone = phone,
                pwd = pwd,
                lastName = lastName,
                firstName = firstName,
                birthyear = birthyear,
                gender = gender
            )

            showLoadingDialog(requireContext())

            SignUpService(this).tryPostSignUp(postRequest)

        }

        // 이전 화면으로 이동(비밀번호 입력)
        binding.btnBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        // 포커스 여부 감지
        binding.apply {
            etLastName.setOnFocusChangeListener { v, hasFocus ->
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
            etFirstName.setOnFocusChangeListener { v, hasFocus ->
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
        binding.etLastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s?.length!! >= 1){
                    first_name_flags = true
                    binding.etLastName.setTypeface(null,Typeface.BOLD)
                }else{
                    first_name_flags = false
                    binding.etLastName.setTypeface(null,Typeface.NORMAL)
                }

                checkIntentState()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 입력감지(last-name)
        binding.etFirstName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //last_name_flags = s?.length!! >= 1
                if(s?.length!! >= 1){
                    last_name_flags = true
                    binding.etFirstName.setTypeface(null,Typeface.BOLD)
                }else{
                    last_name_flags = false
                    binding.etFirstName.setTypeface(null,Typeface.NORMAL)
                }

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
        binding.btnNext.setTextColor(Color.parseColor("#343434"))
    }

    // 버튼 비활성화 함수
    private fun deActivateBtn() {
        binding.btnNext.isEnabled = false
        binding.btnNext.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.btn_disable_yellow_fill_rounded)
        binding.btnNext.setTextColor(Color.parseColor("#adadad"))
    }

    // bottomsheet dialog에서 생년월일 받아오기
    override fun onItemSelected(text: String) {
        binding.tvInputAge.text = text
        binding.tvInputAge.setTextColor(Color.parseColor("#343434"))
        binding.tvInputAge.setTypeface(null,Typeface.BOLD)
        age_flags = true
        checkIntentState()
    }

    // 회원 가입 통신
    override fun onPostSignUpSuccess(response: SignUpResponse) {
        dismissLoadingDialog()
        if (response.code == 200) {
            showCustomToast("회원가입 성공")
            // 메인 화면으로 이동
            val mainIntent = Intent(requireContext(), MainActivity::class.java)
            startActivity(mainIntent)
            activity?.finish()
        } else {
            // 이미 존재하는 연락처입니다.
            showCustomToast("회원가입 실패")
        }
    }

    override fun onPostSignUpFailure(message: String) {
        dismissLoadingDialog()
        Log.e("SignUpFailure", message)
    }
}