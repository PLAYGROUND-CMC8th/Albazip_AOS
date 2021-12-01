package com.playground.albazip.src.mypage.worker.myInfo.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityWorkDoneBinding
import com.playground.albazip.src.mypage.common.workerdata.taskinfo.data.GetTaskRateResponse
import com.playground.albazip.src.mypage.common.workerdata.taskinfo.network.TaskRateFragmentView
import com.playground.albazip.src.mypage.common.workerdata.taskinfo.network.TaskRateService
import com.playground.albazip.src.mypage.worker.adapter.WorkDoneAdapter
import com.playground.albazip.src.mypage.worker.data.local.WorkListData

// 마이페이지 > 내정보 > 완료한 업무
class DoneWorkActivity : BaseActivity<ActivityWorkDoneBinding>(ActivityWorkDoneBinding::inflate),
    TaskRateFragmentView {

    private lateinit var workDoneAdapter: WorkDoneAdapter
    val workList = ArrayList<WorkListData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TaskRateService(this).tryGetTaskRate()
        showLoadingDialog(this)

        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onTaskRateGetSuccess(response: GetTaskRateResponse) {
        dismissLoadingDialog()

        // 업무 완수율
        binding.tvDonePercent.text = (((response.data.taskRate.completeTaskCount).toDouble() / (response.data.taskRate.totalTaskCount).toDouble()) * 100).toInt().toString()

        // 업무리스트가 비었을 때 UI 표시
        if(response.data.taskData.size == 0){
            binding.clNoListView.visibility = View.VISIBLE
        }

        // 업무리스트
        for (i in 0 until response.data.taskData.size) {
            workList.add(
                WorkListData(
                    response.data.taskData[i].year.substring(
                        2,
                        4
                    ) + "년 " + response.data.taskData[i].month + "월 업무",
                    response.data.taskData[i].completeCount,
                    response.data.taskData[i].totalCount
                )
            )
        }

        workDoneAdapter = WorkDoneAdapter(workList)
        binding.rvDoneWorkList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDoneWorkList.adapter = workDoneAdapter

        workDoneAdapter.setOnItemClickListener(object : WorkDoneAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                // toolbar 의 타이틀을 넘겨준다.
                val nextIntent = Intent(this@DoneWorkActivity, SelectMonthListActivity::class.java)
                nextIntent.putExtra("title", workList[position].monthTxt)
                startActivity(nextIntent)
            }
        })

    }

    override fun onTaskRateGetFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }
}