package com.example.albazip.src.home.worker.opened.worklist.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentPersonalBinding
import com.example.albazip.databinding.ChildFragmentWTodoListBinding
import com.example.albazip.databinding.DialogTodoAllDoneBinding
import com.example.albazip.src.home.common.adapter.DoneWorkerCntAdapter
import com.example.albazip.src.home.common.data.DoneWorkerCntData
import com.example.albazip.src.home.worker.opened.worklist.adapter.HDoneAdapter
import com.example.albazip.src.home.worker.opened.worklist.adapter.HUnDoneAdapter
import com.example.albazip.src.home.worker.opened.worklist.data.HDoneWorkListData
import com.example.albazip.src.home.worker.opened.worklist.data.HUnDoneWorkListData

class ChildFragmentWPersonal: BaseFragment<ChildFragmentWTodoListBinding>(
    ChildFragmentWTodoListBinding::bind,
    R.layout.child_fragment_w_todo_list) {

    // 미완료 리스트
    private var unDoneList = ArrayList<HUnDoneWorkListData>()
    private lateinit var unDoneAdapter: HUnDoneAdapter

    // 완료 리스트
    private var doneList = ArrayList<HDoneWorkListData>()
    private lateinit var doneAdpater: HDoneAdapter

    // 다이얼로그 바인딩
    private lateinit var dialogBinding:DialogTodoAllDoneBinding

    // 인원수 체크 버튼 클릭 여부
    var clickedCnt = false
    private var doneWorkerCntList = ArrayList<DoneWorkerCntData>()
    private lateinit var doneWorkerCntAdapter:DoneWorkerCntAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogBinding = DialogTodoAllDoneBinding.inflate(layoutInflater)

        // 미완료 리스트가 없으면 (배열 개수 0)

        unDoneList.add(HUnDoneWorkListData(0,"제목1","내용1","작성 날짜 및 작성자"))
        unDoneList.add(HUnDoneWorkListData(0,"제목2","내용2","작성 날짜 및 작성자"))
        unDoneList.add(HUnDoneWorkListData(1,"제목3","내용3","작성 날짜 및 작성자"))
        unDoneList.add(HUnDoneWorkListData(1,"제목4","내용4","작성 날짜 및 작성자"))

        unDoneAdapter = HUnDoneAdapter(unDoneList,requireContext(),dialogBinding.root)

     //   val linearLayoutManager = LinearLayoutManager(requireContext())
     //   linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

     //   binding.rvUndone.layoutManager = linearLayoutManager
        binding.rvUndone.adapter = unDoneAdapter

        // 완료 리스트가 없으면 (배열 개수 0)
        doneList.add(HDoneWorkListData(0,"제목1","시간"))
        doneList.add(HDoneWorkListData(1,"제목2","시간"))
        doneList.add(HDoneWorkListData(1,"제목3","시간"))
        doneList.add(HDoneWorkListData(0,"제목4","시간"))

        doneAdpater = HDoneAdapter(requireContext(),doneList)
     //   binding.rvDone.layoutManager = linearLayoutManager
        binding.rvDone.adapter = doneAdpater


        doneWorkerCntList.add(DoneWorkerCntData("","평일마감","지연",1))
        doneWorkerCntList.add(DoneWorkerCntData("","평일마감","주연",2))
        doneWorkerCntList.add(DoneWorkerCntData("","평일마감","수빈",1))
        doneWorkerCntList.add(DoneWorkerCntData("","평일마감","희영",3))

        doneWorkerCntAdapter = DoneWorkerCntAdapter(doneWorkerCntList)
        binding.rvDoneWorkerList.adapter = doneWorkerCntAdapter

        // 인원수 체크 레이아웃
        binding.rlDonePersonCnt.setOnClickListener {

            if(clickedCnt == false) {
                // pop up 활성화 상태 -> 터치 대기
                clickedCnt = true
                // bg 도 변경하기
                binding.rlDonePersonCnt.background = ContextCompat.getDrawable(
                        requireContext(),
                R.drawable.rectangle_gray_radius_7
                )
                binding.frameLayoutCntDoneWorker.visibility = View.VISIBLE
            }else{
                clickedCnt = false
                binding.rlDonePersonCnt.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.rectangle_light_gray_radius_7
                )
                binding.frameLayoutCntDoneWorker.visibility = View.GONE
            }
        }
    }



}