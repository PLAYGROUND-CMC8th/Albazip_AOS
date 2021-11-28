package com.example.albazip.src.mypage.manager.workerlist.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.R
import com.example.albazip.config.ApplicationClass.Companion.prefs
import com.example.albazip.config.BaseActivity
import com.example.albazip.config.BaseResponse
import com.example.albazip.databinding.ActivityAddWorkerTwoBinding
import com.example.albazip.src.main.ManagerMainActivity
import com.example.albazip.src.mypage.manager.adapter.ToDoListAdapter
import com.example.albazip.src.mypage.manager.workerlist.data.local.TodoData
import com.example.albazip.src.mypage.manager.workerlist.data.remote.PostAddWorkerRequest
import com.example.albazip.src.mypage.manager.workerlist.data.remote.TaskLists
import com.example.albazip.src.mypage.manager.workerlist.network.AddWorkerFragmentView
import com.example.albazip.src.mypage.manager.workerlist.network.AddWorkerService

class AddWorkerTwoActivity:BaseActivity<ActivityAddWorkerTwoBinding>(ActivityAddWorkerTwoBinding::inflate),AddWorkerFragmentView {

    private val toDoList = ArrayList<TodoData>()
    private lateinit var todoAdapter:ToDoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뒤로가기 버튼
        binding.ibtnBack.setOnClickListener {
            finish()
        }

        // recyclerview 연결
        todoAdapter = ToDoListAdapter(toDoList,this@AddWorkerTwoActivity)

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
            val workerDataList: ArrayList<Any> =
                intent.getSerializableExtra("workerDataList") as ArrayList<Any>

            val taskList = ArrayList<TaskLists>()
            todoAdapter.notifyDataSetChanged()

            if (todoAdapter.itemList.isNotEmpty()) {
                for (i in 0 until todoAdapter.itemCount) {
                    taskList.add(
                        TaskLists(
                            todoAdapter.itemList[i].titleTxt,
                            todoAdapter.itemList[i].contextTxt
                        )
                    )
                }
            }

            // recyclerview 데이터가 하나라도 존재할 때
            if(binding.rvToDoList.isNotEmpty()){
                // title 값 체크하고 서버통신
                //for(i in 0 until todoAdapter.itemCount) {
                    //if (todoAdapter.itemList[i].titleTxt.isEmpty()) { // '제목'부분이 비어있다면
                    //    showCustomToast("작성이 미완료된 업무가 있습니다!")
                    //    break
                    //}else{
                        val postRequest = PostAddWorkerRequest(
                            rank = workerDataList[0].toString(),
                            title = workerDataList[1].toString(),
                            startTime = workerDataList[2].toString(),
                            endTime = workerDataList[3].toString(),
                            workDays = workerDataList[4] as ArrayList<String>,
                            breakTime = workerDataList[5].toString(),
                            salary = workerDataList[6].toString(),
                            salaryType = workerDataList[7].toString(),
                            taskLists = taskList
                        )

                        showLoadingDialog(this)

                        AddWorkerService(this).tryPostWorker(postRequest)
                   // }
                //}
            }else {
                //val workerDataList :ArrayList<Any> = arrayListOf(rank,title,startTime,endTime,workDays,breakTime,salary,salaryType)
                if (taskList.isEmpty()) {
                    val postRequest = PostAddWorkerRequest(
                        rank = workerDataList[0].toString(),
                        title = workerDataList[1].toString(),
                        startTime = workerDataList[2].toString(),
                        endTime = workerDataList[3].toString(),
                        workDays = workerDataList[4] as ArrayList<String>,
                        breakTime = workerDataList[5].toString(),
                        salary = workerDataList[6].toString(),
                        salaryType = workerDataList[7].toString(),
                        taskLists = null
                    )

                    showLoadingDialog(this)

                    AddWorkerService(this).tryPostWorker(postRequest)

                }
            }

        }
    }


    override fun onPostSuccess(response: BaseResponse) {
        dismissLoadingDialog()
        if(response.code == 200){ // 근무자 추가 완료(=포지션 등록완료)
            // activity 종료
            finish()
        }else{
            showCustomToast(response.message.toString())
        }
    }

    override fun onPostFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }
}