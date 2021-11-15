package com.example.albazip.src.mypage.worker.myInfo.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityTogetherWorkBinding
import com.example.albazip.src.mypage.common.workerdata.cotask.data.GetCoTaskInfoResponse
import com.example.albazip.src.mypage.common.workerdata.cotask.network.CoTaskFragmentView
import com.example.albazip.src.mypage.common.workerdata.cotask.network.CoTaskService
import com.example.albazip.src.mypage.worker.adapter.OutWorkListAdapter
import com.example.albazip.src.mypage.worker.data.local.InWorkListData
import com.example.albazip.src.mypage.worker.data.local.OutWorkListData

class TogetherWorkActivity:BaseActivity<ActivityTogetherWorkBinding>(ActivityTogetherWorkBinding::inflate), CoTaskFragmentView{
    private lateinit var outWorkListAdapter:OutWorkListAdapter

    // 날짜는 최신순으로 내림차순 정렬
    // 날짜를 담을 배열 생성
    val dateList = ArrayList<String>()

    // contents 를 담을 배열 생성
    val showingDateList = ArrayList<String>() // "d_2011-11-15"


    // out rv를 담을 배열
    val outWorkList = ArrayList<OutWorkListData>()

    // in rv를 담을 배열
    val inWorkListOne = ArrayList<InWorkListData>()
    val inWorkListTwo = ArrayList<InWorkListData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoTaskService(this).tryCoTask()
        showLoadingDialog(this)

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

    override fun onCoTaskGetSuccess(response: GetCoTaskInfoResponse) {
        dismissLoadingDialog()

        if (response.code == 200) {
            // 완료 업무 수 표시
            binding.tvLateCnt.text = response.data.size.toString()

            // 날짜 리스트 배열에 저장하기
            for (i in 0 until response.data.size)
                dateList.add(response.data[i].complete_date.substring(0, 10))

            // 날짜 리스트 중복 제거하기
            val pureDateList = dateList.distinct()

            for (i in 0 until response.data.size) {
                for (j in 0 until pureDateList.size) {
                    if (response.data[i].complete_date.substring(0, 10)
                            .contains(pureDateList[j])
                    ) { // 일치하는 날짜 정보를 찾았을 때

                        // 이미 이전에 날짜 정보 배열을 받았으면 값만 저장하기
                        if (showingDateList.contains("d_" + pureDateList[j])) {

                        } else {
                            // 해당 날짜 정보의 배열을 생성하고 값 저장하기
                            showingDateList.add("d_" + pureDateList[j]) // "d_2021_11_01"
                        }
                    }
                }



              for(i in 0 until response.data.size){

              }
                showCustomToast(showingDateList.toString())

            }

            // showCustomToast(response.message.toString())
        }
    }

    override fun onCoTaskGetFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }
}