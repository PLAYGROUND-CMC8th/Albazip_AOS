package com.playground.albazip.src.home.manager.worklist.ui

import android.os.Bundle
import android.view.View
import com.playground.albazip.R
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.ChildFragmentPersonalBinding
import com.playground.albazip.src.home.manager.adapter.PerWorkListAdapter
import com.playground.albazip.src.home.manager.data.HomeMPerWorkResponse
import com.playground.albazip.src.home.manager.data.HomePerWorkData
import com.playground.albazip.src.home.manager.network.GetHomeMPerService
import com.playground.albazip.src.home.manager.network.GetHomeMPerServiceFragmentView
import com.playground.albazip.src.home.manager.worklist.network.MTodayTaskResult

class ChildFragmentPersonal(data: MTodayTaskResult?):BaseFragment<ChildFragmentPersonalBinding>(ChildFragmentPersonalBinding::bind,
    R.layout.child_fragment_personal),GetHomeMPerServiceFragmentView {

    private var perWorkList = ArrayList<HomePerWorkData>()
    private lateinit var perListAdapter:PerWorkListAdapter

    private var ResultData = data

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ResultData?.perTask?.size != null) {
            for(i in 0 until ResultData?.perTask?.size!!){
                perWorkList.add(HomePerWorkData(ResultData!!.perTask[i].workerId,ResultData!!.perTask[i].workerTitle,ResultData!!.perTask[i].workerName,ResultData!!.perTask[i].completeCount,ResultData!!.perTask[i].totalCount))
            }

            perListAdapter = PerWorkListAdapter(perWorkList,requireContext())
            binding.rvPersonalWork.adapter = perListAdapter
        }

        checkingUI()
    }

    // UI 체크
    fun checkingUI(){
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
        // showLoadingDialog(requireContext())
    }

    override fun onGetMPerWorkSuccess(response: HomeMPerWorkResponse) {
       // dismissLoadingDialog()

        // 기존 데이터 비우기
        if(perWorkList.size != 0) {
            perWorkList.clear()
            binding.rvPersonalWork.recycledViewPool.clear()
            perListAdapter.notifyDataSetChanged()
        }

        if (response.data.size != 0) {
            for(i in 0 until response.data.size){
                perWorkList.add(HomePerWorkData(response.data[i].workerId,response.data[i].workerTitle,response.data[i].workerName,response.data[i].completeCount,response.data[i].totalCount))
            }
        }else{
            perWorkList.clear()
            binding.rvPersonalWork.recycledViewPool.clear()
            perListAdapter.notifyDataSetChanged()
        }

        perListAdapter = PerWorkListAdapter(perWorkList,requireContext())
        binding.rvPersonalWork.adapter = perListAdapter
    }

    override fun onGetMPerWorkFailure(message: String) {
        dismissLoadingDialog()

    }
}