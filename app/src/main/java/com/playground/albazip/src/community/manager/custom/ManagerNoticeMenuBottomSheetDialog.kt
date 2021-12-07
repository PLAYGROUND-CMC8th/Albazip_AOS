package com.playground.albazip.src.community.manager.custom


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.playground.albazip.databinding.DialogFragmentNoticeMenuForMBinding
import com.playground.albazip.src.community.manager.ui.EditNoticeActivity

class ManagerNoticeMenuBottomSheetDialog(noticeId:Int): BottomSheetDialogFragment() {
    private lateinit var binding: DialogFragmentNoticeMenuForMBinding
    var getNoticeId = noticeId

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentNoticeMenuForMBinding.inflate(inflater, container, false)

        // 편집하기
        binding.rlRowOne.setOnClickListener {
            val nextIntent = Intent(requireContext(),EditNoticeActivity::class.java)
            nextIntent.putExtra("noticeId",getNoticeId)
            startActivity(nextIntent)
        }

        // 삭제하기
        binding.rlRowTwo.setOnClickListener {
            DelNoticeBottomSheetDialog(getNoticeId).show(activity?.supportFragmentManager!!, "DelNoticeAlert")
            dismiss()
        }


        return binding.root
    }
}