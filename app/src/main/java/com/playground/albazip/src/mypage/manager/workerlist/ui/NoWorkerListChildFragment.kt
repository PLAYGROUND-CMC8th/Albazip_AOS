package com.playground.albazip.src.mypage.manager.workerlist.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.playground.albazip.R
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.ChildFragmentNoWorkerListBinding

class NoWorkerListChildFragment : BaseFragment<ChildFragmentNoWorkerListBinding>(
    ChildFragmentNoWorkerListBinding::bind,
    R.layout.child_fragment_no_worker_list
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddWorker.setOnClickListener {
            val nextIntent = Intent(requireContext(), AddWorkerOneActivity::class.java)
            startActivity(nextIntent)
        }

        // 근무자 리스트 조회
        //WorkerListService(this).tryGetWorkerList()
        //showLoadingDialog(requireContext())

    }

    // 근무자 존재 여부 체크
    fun checkExistState(){
        // 만약 데이터가 있으면
        // ll 은 View.Gone


        // 데이터가 없으면
        // cl은 View.Gone
    }

    override fun onResume() {
        super.onResume()
        // 근무자 추가 activity 를 완료한 후 돌아왔을 때
        checkExistState()
    }


//    override fun onGetSuccess(response: GetWorkerListResponse) {
//        dismissLoadingDialog()
//        if(response.code == 200){
//
//            showCustomToast("성공")
//
//        }
//    }

//    override fun onGetSuccess(response: GetWorkerListResponse, workerListData: WorkerListData) {
//        dismissLoadingDialog()
//        if(response.code == 200){
//
//            showCustomToast("성공")
//
//        }
//    }
//
//    override fun onGetFailure(message: String) {
//       dismissLoadingDialog()
//    }
}

