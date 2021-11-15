package com.example.albazip.src.mypage.worker.myInfo.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivitySelectMonthListBinding
import com.example.albazip.src.mypage.common.workerdata.taskinfo.data.GetMonthTaskRateResponse
import com.example.albazip.src.mypage.common.workerdata.taskinfo.data.GetTaskRateResponse
import com.example.albazip.src.mypage.common.workerdata.taskinfo.network.MonthRateFragmentView
import com.example.albazip.src.mypage.common.workerdata.taskinfo.network.MonthRateService
import com.example.albazip.src.mypage.common.workerdata.taskinfo.network.TaskRateFragmentView
import com.example.albazip.src.mypage.common.workerdata.taskinfo.network.TaskRateService
import com.example.albazip.src.mypage.worker.adapter.WorkDoneAdapter
import com.example.albazip.src.mypage.worker.data.local.WorkListData

class SelectMonthListActivity:BaseActivity<ActivitySelectMonthListBinding>(ActivitySelectMonthListBinding::inflate),MonthRateFragmentView {

    private lateinit var workDoneAdapter: WorkDoneAdapter
    private val monthList = ArrayList<WorkListData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 이전 액티비티에서 title 받아오기
        if(intent.hasExtra("title")) {
            binding.tvMonthTitle.text = intent.getStringExtra("title")

            val year = "20"+binding.tvMonthTitle.text.substring(0,2)
            val month = binding.tvMonthTitle.text.substring(4,6)
            MonthRateService(this).tryGetTaskRate(year,month)
            showLoadingDialog(this)
        }
    }

    override fun onMonthRateGetSuccess(response: GetMonthTaskRateResponse) {
      dismissLoadingDialog()

      for(i in 0 until response.data.size)
          monthList.add(WorkListData(response.data[i].month+"/"+response.data[i].day+" 요일 업무",response.data[i].completeCount,response.data[i].totalCount))

        workDoneAdapter = WorkDoneAdapter(monthList)
        binding.rvDoneWorkList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvDoneWorkList.adapter = workDoneAdapter



        workDoneAdapter.setOnItemClickListener(object : WorkDoneAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                // toolbar 의 타이틀을 넘겨준다.
                val nextIntent = Intent(this@SelectMonthListActivity, SelectDayListActivity::class.java)
                nextIntent.putExtra("title", monthList[position].monthTxt)
                val year =  "20"+binding.tvMonthTitle.text.substring(0,2)
                nextIntent.putExtra("year",year)
                startActivity(nextIntent)
            }
        })
    }

    override fun onMonthRateGetFailure(message: String) {
        dismissLoadingDialog()
    }

}