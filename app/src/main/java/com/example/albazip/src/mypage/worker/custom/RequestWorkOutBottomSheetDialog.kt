package com.example.albazip.src.mypage.worker.custom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.albazip.databinding.DialogFragmentRequestWorkOutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RequestWorkOutBottomSheetDialog(): BottomSheetDialogFragment() {

    private lateinit var binding: DialogFragmentRequestWorkOutBinding

    lateinit var bottomSheetClickListener: BottomSheetClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bottomSheetClickListener = requireParentFragment() as BottomSheetClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentRequestWorkOutBinding.inflate(inflater, container, false)


        // 취소 버튼
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        // 요청하기 버튼
        binding.btnRequest.setOnClickListener {
            bottomSheetClickListener.onItemSelected(true)
            dismiss()
        }

        return binding.root
    }

    interface BottomSheetClickListener{
        fun onItemSelected(isDeleteClicked:Boolean)
    }
}