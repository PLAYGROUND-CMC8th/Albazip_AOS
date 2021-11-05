package com.example.albazip.src.mypage.worker.myInfo

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityTogetherWorkBinding
import com.example.albazip.src.mypage.worker.adapter.OutWorkListAdapter
import com.example.albazip.src.mypage.worker.data.local.InWorkListData
import com.example.albazip.src.mypage.worker.data.local.OutWorkListData

class TogetherWorkActivity:BaseActivity<ActivityTogetherWorkBinding>(ActivityTogetherWorkBinding::inflate) {

    private lateinit var outWorkListAdapter:OutWorkListAdapter

    // out rv를 담을 배열
    val outWorkList = ArrayList<OutWorkListData>()

    // in rv를 담을 배열
    val inWorkListOne = ArrayList<InWorkListData>()
    val inWorkListTwo = ArrayList<InWorkListData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 첫 번째 보조 rv 생성
        inWorkListOne.add(InWorkListData("우유배달 오면 오른쪽 냉장고에 정리","완료 10:23"))

        // 두 번째 보조 rv 생성
        inWorkListTwo.add(InWorkListData("우유배달 오면 오른쪽 냉장고에 정리","완료 10:23"))
        inWorkListTwo.add(InWorkListData("우유배달 오면 오른쪽 냉장고에 정리","완료 10:23"))

        // 본 rv 생성
        outWorkList.add(OutWorkListData("2021.07.04.",inWorkListOne))
        outWorkList.add(OutWorkListData("2021.07.24.",inWorkListTwo))

        // out rv의 adapter 객체 생성 후 데이터 전달
        outWorkListAdapter = OutWorkListAdapter(this,outWorkList)

        // out rv의 레이아웃 설정
        binding.rvRecord.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)

        binding.rvRecord.adapter = outWorkListAdapter
    }
}