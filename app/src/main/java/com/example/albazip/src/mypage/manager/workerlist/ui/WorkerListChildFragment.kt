package com.example.albazip.src.mypage.manager.workerlist.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.example.albazip.R
import com.example.albazip.config.ApplicationClass.Companion.prefs
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentWorkerListBinding
import com.example.albazip.src.mypage.manager.init.data.WorkerList
import com.example.albazip.src.mypage.manager.workerlist.adapter.WorkerCardAdapter
import com.example.albazip.src.mypage.manager.workerlist.cardevent.ui.CardInfoFragment
import com.example.albazip.src.mypage.manager.workerlist.data.local.CardData
import com.google.android.gms.dynamic.SupportFragmentWrapper

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
        workerCardAdapter = WorkerCardAdapter(cardList,requireContext())
        binding.rvWorkerList.adapter = workerCardAdapter

        // 근무자 카드를 클릭 했을 때
        workerCardAdapter.setOnItemClickListener(object :WorkerCardAdapter.OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {

                // 1. 근무자 부재
                if(cardList[position].status == 0){
                    showCustomToast("근무자 부재")
                    parentFragmentManager.beginTransaction().add(R.id.manager_main_frm,CardInfoFragment()).addToBackStack(null).commit()
                    prefs.setInt("backStackState",1)
                }else{// 2. 근무자 존재

                }
            }

        })

        // 근무자 리스트 조회
        //WorkerListService(this).tryGetWorkerList()
        //showLoadingDialog(requireContext())
    }


    override fun onResume() {
        super.onResume()

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