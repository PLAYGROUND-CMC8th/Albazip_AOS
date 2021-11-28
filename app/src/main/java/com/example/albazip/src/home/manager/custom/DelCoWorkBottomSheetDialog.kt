package com.example.albazip.src.home.manager.custom

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.albazip.config.BaseResponse
import com.example.albazip.databinding.DialogFragmentDeleteCoWorkBinding
import com.example.albazip.src.home.manager.worklist.network.DelTodayTaskFragmentView
import com.example.albazip.src.home.manager.worklist.network.DelTodayTaskService
import com.example.albazip.src.home.manager.worklist.ui.HomeMTodayToDoListActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DelCoWorkBottomSheetDialog(taskId:Int): BottomSheetDialogFragment(),DelTodayTaskFragmentView {

    var getTaskId = taskId
    private lateinit var binding: DialogFragmentDeleteCoWorkBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentDeleteCoWorkBinding.inflate(inflater, container, false)

        // 취소 버튼
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        // 삭제하기 버튼
        binding.btnDelete.setOnClickListener {
            DelTodayTaskService(this).tryGetAllWHomeInfo(getTaskId)
        }

        return binding.root
    }


    // 업무 삭제 성공
    override fun onDelTaskSuccess(response: BaseResponse) {
        dismiss()
        val refreshIntent = Intent(requireContext(),HomeMTodayToDoListActivity::class.java)
        startActivity(refreshIntent)
        activity?.finish()
    }

    // 업무 삭제 실패
    override fun onDelTaskFailure(message: String) {
    }

}