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

        // ?????? ??????
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        // ???????????? ??????
        binding.btnReturn.setOnClickListener {
            checkView.isChecked = false
            delView.visibility = View.GONE


            // ????????? ??? ???
            if (myJobFlags == 0) {
                if (popComWorker != null) {
                    for (i in 0 until popComWorker!!.size) { // taskId??? ???????????? ????????? ????????? ??? ?????? ?????? ???
                        if (popComWorker!![i].worker.contains("?????????") && popComWorker!![i].taskId.contains(taskId)) {
                            managerExist = true
                            break
                        }
                    }
                    checkManagerExist(managerExist)
                }else{
                    PutTodayHomeTaskService(this).tryPutTodayTask(taskId)
                }
            }else{ // ???????????? ???
                 if (popComWorker != null) {
                    for (i in 0 until popComWorker!!.size) { // taskId??? ???????????? ????????? ????????? ??? ?????? ?????? ???
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

    // ???????????? ??????
    override fun onPutTodayTaskSuccess(response: BaseResponse) {
        if (myJobFlags == 0) { // ?????????
            val nextIntent = Intent(requireContext(), HomeMTodayToDoListActivity::class.java)
            startActivity(nextIntent)
            activity?.finish()
        } else { // ?????????
            val nextIntent = Intent(requireContext(), HomeWTodayToDoListActivity::class.java)
            startActivity(nextIntent)
            activity?.finish()
        }
        dismiss()
    }

    // ???????????? ??????!
    override fun onPutTodayTaskFailure(message: String) {

    }

    override fun onGetWorkerInfoSuccess(response: WorkerInfoResponse) {
        workerName = response.data.firstName
    }

    override fun onGetWorkerInfoFailure(message: String) {

    }

    // ????????? ????????? ??? ???????????? ??????
    fun checkWorkerExist(isExist:Boolean){
        if(isExist == true) {
            PutTodayHomeTaskService(this).tryPutTodayTask(taskId)
        }else{
            checkView.isChecked = true
            delView.visibility = View.VISIBLE
            Toast.makeText(context, "?????? ????????? ????????? ???????????????.", Toast.LENGTH_SHORT)
                .show()
            dismiss()
        }
    }

    // ????????? ????????? ??? ???????????? ??????
    fun checkManagerExist(isExist:Boolean){
        if(isExist == true) {
            PutTodayHomeTaskService(this).tryPutTodayTask(taskId)
        }else{
            checkView.isChecked = true
            delView.visibility = View.VISIBLE
            Toast.makeText(context, "?????? ????????? ????????? ???????????????.", Toast.LENGTH_SHORT)
                .show()
            dismiss()
        }
    }

    /* interface BottomSheetClickListener{
         fun onItemSelected(delView:Boolean)
     }*/
}