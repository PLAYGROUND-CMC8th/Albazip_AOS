package com.example.albazip.src.mypage.manager

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.ImageButton
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityAddWorkerTwoBinding
import com.example.albazip.src.main.MainActivity
import com.example.albazip.src.mypage.manager.adapter.ToDoListAdapter
import com.example.albazip.src.mypage.manager.data.local.TodoData
import okhttp3.internal.notifyAll

class AddWorkerTwoActivity:BaseActivity<ActivityAddWorkerTwoBinding>(ActivityAddWorkerTwoBinding::inflate) {

    private val toDoList = ArrayList<TodoData>()
    private lateinit var todoAdapter:ToDoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // recyclerview 연결
        todoAdapter = ToDoListAdapter(toDoList)

        // 리사이클러 뷰 타입 설정
        // 만든 어댑터 recyclerview에 연결
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvToDoList.layoutManager = linearLayoutManager

        binding.rvToDoList.adapter = todoAdapter

        // 업무추가 버튼 누르면 recyclerview 등장
        binding.clAddToDo.setOnClickListener {
            if(binding.rvToDoList.visibility == View.GONE) {
                binding.rvToDoList.visibility = View.VISIBLE
            }

            toDoList.add(TodoData("",""))
            todoAdapter.notifyDataSetChanged()
        }

        // 완료 버튼 -> 근무자 추가 POST
        // BUT 타이틀 없으면 등록 x
        binding.tvNext.setOnClickListener {
            // recyclerview 데이터가 하나라도 존재할 때
            if(binding.rvToDoList.isNotEmpty()){
                // title 값 체크하고 서버통신
                for(i in 0 until todoAdapter.itemCount) {
                    if (todoAdapter.itemList[i].titleTxt.isEmpty()) { // '제목'부분이 비어있다면
                        showCustomToast("작성이 미완료된 업무가 있습니다!")
                        break
                    }
                }
            }else{ // 서버 통신 시작
                //val workerDataList: ArrayList<Any> =
                //    intent.getSerializableExtra("workerDataList") as ArrayList<Any>

            }
        }
    }
}