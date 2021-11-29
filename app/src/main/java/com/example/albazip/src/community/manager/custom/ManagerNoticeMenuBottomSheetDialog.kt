package com.example.albazip.src.community.manager.custom


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.albazip.databinding.DialogFragmentNoticeMenuForWBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ManagerNoticeMenuBottomSheetDialog(noticeId:Int): BottomSheetDialogFragment() {
    private lateinit var binding: DialogFragmentNoticeMenuForWBinding
    var getNoticeId = noticeId

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentNoticeMenuForWBinding.inflate(inflater, container, false)

        binding.tvMenuOne.text = "삭제하기"

        // 삭제하기
        binding.rlRowOne.setOnClickListener {
            DelNoticeBottomSheetDialog(getNoticeId).show(activity?.supportFragmentManager!!, "DelNoticeAlert")
            dismiss()
        }


        return binding.root
    }
}