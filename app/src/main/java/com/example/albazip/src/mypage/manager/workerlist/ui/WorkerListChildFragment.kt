package com.example.albazip.src.mypage.manager.workerlist.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentWorkerListBinding
import com.example.albazip.src.mypage.manager.init.data.WorkerList
import com.example.albazip.src.mypage.manager.workerlist.adapter.WorkerCardAdapter
import com.example.albazip.src.mypage.manager.workerlist.data.local.CardData

class WorkerListChildFragment(serverCardList:ArrayList<WorkerList>) : BaseFragment<ChildFragmentWorkerListBinding>(
    ChildFragmentWorkerListBinding::bind,
    R.layout.child_fragment_worker_list
) {
    // 직원카드 배열
    val getCardList = serverCardList
    private var cardList = ArrayList<CardData>()
    private lateinit var workerCardAdapter:WorkerCardAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 직원 명 수 띄우기
        binding.tvWorkerCnt.text = getCardList.size.toString()

        for(i in 0 until getCardList.size){
            cardList.add(CardData(getCardList[i].status,getCardList[i].rank,getCardList[i].image_path.toString(),getCardList[i].title,getCardList[i].first_name))
        }


        //binding.rvWorkerList.layoutManager = GridLayoutManager(context,3)
        workerCardAdapter = WorkerCardAdapter(cardList,requireContext())
        binding.rvWorkerList.adapter = workerCardAdapter

        // 근무자 리스트 조회
        //WorkerListService(this).tryGetWorkerList()
        //showLoadingDialog(requireContext())
    }


    override fun onResume() {
        super.onResume()
        // 근무자 추가 activity 를 완료한 후 돌아왔을 때
        //checkExistState()
    }


    // 새로고침할 때 써먹어야쥥~ ㅎㅎ
//    override fun onGetSuccess(response: GetWorkerListResponse) {
//        dismissLoadingDialog()
//        if(response.code == 200){
//            showCustomToast("성공")
//        }
//    }
}