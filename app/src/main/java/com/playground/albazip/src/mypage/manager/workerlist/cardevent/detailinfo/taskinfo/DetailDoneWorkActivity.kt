package com.playground.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.taskinfo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityWorkDoneBinding
import com.playground.albazip.src.mypage.common.workerdata.taskinfo.data.GetTaskRateResponse
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.taskinfo.network.DetailTaskRateFragmentView
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.taskinfo.network.DetailTaskRateService
import com.playground.albazip.src.mypage.worker.adapter.WorkDoneAdapter
import com.playground.albazip.src.mypage.worker.data.local.WorkListData

class DetailDoneWorkActivity:BaseActivity<ActivityWorkDoneBinding>(ActivityWorkDoneBinding::inflate),DetailTaskRateFragmentView {

    private lateinit var workDoneAdapter: WorkDoneAdapter
    val workList = ArrayList<WorkListData>()

    var setPositionId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val positionId = intent.getIntExtra("positionId",0)
        setPositionId = positionId

        DetailTaskRateService(this).tryGetDetailTaskRate(positionId)
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
                val nextIntent = Intent(this@DetailDoneWorkActivity, DetailSelectMonthListActivity::class.java)
                nextIntent.putExtra("title", workList[position].monthTxt)
                nextIntent.putExtra("positionId",setPositionId)
                startActivity(nextIntent)
            }
        })
    }

    override fun onTaskRateGetFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }
}