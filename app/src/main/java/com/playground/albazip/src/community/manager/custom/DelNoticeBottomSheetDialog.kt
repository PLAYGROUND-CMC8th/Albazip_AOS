package com.playground.albazip.src.community.manager.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.databinding.DialogFragmentNoticeDeleteBinding
import com.playground.albazip.src.community.manager.network.DelNoticeBoardListFragmentView
import com.playground.albazip.src.community.manager.network.DelNoticeBoardService
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