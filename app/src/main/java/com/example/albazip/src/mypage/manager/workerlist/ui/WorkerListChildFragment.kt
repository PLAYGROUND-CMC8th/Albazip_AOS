package com.example.albazip.src.mypage.manager.workerlist.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.os.bundleOf
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentWorkerListBinding
import com.example.albazip.src.mypage.manager.init.data.WorkerList
import com.example.albazip.src.mypage.manager.workerlist.data.remote.GetWorkerListResponse
import com.example.albazip.src.mypage.manager.workerlist.data.remote.WorkerListData
import com.example.albazip.src.mypage.manager.workerlist.network.WorkListFragmentView
import com.example.albazip.src.mypage.manager.workerlist.network.WorkerListService
import kotlinx.coroutines.withTimeoutOrNull

class WorkerListChildFragment(workerList:ArrayList<WorkerList>) : BaseFragment<ChildFragmentWorkerListBinding>(
    ChildFragmentWorkerListBinding::bind,
    R.layout.child_fragment_worker_list
) {
    val myWorKerList = workerList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showCustomToast(myWorKerList.toString()+"야 여기 찍히냥?ㅎㅎ")
        // 근무자 리스트 조회
        //WorkerListService(this).tryGetWorkerList()
        //showLoadingDialog(requireContext())
    }


    override fun onResume() {
        super.onResume()
        // 근무자 추가 activity 를 완료한 후 돌아왔을 때
        //checkExistState()
    }


//    override fun onGetSuccess(response: GetWorkerListResponse) {
//        dismissLoadingDialog()
//        if(response.code == 200){
//            showCustomToast("성공")
//        }
//    }
//
//    override fun onGetSuccess(response: GetWorkerListResponse, workerListData: WorkerListData) {
//        dismissLoadingDialog()
//        if(response.code == 200){
//            showCustomToast("성공")
//        }
//    }
//
//    override fun onGetFailure(message: String) {
//        dismissLoadingDialog()
//    }
}