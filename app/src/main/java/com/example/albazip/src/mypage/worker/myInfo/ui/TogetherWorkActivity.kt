package com.example.albazip.src.mypage.worker.myInfo.ui

import android.os.Bundle
import android.util.Log
import android.view.VerifiedInputEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityTogetherWorkBinding
import com.example.albazip.src.mypage.common.workerdata.cotask.data.GetCoTaskInfoResponse
import com.example.albazip.src.mypage.common.workerdata.cotask.network.CoTaskFragmentView
import com.example.albazip.src.mypage.common.workerdata.cotask.network.CoTaskService
import com.example.albazip.src.mypage.worker.adapter.OutWorkListAdapter
import com.example.albazip.src.mypage.worker.data.local.InWorkListData
import com.example.albazip.src.mypage.worker.data.local.OutWorkListData
import com.example.albazip.src.mypage.worker.myInfo.data.AllCoWorkData

class TogetherWorkActivity:BaseActivity<ActivityTogetherWorkBinding>(ActivityTogetherWorkBinding::inflate),CoTaskFragmentView{
    private lateinit var outWorkListAdapter:OutWorkListAdapter

    // 모든 데이터 받아오기
    val saveAllData = ArrayList<AllCoWorkData>()

    // 날짜는 최신순으로 내림차순 정렬
    // 날짜를 담을 배열 생성
    val dateList = ArrayList<String>()

    // out rv를 담을 배열
    val outWorkList = ArrayList<OutWorkListData>()

    // in rv를 담을 배열
    val inWorkListOne = ArrayList<InWorkListData>()
    val inWorkListTwo = ArrayList<InWorkListData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoTaskService(this).tryCoTask()
        showLoadingDialog(this)

        binding.ibtnBack.setOnClickListener {
            finish()
        }

    }


    override fun onCoTaskGetSuccess(response: GetCoTaskInfoResponse) {
        dismissLoadingDialog()

        // 첫 번째 보조 rv 생성
        //inWorkListOne.add(InWorkListData("2021","우유배달 오면 오른쪽 냉장고에 정리","완료 10:23"))

        // 두 번째 보조 rv 생성
        //inWorkListTwo.add(InWorkListData("2021","우유배달 오면 오른쪽 냉장고에 정리","완료 10:23"))
        //inWorkListTwo.add(InWorkListData("2021","우유배달 오면 오른쪽 냉장고에 정리","완료 10:23"))

        // 본 rv 생성
        //outWorkList.add(OutWorkListData("2021.07.04.",inWorkListOne))

        binding.tvLateCnt.text = response.data.size.toString()

        if (response.data.size == 0){
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