package com.example.albazip.src.home.manager.worklist.ui

import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentPersonalBinding
import com.example.albazip.src.home.manager.adapter.PerWorkListAdapter
import com.example.albazip.src.home.manager.data.HomeMPerWorkResponse
import com.example.albazip.src.home.manager.data.HomePerWorkData
import com.example.albazip.src.home.manager.network.GetHomeMPerService
import com.example.albazip.src.home.manager.network.GetHomeMPerServiceFragmentView
import com.example.albazip.src.home.manager.worklist.network.MTodayTaskResult

class ChildFragmentPersonal(data: MTodayTaskResult?):BaseFragment<ChildFragmentPersonalBinding>(ChildFragmentPersonalBinding::bind,
    R.layout.child_fragment_personal),GetHomeMPerServiceFragmentView {

    private var perWorkList = ArrayList<HomePerWorkData>()
    private lateinit var perListAdapter:PerWorkListAdapter

    private var ResultData = data

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ResultData?.perTask?.size != null) {
            for(i in 0 until ResultData?.perTask?.size!!){
                perWorkList.add(HomePerWorkData(ResultData!!.perTask[i].workerId,ResultData!!.perTask[i].workerTitle,ResultData!!.perTask[i].completeCount,ResultData!!.perTask[i].totalCount))
            }

            perListAdapter = PerWorkListAdapter(perWorkList,requireContext())
            binding.rvPersonalWork.adapter = perListAdapter
        }

        // 등록된 업무가 없을 때
        if (perWorkList.size == 0){
            binding.clNoBothWork.visibility = View.VISIBLE
        }else{
            binding.clNoBothWork.visibility = View.GONE
        }
    }

    // 화면 갱신시 재조회
    override fun onResume() {
        super.onResume()
        GetHomeMPerService(this).tryGetHomeMPer()
        showLoadingDialog(requireContext())
    }

    override fun onGetMPerWorkSuccess(response: HomeMPerWorkResponse) {
       dismissLoadingDialog()
        showCustomToast(response.message.toString())
    }

    override fun onGetMPerWorkFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }
}