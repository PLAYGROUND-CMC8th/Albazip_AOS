package com.example.albazip.src.home.manager.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.albazip.databinding.DialogFragmentAddWorklistBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddWorkBottomSheetDialog:BottomSheetDialogFragment(),View.OnClickListener {

    private lateinit var binding: DialogFragmentAddWorklistBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,

        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentAddWorklistBinding.inflate(inflater, container, false)

        binding.rlRowOne.setOnClickListener(this)
        binding.rlRowTwo.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v) {
                rlRowOne -> {
                   // 공동업무 추가 화면으로 이동
                }

                rlRowTwo -> {
                    // 개인업무 추가 화면으로 이동
                }

            }
        }
    }

}