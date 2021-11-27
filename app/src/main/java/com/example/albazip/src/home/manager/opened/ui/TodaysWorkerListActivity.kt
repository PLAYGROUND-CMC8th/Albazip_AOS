package com.example.albazip.src.home.manager.opened.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityTodayWorkerListBinding
import com.example.albazip.src.home.manager.opened.adapter.TodaysWorkerAdapter
import com.example.albazip.src.home.manager.opened.data.TodayWorkerData
import com.example.albazip.src.home.manager.opened.network.GetTodayWorkerFragmentView
import com.example.albazip.src.home.manager.opened.network.GetTodayWorkerResponse
import com.example.albazip.src.home.manager.opened.network.GetTodaysWorkerService

class TodaysWorkerListActivity:BaseActivity<ActivityTodayWorkerListBinding>(ActivityTodayWorkerListBinding::inflate),GetTodayWorkerFragmentView {

    private var workerList = ArrayList<TodayWorkerData>()
    private lateinit var todaysWorkerAdapter:TodaysWorkerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GetTodaysWorkerService(this).tryGetTodayWorkers()
        showLoadingDialog(this)

        // 뒤로가기
        binding.ibtnBack.setOnClickListener {
            finish()
        }

    }

    // 오늘의 근무자 불러오기 성공
    override fun onGetTodayWorkersSuccess(response: GetTodayWorkerResponse) {
        dismissLoadingDialog()

        for(i in 0 until response.data.size)
        {
            workerList.add(TodayWorkerData(response.data[i].workerImage,response.data[i].workerTitle,response.data[i].workerName))
        }

        todaysWorkerAdapter = TodaysWorkerAdapter(workerList,this)

        binding.rvTodayWorkers.adapter = todaysWorkerAdapter

    }

    override fun onGetTodayWorkersFailure(message: String) {
        dismissLoadingDialog()
    }
}