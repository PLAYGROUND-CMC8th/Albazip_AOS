package com.example.albazip.src.community.worker.custom

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.albazip.config.ApplicationClass
import com.example.albazip.databinding.DialogFragmentLogoutBinding
import com.example.albazip.databinding.DialogFragmentNoticeMenuForWBinding
import com.example.albazip.databinding.DialogFragmentReportReasonBinding
import com.example.albazip.src.main.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NoticeReportBottomSheetDialog(noticeId:Int): BottomSheetDialogFragment() {

    private lateinit var binding: DialogFragmentNoticeMenuForWBinding

    private var getNoticeId = noticeId

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentNoticeMenuForWBinding.inflate(inflater, container, false)

        // 신고하기
        binding.rlRowOne.setOnClickListener {
            NoticeReasonBottomSheetDialog(getNoticeId).show(activity?.supportFragmentManager!!, "ReportReasonAlert")
            dismiss()
        }

        return binding.root
    }
}