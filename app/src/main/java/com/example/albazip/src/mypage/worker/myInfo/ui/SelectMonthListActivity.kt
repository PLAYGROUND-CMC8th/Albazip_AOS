package com.example.albazip.src.mypage.worker.myInfo.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivitySelectMonthListBinding
import com.example.albazip.src.mypage.common.workerdata.taskinfo.data.GetTaskRateResponse
import com.example.albazip.src.mypage.common.workerdata.taskinfo.network.TaskRateFragmentView
import com.example.albazip.src.mypage.common.workerdata.taskinfo.network.TaskRateService
import com.example.albazip.src.mypage.worker.adapter.WorkDoneAdapter
import com.example.albazip.src.mypage.worker.data.local.WorkListData

class SelectMonthListActivity:BaseActivity<ActivitySelectMonthListBinding>(ActivitySelectMonthListBinding::inflate) {

    private lateinit var workDoneAdapter: WorkDoneAdapter
    private val monthList = ArrayList<WorkListData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 이전 액티비티에서 title 받아오기
        if(intent.hasExtra("title")) {
            binding.tvMonthTitle.text = intent.getStringExtra("title")
        }

        monthList.add(WorkListData("9/23 목요일 업무",4,7))
        monthList.add(WorkListData("9/22 수요일 업무",7,7))
        monthList.add(WorkListData("9/21 화요일 업무",7,7))
        monthList.add(WorkListData("9/20 월요일 업무",7,7))


        workDoneAdapter = WorkDoneAdapter(monthList)
        binding.rvDoneWorkList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvDoneWorkList.adapter = workDoneAdapter

        workDoneAdapter.setOnItemClickListener(object : WorkDoneAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                // toolbar 의 타이틀을 넘겨준다.
                val nextIntent = Intent(this@SelectMonthListActivity, SelectDayListActivity::class.java)
                nextIntent.putExtra("title", monthList[position].monthTxt)
                startActivity(nextIntent)
            }
        })

    }

}