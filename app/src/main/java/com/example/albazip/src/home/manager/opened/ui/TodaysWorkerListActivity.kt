package com.example.albazip.src.home.manager.opened.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityTodayWorkerListBinding
import com.example.albazip.src.home.manager.opened.adapter.TodaysWorkerAdapter
import com.example.albazip.src.home.manager.opened.data.TodayWorkerData

class TodaysWorkerListActivity:BaseActivity<ActivityTodayWorkerListBinding>(ActivityTodayWorkerListBinding::inflate) {

    private lateinit var workerList:ArrayList<TodayWorkerData>
    private lateinit var todaysWorkerAdapter:TodaysWorkerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        workerList = ArrayList<TodayWorkerData>()

        workerList.add(TodayWorkerData("","평일마감","주연"))
        workerList.add(TodayWorkerData("","평일미들","수빈"))
        workerList.add(TodayWorkerData("","평일마감","히영"))
        workerList.add(TodayWorkerData("","평일마감","지연"))

        todaysWorkerAdapter = TodaysWorkerAdapter(workerList)

        binding.rvTodayWorkers.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvTodayWorkers.adapter = todaysWorkerAdapter


    }
}