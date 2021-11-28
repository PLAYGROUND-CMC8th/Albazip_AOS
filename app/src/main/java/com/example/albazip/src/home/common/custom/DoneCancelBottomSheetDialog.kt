package com.example.albazip.src.home.common.custom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.example.albazip.config.BaseResponse
import com.example.albazip.databinding.DialogFragmentCancelDoneBinding
import com.example.albazip.src.home.common.network.PutTodayHomeTaskFragmentView
import com.example.albazip.src.home.common.network.PutTodayHomeTaskService
import com.example.albazip.src.home.manager.worklist.ui.HomeMTodayToDoListActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DoneCancelBottomSheetDialog(checkView:CheckBox,delView:View,taskId:Int): BottomSheetDialogFragment(),PutTodayHomeTaskFragmentView {

    private lateinit var binding: DialogFragmentCancelDoneBinding
    private var checkView = checkView
    private val delView = delView
    private val taskId = taskId

    //lateinit var bottomSheetClickListener: DoneCancelBottomSheetDialog.BottomSheetClickListener

    //override fun onAttach(context: Context) {
    //    super.onAttach(context)

    //    bottomSheetClickListener = context as DoneCancelBottomSheetDialog.BottomSheetClickListener
    //}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentCancelDoneBinding.inflate(inflater, container, false)


        // 취소 버튼
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        // 되돌리기 버튼
        binding.btnReturn.setOnClickListener {
            checkView.isChecked = false
            delView.visibility = View.GONE

            PutTodayHomeTaskService(this).tryPutTodayTask(taskId)

        }

        return binding.root
    }

    // 되돌리기 성공
    override fun onPutTodayTaskSuccess(response: BaseResponse) {
        var nextIntent = Intent(requireContext(), HomeMTodayToDoListActivity::class.java)
        startActivity(nextIntent)
        activity?.finish()
        dismiss()
    }

    // 되돌리기 실패!
    override fun onPutTodayTaskFailure(message: String) {

    }

    /* interface BottomSheetClickListener{
         fun onItemSelected(delView:Boolean)
     }*/
}