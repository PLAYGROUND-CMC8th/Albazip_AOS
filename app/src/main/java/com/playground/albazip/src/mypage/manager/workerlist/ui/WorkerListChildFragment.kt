package com.playground.albazip.src.mypage.manager.workerlist.ui

import android.os.Bundle
import android.view.View
import com.playground.albazip.R
import com.playground.albazip.config.ApplicationClass.Companion.prefs
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.ChildFragmentWorkerListBinding
import com.playground.albazip.src.mypage.manager.init.data.WorkerList
import com.playground.albazip.src.mypage.manager.workerlist.adapter.WorkerCardAdapter
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.ui.CardExistWorkerFragment
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.ui.CardNoWorkerFragment
import com.playground.albazip.src.mypage.manager.workerlist.data.local.CardData
import com.playground.albazip.src.mypage.manager.workerlist.data.remote.GetWorkerListResponse
import com.playground.albazip.src.mypage.manager.workerlist.network.WorkListFragmentView
import com.playground.albazip.src.mypage.manager.workerlist.network.WorkerListService

class WorkerListChildFragment(serverCardList:ArrayList<WorkerList>) : BaseFragment<ChildFragmentWorkerListBinding>(
    ChildFragmentWorkerListBinding::bind,
    R.layout.child_fragment_worker_list
), WorkListFragmentView {
    // 직원카드 배열
    val getCardList = serverCardList
    private var cardList = ArrayList<CardData>()
    private lateinit var workerCardAdapter:WorkerCardAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 직원 명 수 띄우기
        binding.tvWorkerCnt.text = getCardList.size.toString()

        for(i in 0 until getCardList.size){
            cardList.add(CardData(getCardList[i].positionId,getCardList[i].status,getCardList[i].rank,getCardList[i].image_path.toString(),getCardList[i].title,getCardList[i].first_name))
        }
        workerCardAdapter = WorkerCardAdapter(cardList,requireContext())
        binding.rvWorkerList.adapter = workerCardAdapter

        cardClickEvent()

        // 근무자 카드 새로고침
        binding.swipelayout.setOnRefreshListener {
            WorkerListService(this).tryGetWorkerList()
        }

        // 근무자 리스트 조회
        //WorkerListService(this).tryGetWorkerList()
        //showLoadingDialog(requireContext())
    }


    override fun onResume() {
        super.onResume()
        //checkExistState()
    }

    fun cardClickEvent(){
        // 근무자 카드를 클릭 했을 때
        workerCardAdapter.setOnItemClickListener(object :WorkerCardAdapter.OnItemClickListener{
            override fun onItemClick(v: View, position: Int,outStatus:Int) {

                // 1. 근무자 부재
                if(cardList[position].status == 0){
                    parentFragmentManager.beginTransaction().add(R.id.manager_main_frm,CardNoWorkerFragment(cardList[position].positionId)).addToBackStack(null).commit()
                    prefs.setInt("backStackState",1)
                }else{// 2. 근무자 존재

                    if(outStatus == 1){ // 일반 근무자(status == 1)
                        parentFragmentManager.beginTransaction().add(R.id.manager_main_frm,CardExistWorkerFragment(cardList[position].positionId,1)).addToBackStack(null).commit()
                    }else{  // 퇴사요청 상태의 근무자 (status == 2)
                        parentFragmentManager.beginTransaction().add(R.id.manager_main_frm,CardExistWorkerFragment(cardList[position].positionId,2)).addToBackStack(null).commit()
                    }
                        prefs.setInt("backStackState",1)

                }
            }

        })
    }

    override fun onGetSuccess(response: GetWorkerListResponse) {
       binding.swipelayout.isRefreshing = false

        if(response.code == 200){

            cardList.clear() // 카드리스트 초기화

            for(i in 0 until response.data!!.size){
                cardList.add(CardData(response.data[i].positionId,response.data[i].status,response.data[i].rank,response.data[i].image_path.toString(),response.data[i].title,response.data[i].first_name))
            }
            workerCardAdapter = WorkerCardAdapter(cardList,requireContext())
            binding.rvWorkerList.adapter = workerCardAdapter

            cardClickEvent()
        }

    }

    override fun onGetFailure(message: String) {
        binding.swipelayout.isRefreshing = false
    }


    // 새로고침할 때 써먹어야쥥~ ㅎㅎ
//    override fun onGetSuccess(response: GetWorkerListResponse) {
//        dismissLoadingDialog()
//        if(response.code == 200){
//            showCustomToast("성공")
//        }
//    }
}