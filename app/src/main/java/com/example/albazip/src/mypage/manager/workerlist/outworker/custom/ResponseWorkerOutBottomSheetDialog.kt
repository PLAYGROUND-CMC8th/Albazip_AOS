package com.example.albazip.src.mypage.manager.workerlist.outworker.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.albazip.R
import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.example.albazip.databinding.DialogFragmentResponseWorkOutBinding
import com.example.albazip.src.mypage.manager.MMyPageFragment
import com.example.albazip.src.mypage.manager.workerlist.outworker.network.ResponseWorkOutFragmentView
import com.example.albazip.src.mypage.manager.workerlist.outworker.network.ResponseWorkOutService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ResponseWorkerOutBottomSheetDialog(val positionId:Int,val name:String): BottomSheetDialogFragment(),
    ResponseWorkOutFragmentView {

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
            ResponseWorkOutService(this).tryPutWorkOut(getPositionId) // 서버통신 시도
            //ResponseRealOutBottomSheetDialog(getPositionId,worker_name).show(requireActivity().supportFragmentManager,"real_response_out")
        }

        return binding.root
    }

    override fun onWorkOutDelSuccess(response: BaseResponse) {
        dismiss()
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.manager_main_frm, MMyPageFragment()).commitNow()
        ApplicationClass.prefs.setInt("backStackState",0) // 백스택 관리
    }

    override fun onWorkOutDelFailure(message: String) {
    }

}