package com.example.albazip.src.community.manager.custom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.albazip.config.BaseResponse
import com.example.albazip.databinding.DialogFragmentNoticeDeleteBinding
import com.example.albazip.src.community.manager.network.DelNoticeBoardListFragmentView
import com.example.albazip.src.community.manager.network.DelNoticeBoardService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DelNoticeBottomSheetDialog(noticeId:Int): BottomSheetDialogFragment(),DelNoticeBoardListFragmentView {

    private lateinit var binding: DialogFragmentNoticeDeleteBinding
    private var getNoticeId = noticeId

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentNoticeDeleteBinding.inflate(inflater, container, false)

        // 취소 버튼
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        // 삭제 버튼
        binding.btnDel.setOnClickListener {
            DelNoticeBoardService(this).tryDelNoticeBoardList(getNoticeId)
        }

        return binding.root
    }

    override fun onDelNoticeGetSuccess(response: BaseResponse) {
        dismiss()
        activity?.finish()
    }

    override fun onDelNoticeGetFailure(message: String) {
    }
}