package com.playground.albazip.src.home.common.custom

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.databinding.DialogFragmentCancelDoneBinding
import com.playground.albazip.src.home.common.network.PutTodayHomeTaskFragmentView
import com.playground.albazip.src.home.common.network.PutTodayHomeTaskService
import com.playground.albazip.src.home.manager.worklist.ui.HomeMTodayToDoListActivity
import com.playground.albazip.src.home.worker.opened.worklist.ui.HomeWTodayToDoListActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.playground.albazip.src.home.manager.worklist.network.InnerCoWorker

class DoneCancelBottomSheetDialog(checkView:CheckBox,delView:View,taskId:Int,jobFlags:Int,comWorker: ArrayList<InnerCoWorker>?): BottomSheetDialogFragment(),PutTodayHomeTaskFragmentView {

    private lateinit var binding: DialogFragmentCancelDoneBinding
    private var checkView = checkView
    private val delView = delView
    private val taskId = taskId
    private var jobFlags = jobFlags

    private var popComWorker = comWorker
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

            if(popComWorker != null) {
                // 관리자 일 때
                if (jobFlags == 0) {
                    for (i in 0 until popComWorker!!.size) { // taskId가 일치하고 관리자 본인이 한 일이 맞을 때
                        if (popComWorker!![i].worker.contains("사장님") && popComWorker!![i].taskId.contains(taskId)
                        ) {
                            PutTodayHomeTaskService(this).tryPutTodayTask(taskId)
                            break
                        } else {
                            checkView.isChecked = true
                            delView.visibility = View.VISIBLE
                            Toast.makeText(context, "다른 사람이 완료한 업무입니다.", Toast.LENGTH_SHORT).show()
                            dismiss()
                        }
                    }
                } else { // 근무자 일 때

                }
            }

        }

        return binding.root
    }

    // 되돌리기 성공
    override fun onPutTodayTaskSuccess(response: BaseResponse) {
        if(jobFlags == 0) { // 관리자
            var nextIntent = Intent(requireContext(), HomeMTodayToDoListActivity::class.java)
            startActivity(nextIntent)
            activity?.finish()
        }else{ // 근무자
            var nextIntent = Intent(requireContext(), HomeWTodayToDoListActivity::class.java)
            startActivity(nextIntent)
            activity?.finish()
        }
        dismiss()
    }

    // 되돌리기 실패!
    override fun onPutTodayTaskFailure(message: String) {

    }

    /* interface BottomSheetClickListener{
         fun onItemSelected(delView:Boolean)
     }*/
}