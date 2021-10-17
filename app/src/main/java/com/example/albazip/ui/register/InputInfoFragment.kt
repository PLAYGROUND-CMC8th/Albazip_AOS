package com.example.albazip.ui.register

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.FragmentInputInfoBinding

class InputInfoFragment : BaseFragment<FragmentInputInfoBinding>(
    FragmentInputInfoBinding::bind,
    R.layout.fragment_input_info
) {
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

        // 성별 선택
        binding.apply {
            // 남자 선택
            rlMan.setOnClickListener {
                rlMan.isSelected = true
                rlWoman.isSelected = false
            }

            // 여자 선택
            rlWoman.setOnClickListener {
                rlWoman.isSelected = true
                rlMan.isSelected = false
            }
        }
    }
}