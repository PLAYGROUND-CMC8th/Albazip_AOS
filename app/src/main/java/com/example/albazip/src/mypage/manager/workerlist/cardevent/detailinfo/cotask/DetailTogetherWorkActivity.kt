package com.example.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.cotask

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityTogetherWorkBinding
import com.example.albazip.src.mypage.common.workerdata.cotask.data.GetCoTaskInfoResponse
import com.example.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.cotask.network.DetailCoTaskFragmentView
import com.example.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.cotask.network.DetailCoTaskService
import com.example.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.taskinfo.network.DetailTaskRateFragmentView
import com.example.albazip.src.mypage.worker.adapter.OutWorkListAdapter
import com.example.albazip.src.mypage.worker.data.local.InWorkListData
import com.example.albazip.src.mypage.worker.data.local.OutWorkListData

class DetailTogetherWorkActivity:BaseActivity<ActivityTogetherWorkBinding>(ActivityTogetherWorkBinding::inflate),DetailCoTaskFragmentView {

    private lateinit var outWorkListAdapter: OutWorkListAdapter

    // out rv를 담을 배열
    val outWorkList = ArrayList<OutWorkListData>()

    // in rv를 담을 배열
    val inWorkListOne = ArrayList<InWorkListData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val positionId = intent.getIntExtra("positionId",0)

        DetailCoTaskService(this).tryGetDetailCoTask(positionId)
        showLoadingDialog(this)
    }

    override fun onCoTaskGetSuccess(response: GetCoTaskInfoResponse) {

        dismissLoadingDialog()

        binding.tvLateCnt.text = response.data.size.toString()

        if(response.data.size == 0){
            binding.llNoWorkList.visibility = View.VISIBLE
        }

        if(response.data.size !=0){
            for(i in 0 until response.data.size){

                inWorkListOne.add(InWorkListData(response.data[i].complete_date,response.data[i].title,response.data[i].complete_date.substring(0,10).replace("-",".")+"."))

            }
            outWorkList.add(OutWorkListData("완료 목록",inWorkListOne))
        }

        // out rv의 adapter 객체 생성 후 데이터 전달
        outWorkListAdapter = OutWorkListAdapter(this,outWorkList)

        // out rv의 레이아웃 설정
        binding.rvRecord.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)

        binding.rvRecord.adapter = outWorkListAdapter
    }

    override fun onCoTaskGetFailure(message: String) {
        dismissLoadingDialog()
    }
}