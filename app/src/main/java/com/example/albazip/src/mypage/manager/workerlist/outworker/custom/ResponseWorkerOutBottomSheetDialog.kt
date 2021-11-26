package com.example.albazip.src.mypage.manager.workerlist.outworker.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.albazip.databinding.DialogFragmentResponseWorkOutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ResponseWorkerOutBottomSheetDialog(val positionId:Int,val name:String): BottomSheetDialogFragment(){

    private var getPositionId = positionId
    private var worker_name = name
    private lateinit var binding: DialogFragmentResponseWorkOutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentResponseWorkOutBinding.inflate(inflater, container, false)

        // 이름 + 안내문
        binding.tvTitle.text = worker_name + "님이 퇴사 요청을 보냈어요."

        // 거절하기 버튼
        binding.btnNo.setOnClickListener {
            dismiss()
        }

        // 퇴사 ok
        binding.btnYes.setOnClickListener {
            this.dismiss()
            ResponseRealOutBottomSheetDialog(getPositionId,worker_name).show(requireActivity().supportFragmentManager,"real_response_out")
        }

        return binding.root
    }

}