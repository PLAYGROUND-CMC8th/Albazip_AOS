package com.example.albazip.src.mypage.worker.myInfo

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityWorkDoneBinding
import com.example.albazip.src.mypage.worker.adapter.WorkDoneAdapter
import com.example.albazip.src.mypage.worker.data.local.WorkListData

// 마이페이지 > 내정보 > 완료한 업무
class DoneWorkActivity:BaseActivity<ActivityWorkDoneBinding>(ActivityWorkDoneBinding::inflate) {

    private lateinit var workDoneAdapter:WorkDoneAdapter
    val workList = ArrayList<WorkListData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        workList.add(WorkListData("21년 9월 업무",150,150))
        workList.add(WorkListData("21년 9월 업무",150,150))
        workList.add(WorkListData("21년 9월 업무",150,150))
        workList.add(WorkListData("21년 9월 업무",150,150))
        workList.add(WorkListData("21년 9월 업무",150,150))
        workList.add(WorkListData("21년 9월 업무",150,150))

        workDoneAdapter = WorkDoneAdapter(workList)
       binding.rvDoneWorkList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvDoneWorkList.adapter = workDoneAdapter
    }
}