package com.example.albazip.src.mypage.manager.workerlist.cardevent.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentCardToDoListBinding
import com.example.albazip.src.mypage.manager.workerlist.cardevent.data.PositionTaskList
import com.example.albazip.src.mypage.worker.adapter.DailyUnDoneAdapter
import com.example.albazip.src.mypage.worker.data.local.DailyUnDoneWorkListData

class CardToDoChildFragment(positionTaskList:ArrayList<PositionTaskList>):BaseFragment<ChildFragmentCardToDoListBinding>(ChildFragmentCardToDoListBinding::bind,
    R.layout.child_fragment_card_to_do_list) {

    private val getPositionTaskList = positionTaskList
    private val unDoneList = ArrayList<DailyUnDoneWorkListData>()

    private lateinit var unDoneListAdapter: DailyUnDoneAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 미완료 업무 리스트
        for(i in 0 until getPositionTaskList.size)
        unDoneList.add(DailyUnDoneWorkListData(getPositionTaskList[i].title, getPositionTaskList[i].content,getPositionTaskList[i].writerTitle+" "+getPositionTaskList[i].writerName+" · "+getPositionTaskList[i].registerDate.substring(0,10).replace("-",".")+"." ))

        unDoneListAdapter = DailyUnDoneAdapter(unDoneList)
        binding.rvToDoList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvToDoList.adapter = unDoneListAdapter
    }
}