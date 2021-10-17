package com.example.albazip.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import com.example.albazip.MainActivity
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.FragmentAgreementBinding
import com.example.albazip.ui.login.RegisterActivity

// 약관동의
class AgreementFragment : BaseFragment<FragmentAgreementBinding>(
    FragmentAgreementBinding::bind,
    R.layout.fragment_agreement
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 동의 체크 이벤트
        binding.apply {
            cbAgreeAll.setOnClickListener { onCheckChanged(cbAgreeAll) }
            cbAge.setOnClickListener { onCheckChanged(cbAge) }
            cbService.setOnClickListener { onCheckChanged(cbService) }
            cbCollect.setOnClickListener { onCheckChanged(cbCollect) }
        }
        
        // 비밀번호 입력 화면으로 이동
        binding.btnNext.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.main_fragment,InputPWFragment()).commit()
        }

        // 이전 화면으로 이동
        binding.btnBack.setOnClickListener {
            activity?.finish()
        }
    }

    private fun onCheckChanged(checkBox: CheckBox) {
        binding.apply {
            when (checkBox) {
                cbAgreeAll -> {
                    if (cbAgreeAll.isChecked) {
                        cbAge.isChecked = true
                        cbService.isChecked = true
                        cbCollect.isChecked = true
                    } else {
                        cbAge.isChecked = false
                        cbService.isChecked = false
                        cbCollect.isChecked = false
                    }
                }
                else -> {
                    cbAgreeAll.isChecked = (
                            cbAge.isChecked && cbService.isChecked && cbCollect.isChecked)
                }
            }

            // 전체 동의가 되었으면
            if (cbAgreeAll.isChecked) {
                activateBtn()
                showCustomToast("활성화")
            } else {
                deActivateBtn()
                showCustomToast("비활성화")
            }

        }
    }

    // 버튼 활성화 함수
    private fun activateBtn() {
        binding.btnNext.isClickable = true
    }

    // 버튼 비활성화 함수
    private fun deActivateBtn() {
        binding.btnNext.isClickable = false
    }
}