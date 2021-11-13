package com.example.albazip.src.mypage.worker.myInfo.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityWorkDoneBinding
import com.example.albazip.src.mypage.worker.adapter.WorkDoneAdapter
import com.example.albazip.src.mypage.worker.data.local.WorkListData

// 마이페이지 > 내정보 > 완료한 업무
class DoneWorkActivity : BaseActivity<ActivityWorkDoneBinding>(ActivityWorkDoneBinding::inflate) {

    private lateinit var workDoneAdapter: WorkDoneAdapter
    val workList = ArrayList<WorkListData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        workList.add(WorkListData("21년 9월 업무", 150, 150))
        workList.add(WorkListData("21년 10월 업무", 20, 150))
        workList.add(WorkListData("21년 9월 업무", 100, 150))
        workList.add(WorkListData("21년 9월 업무", 10, 150))
        workList.add(WorkListData("21년 9월 업무", 80, 150))
        workList.add(WorkListData("21년 9월 업무", 46, 150))
        workList.add(WorkListData("21년 9월 업무", 80, 150))
        workList.add(WorkListData("21년 9월 업무", 46, 150))
        workList.add(WorkListData("21년 9월 업무", 80, 150))
        workList.add(WorkListData("21년 9월 업무", 46, 150))
        workList.add(WorkListData("21년 9월 업무", 80, 150))
        workList.add(WorkListData("21년 9월 업무", 46, 150))

        workDoneAdapter = WorkDoneAdapter(workList)
        binding.rvDoneWorkList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDoneWorkList.adapter = workDoneAdapter

        workDoneAdapter.setOnItemClickListener(object : WorkDoneAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                // toolbar 의 타이틀을 넘겨준다.
                val nextIntent = Intent(this@DoneWorkActivity, SelectMonthListActivity::class.java)
                nextIntent.putExtra("title", workList[position].monthTxt)
                startActivity(nextIntent)
            }
        })

    }
}