package com.playground.albazip.src.mypage.manager.workerlist.cardevent.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.playground.albazip.R
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.ChildFragmentCardToDoListBinding
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.data.PositionTaskList
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.network.PWorkListFragmentView
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.network.PWorkListService
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.network.PWorkResponse
import com.playground.albazip.src.mypage.worker.adapter.DailyUnDoneAdapter
import com.playground.albazip.src.mypage.worker.data.local.DailyUnDoneWorkListData

class CardToDoChildFragment(positionTaskList:ArrayList<PositionTaskList>,var positionId:Int):BaseFragment<ChildFragmentCardToDoListBinding>(ChildFragmentCardToDoListBinding::bind,
    R.layout.child_fragment_card_to_do_list),PWorkListFragmentView {

    private val getPositionTaskList = positionTaskList
    private val unDoneList = ArrayList<DailyUnDoneWorkListData>()

    private val getPositionId = positionId

    private lateinit var unDoneListAdapter: DailyUnDoneAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 새로고침 기능 달아주기
        binding.swipelayout.setOnRefreshListener {
            // 서버 통신 시도
            PWorkListService(this).tryGetWorkList(getPositionId)
        }

        // 작성글 존재여부 체크 -> UI 변경
        checkBoardEmpty()

        // 미완료 업무 리스트
        for(i in 0 until getPositionTaskList.size)
        unDoneList.add(DailyUnDoneWorkListData(getPositionTaskList[i].title, getPositionTaskList[i].content,getPositionTaskList[i].writerTitle+" "+getPositionTaskList[i].writerName+" · "+getPositionTaskList[i].registerDate.substring(0,10).replace("-",".")+"." ))

        unDoneListAdapter = DailyUnDoneAdapter(unDoneList,requireContext())
        binding.rvToDoList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvToDoList.adapter = unDoneListAdapter
    }

    private fun checkBoardEmpty() {
        if (getPositionTaskList.size == 0) {
            binding.clNoWroteList.visibility = View.VISIBLE
        }else{
            binding.clNoWroteList.visibility = View.GONE
        }
    }

    override fun onGetWorkListSuccess(response: PWorkResponse) {
        binding.swipelayout.isRefreshing = false // 새로고침 끝

        if(unDoneList.size != 0) { // 이전 데이터 제거
            unDoneList.clear()
            binding.rvToDoList.recycledViewPool.clear()
            unDoneListAdapter.notifyDataSetChanged()
        }

        if(response.data.size == 0){ // 업무리스트가 비어있다면
            binding.clNoWroteList.visibility = View.VISIBLE
        }else{
            for(i in 0 until response.data.size){
                unDoneList.add(DailyUnDoneWorkListData(response.data[i].title, response.data[i].content,response.data[i].writerTitle+" "+response.data[i].writerName+" · "+response.data[i].registerDate.substring(0,10).replace("-",".")+"." ))

                unDoneListAdapter = DailyUnDoneAdapter(unDoneList,requireContext())
                binding.rvToDoList.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.rvToDoList.adapter = unDoneListAdapter
            }
        }

        if (unDoneList.size == 0) {
            binding.clNoWroteList.visibility = View.VISIBLE
        }else{
            binding.clNoWroteList.visibility = View.GONE
        }

    }

    override fun onGetWorkListFailure(message: String) {
        binding.swipelayout.isRefreshing = false // 새로고침 끝
    }

}