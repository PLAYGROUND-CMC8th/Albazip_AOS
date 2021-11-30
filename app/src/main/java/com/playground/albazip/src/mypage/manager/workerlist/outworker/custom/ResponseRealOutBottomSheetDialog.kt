package com.playground.albazip.src.mypage.manager.workerlist.outworker.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.playground.albazip.R
import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.databinding.DialogFragmentResponseWorkOutRealBinding
import com.playground.albazip.src.mypage.manager.MMyPageFragment
import com.playground.albazip.src.mypage.manager.workerlist.outworker.network.ResponseWorkOutFragmentView
import com.playground.albazip.src.mypage.manager.workerlist.outworker.network.ResponseWorkOutService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ResponseRealOutBottomSheetDialog(val positionId:Int,val name:String): BottomSheetDialogFragment(),ResponseWorkOutFragmentView{

    private var getPositionId = positionId
    private var worker_name = name
    private lateinit var binding: DialogFragmentResponseWorkOutRealBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentResponseWorkOutRealBinding.inflate(inflater, container, false)

        // 이름 + 안내문
        binding.tvTitle.text = "정말 "+worker_name+"님을 퇴사시키겠어요?"

        // 취소 버튼
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        // 퇴사시키기 (real)
        binding.btnGetOut.setOnClickListener {
            ResponseWorkOutService(this).tryPutWorkOut(getPositionId) // 서버통신 시도
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