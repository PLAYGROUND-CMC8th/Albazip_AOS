package com.playground.albazip.src.home.common.custom

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.databinding.DialogFragmentCancelDoneBinding
import com.playground.albazip.src.home.common.network.PutTodayHomeTaskFragmentView
import com.playground.albazip.src.home.common.network.PutTodayHomeTaskService
import com.playground.albazip.src.home.manager.worklist.ui.HomeMTodayToDoListActivity
import com.playground.albazip.src.home.worker.opened.worklist.ui.HomeWTodayToDoListActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.playground.albazip.src.home.manager.worklist.network.InnerCoWorker
import com.playground.albazip.src.home.worker.network.GetWorkerInfoService
import com.playground.albazip.src.home.worker.network.GetWorkerInfoWFragmentView
import com.playground.albazip.src.home.worker.network.WorkerInfoResponse
import com.playground.albazip.util.LoadingDialog

class DoneCancelBottomSheetDialog(
    checkView: CheckBox,
    delView: View,
    taskId: Int,
    jobFlags: Int,
    comWorker: ArrayList<InnerCoWorker>?
) : BottomSheetDialogFragment(), PutTodayHomeTaskFragmentView,GetWorkerInfoWFragmentView{

    private lateinit var binding: DialogFragmentCancelDoneBinding
    private var checkView = checkView
    private val delView = delView
    private val taskId = taskId
    private var myJobFlags = jobFlags

    private var workerName = ""
    private var workerExist:Boolean = false
    private var managerExist:Boolean = false

    private var popComWorker = comWorker

    override fun onAttach(context: Context) {
        super.onAttach(context)
        GetWorkerInfoService(this).tryGetWorkerInfo()
    }

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


            // 관리자 일 때
            if (myJobFlags == 0) {
                if (popComWorker != null) {
                    for (i in 0 until popComWorker!!.size) { // taskId가 일치하고 관리자 본인이 한 일이 맞을 때
                        if (popComWorker!![i].worker.contains("사장님") && popComWorker!![i].taskId.contains(taskId)) {
                            managerExist = true
                            break
                        }
                    }
                    checkManagerExist(managerExist)
                }else{
                    PutTodayHomeTaskService(this).tryPutTodayTask(taskId)
                }
            }else{ // 근무자일 때
                 if (popComWorker != null) {
                    for (i in 0 until popComWorker!!.size) { // taskId가 일치하고 근무자 본인이 한 일이 맞을 때
                        if (popComWorker!![i].worker.substring(5,popComWorker!![i].worker.lastIndex+1) == workerName && popComWorker!![i].taskId.contains(taskId)) {
                            workerExist=true
                            break
                        }
                    }
                     checkWorkerExist(workerExist)
                }else{
                    PutTodayHomeTaskService(this).tryPutTodayTask(taskId)
                }
            }

        }
        return binding.root
    }

    // 되돌리기 성공
    override fun onPutTodayTaskSuccess(response: BaseResponse) {
        if (myJobFlags == 0) { // 관리자
            val nextIntent = Intent(requireContext(), HomeMTodayToDoListActivity::class.java)
            startActivity(nextIntent)
            activity?.finish()
        } else { // 근무자
            val nextIntent = Intent(requireContext(), HomeWTodayToDoListActivity::class.java)
            startActivity(nextIntent)
            activity?.finish()
        }
        dismiss()
    }

    // 되돌리기 실패!
    override fun onPutTodayTaskFailure(message: String) {

    }

    override fun onGetWorkerInfoSuccess(response: WorkerInfoResponse) {
        workerName = response.data.firstName
    }

    override fun onGetWorkerInfoFailure(message: String) {

    }

    // 근무자 일치할 때 되돌릴지 여부
    fun checkWorkerExist(isExist:Boolean){
        if(isExist == true) {
            PutTodayHomeTaskService(this).tryPutTodayTask(taskId)
        }else{
            checkView.isChecked = true
            delView.visibility = View.VISIBLE
            Toast.makeText(context, "다른 사람이 완료한 업무입니다.", Toast.LENGTH_SHORT)
                .show()
            dismiss()
        }
    }

    // 관리자 일치할 때 되돌릴지 여부
    fun checkManagerExist(isExist:Boolean){
        if(isExist == true) {
            PutTodayHomeTaskService(this).tryPutTodayTask(taskId)
        }else{
            checkView.isChecked = true
            delView.visibility = View.VISIBLE
            Toast.makeText(context, "다른 사람이 완료한 업무입니다.", Toast.LENGTH_SHORT)
                .show()
            dismiss()
        }
    }

    /* interface BottomSheetClickListener{
         fun onItemSelected(delView:Boolean)
     }*/
}