package com.example.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.cotask

import android.os.Bundle
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityTogetherWorkBinding
import com.example.albazip.src.mypage.worker.adapter.OutWorkListAdapter
import com.example.albazip.src.mypage.worker.data.local.OutWorkListData

class DetailTogetherWorkActivity:BaseActivity<ActivityTogetherWorkBinding>(ActivityTogetherWorkBinding::inflate) {

    private lateinit var outWorkListAdapter: OutWorkListAdapter

    // out rv를 담을 배열
    val outWorkList = ArrayList<OutWorkListData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}