package com.example.albazip.src.home.manager.worklist.ui

import android.os.Bundle
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityAddPersonalWorkListBinding
import com.example.albazip.src.home.manager.data.SelectPersonalWorkerData
import com.example.albazip.src.mypage.manager.workerlist.data.local.TodoData

class AddPersonalWorkActivity:BaseActivity<ActivityAddPersonalWorkListBinding>(ActivityAddPersonalWorkListBinding::inflate) {

    // 포지션 선택 배열
    private var personalList = ArrayList<SelectPersonalWorkerData>()

    // 업무 내용 담는 배열
    private var todoList = ArrayList<TodoData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}