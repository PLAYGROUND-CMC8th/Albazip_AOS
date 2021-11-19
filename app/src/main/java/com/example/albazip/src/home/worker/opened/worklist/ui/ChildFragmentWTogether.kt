package com.example.albazip.src.home.worker.opened.worklist.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentPersonalBinding
import com.example.albazip.databinding.ChildFragmentWTodoListBinding
import com.example.albazip.databinding.DialogTodoAllDoneBinding
import com.example.albazip.src.home.worker.opened.worklist.adapter.HDoneAdapter
import com.example.albazip.src.home.worker.opened.worklist.adapter.HTogetherDoneAdapter
import com.example.albazip.src.home.worker.opened.worklist.adapter.HUnDoneAdapter
import com.example.albazip.src.home.worker.opened.worklist.data.HDoneWorkListData
import com.example.albazip.src.home.worker.opened.worklist.data.HUnDoneWorkListData

class ChildFragmentWTogether: BaseFragment<ChildFragmentWTodoListBinding>(
    ChildFragmentWTodoListBinding::bind,
    R.layout.child_fragment_w_todo_list) {

    // 미완료 리스트
    private var unDoneList = ArrayList<HUnDoneWorkListData>()
    private lateinit var unDoneAdapter:HUnDoneAdapter

    // 완료 리스트
    private var doneList = ArrayList<HDoneWorkListData>()
    private lateinit var doneAdpater:HTogetherDoneAdapter

    // 다이얼로그 바인딩
    private lateinit var dialogBinding: DialogTodoAllDoneBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogBinding = DialogTodoAllDoneBinding.inflate(layoutInflater)

        // 미완료 리스트가 없으면 (배열 개수 0)

        unDoneList.add(HUnDoneWorkListData(0,"제목1","내용1","작성 날짜 및 작성자"))
        unDoneList.add(HUnDoneWorkListData(0,"제목2","내용2","작성 날짜 및 작성자"))
        unDoneList.add(HUnDoneWorkListData(1,"제목3","내용3","작성 날짜 및 작성자"))
        unDoneList.add(HUnDoneWorkListData(1,"제목4","내용4","작성 날짜 및 작성자"))

        unDoneAdapter = HUnDoneAdapter(unDoneList,requireContext(),dialogBinding.root)
        binding.rvUndone.adapter = unDoneAdapter


        // 완료 리스트가 없으면 (배열 개수 0)
        doneList.add(HDoneWorkListData(0,"제목1","시간"))
        doneList.add(HDoneWorkListData(1,"제목2","시간"))
        doneList.add(HDoneWorkListData(1,"제목3","시간"))
        doneList.add(HDoneWorkListData(0,"제목4","시간"))

        doneAdpater = HTogetherDoneAdapter(parentFragmentManager,requireContext(),doneList)
        binding.rvDone.adapter = doneAdpater

        // 완료자 목록 확인

    }
}