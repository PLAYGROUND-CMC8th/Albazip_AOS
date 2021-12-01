package com.playground.albazip.src.register.common

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import com.playground.albazip.R
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.FragmentAgreementBinding

// 약관동의
class AgreementFragment(

) : BaseFragment<FragmentAgreementBinding>(
    FragmentAgreementBinding::bind,
    R.layout.fragment_agreement
) {

    var btnEnabled: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 이용약관 보기
        binding.tvShowService.setOnClickListener {
            val uriIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://bronzed-balaur-143.notion.site/42041221d1a6413f84542f571bee6b9c"))
            startActivity(uriIntent)
        }

        // 정보처리 약관 보기
        binding.tvShowCollect.setOnClickListener {
            val uriIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://bronzed-balaur-143.notion.site/02e8b5a9cf514702977b4e01b82651ca"))
            startActivity(uriIntent)
        }

        // 동의 체크 이벤트
        binding.apply {
            cbAgreeAll.setOnClickListener { onCheckChanged(cbAgreeAll) }
            cbAge.setOnClickListener { onCheckChanged(cbAge) }
            cbService.setOnClickListener { onCheckChanged(cbService) }
            cbCollect.setOnClickListener { onCheckChanged(cbCollect) }
        }

        // 비밀번호 입력 화면으로 이동
        binding.btnNext.setOnClickListener {

            prevFragment = activity?.supportFragmentManager?.findFragmentById(R.id.main_fragment)

            val transaction = activity?.supportFragmentManager?.beginTransaction()?.add(R.id.main_fragment,
                InputPhoneFragment()
            )

            transaction?.detach(prevFragment!!)

            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        // 이전 화면으로 이동
        binding.btnBack.setOnClickListener {
            activity?.finish()
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
            activateBtn()
        }else{
            deActivateBtn()
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

            // 전체 동의가 되었을 때
            if (cbAgreeAll.isChecked) { // 활성화
                activateBtn()
            } else { // 비활성화
                deActivateBtn()
            }

        }
    }

    // 버튼 활성화 함수
    private fun activateBtn() {
        binding.btnNext.isEnabled = true
        binding.btnNext.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_main_yellow_fill_rounded)
        binding.btnNext.setTextColor(Color.parseColor("#343434"))
    }

    // 버튼 비활성화 함수
    private fun deActivateBtn() {
        binding.btnNext.isEnabled = false
        binding.btnNext.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_disable_yellow_fill_rounded)
        binding.btnNext.setTextColor(Color.parseColor("#adadad"))
    }
}