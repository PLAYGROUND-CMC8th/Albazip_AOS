package com.example.albazip.src.mypage.worker.myInfo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.R
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityLateCheckBinding
import com.example.albazip.src.mypage.worker.adapter.WBoardListAdapter
import com.example.albazip.src.mypage.worker.adapter.WLateRecordAdapter
import com.example.albazip.src.mypage.worker.data.local.WLateRecordData

class LateCheckActivity:BaseActivity<ActivityLateCheckBinding>(ActivityLateCheckBinding::inflate) {

    private lateinit var lateRecordAdapter:WLateRecordAdapter
    private val lateRecordList = ArrayList<WLateRecordData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lateRecordList.add(WLateRecordData("2021.07.03.","08:20","13:30"))
        lateRecordList.add(WLateRecordData("2021.07.03.","08:20","13:30"))
        lateRecordList.add(WLateRecordData("2021.07.03.","08:20","13:30"))
        lateRecordList.add(WLateRecordData("2021.07.03.","08:20","13:30"))
        lateRecordList.add(WLateRecordData("2021.07.03.","08:20","13:30"))
        lateRecordList.add(WLateRecordData("2021.07.03.","08:20","13:30"))
        lateRecordList.add(WLateRecordData("2021.07.03.","08:20","13:30"))


        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvRecord.layoutManager = linearLayoutManager

        // divider custom
        val dividerItemDecoration = DividerItemDecoration(this,1)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.line_divider_late, null))

        // 아이템 구분선 삽입
        binding.rvRecord.addItemDecoration(dividerItemDecoration)

        lateRecordAdapter = WLateRecordAdapter(lateRecordList)
        binding.rvRecord.adapter = lateRecordAdapter

    }
}