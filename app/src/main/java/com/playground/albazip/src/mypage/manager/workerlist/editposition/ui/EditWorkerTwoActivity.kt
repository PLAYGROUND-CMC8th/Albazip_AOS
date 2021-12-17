package com.playground.albazip.src.mypage.manager.workerlist.editposition.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.databinding.ActivityEditWorkerTwoBinding
import com.playground.albazip.src.mypage.manager.adapter.EditTodoListAdapter
import com.playground.albazip.src.mypage.manager.workerlist.data.local.EditTodoData
import com.playground.albazip.src.mypage.manager.workerlist.editposition.network.*

class EditWorkerTwoActivity:BaseActivity<ActivityEditWorkerTwoBinding>(ActivityEditWorkerTwoBinding::inflate),
    GetPositionInfoFragmentView,PostPositionInfoFragmentView {

    private var toDoList = ArrayList<EditTodoData>()
    private lateinit var todoAdapter: EditTodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 편집 정보 조회하기
        val intentPositionId = intent.getIntExtra("positionId",0)
        GetPositionInfoService(this).tryGetPositionInfo(intentPositionId)
        showLoadingDialog(this)


        // 뒤로가기
        binding.ibtnBack.setOnClickListener { 
            finish()
        }

        // 편집완료
        binding.tvNext.setOnClickListener {

            // 이전 클래스에서 정보 받아오기
            val workerDataList: ArrayList<Any> =
                intent.getSerializableExtra("workerDataList") as ArrayList<Any>

            val taskList = ArrayList<PostEditTaskLists>()
            todoAdapter.notifyDataSetChanged()

            if (todoAdapter.itemList.isNotEmpty()) {
                for (i in 0 until todoAdapter.itemCount) {
                    taskList.add(
                        PostEditTaskLists(
                            null,
                            todoAdapter.itemList[i].titleTxt,
                            todoAdapter.itemList[i].contextTxt
                        )
                    )
                }
            }

            var getSalaryType = workerDataList[7].toString()
            when(getSalaryType){
                "시급" -> {getSalaryType = "0"}
                "주급" -> {getSalaryType = "1"}
                "월급" -> {getSalaryType = "2"}
            }

            // recyclerview 데이터가 하나라도 존재할 때
            if(binding.rvToDoList.isNotEmpty()){
                // title 값 체크하고 서버통신
                for(i in 0 until todoAdapter.itemCount) {
                if (todoAdapter.itemList[i].titleTxt.isEmpty()) { // '제목'부분이 비어있다면
                    showCustomToast("작성이 미완료된 업무가 있습니다!")
                    break
                }  else{

                val postRequest = PostEditWorkerRequest(
                    rank = workerDataList[0].toString(),
                    title = workerDataList[1].toString(),
                    startTime = workerDataList[2].toString(),
                    endTime = workerDataList[3].toString(),
                    workDay = workerDataList[4] as ArrayList<String>,
                    breakTime = workerDataList[5].toString(),
                    salary = workerDataList[6].toString().replace(",",""),
                    salaryType = getSalaryType,
                    taskList = taskList
                )

                    PostPositionInfoService(this).tryPostPositionInfo(intentPositionId,postRequest)
                    showLoadingDialog(this)

                 }
                }
            }

            /*else {
                //val workerDataList :ArrayList<Any> = arrayListOf(rank,title,startTime,endTime,workDays,breakTime,salary,salaryType)
                if (taskList.isEmpty()) {
                    val postRequest = PostEditWorkerRequest(
                        rank = workerDataList[0].toString(),
                        title = workerDataList[1].toString(),
                        startTime = workerDataList[2].toString(),
                        endTime = workerDataList[3].toString(),
                        workDay = workerDataList[4] as ArrayList<String>,
                        breakTime = workerDataList[5].toString(),
                        salary = workerDataList[6].toString().replace(",",""),
                        salaryType = getSalaryType,
                        taskList = taskList
                    )

                    PostPositionInfoService(this).tryPostPositionInfo(intentPositionId,postRequest)
                    showLoadingDialog(this)

                }
            }*/


        }

        //toDoList = taskList as ArrayList<EditTodoData>

        // recyclerview 연결
//        todoAdapter = EditTodoListAdapter(toDoList,this@EditWorkerTwoActivity)
//
//        // 리사이클러 뷰 타입 설정
//        // 만든 어댑터 recyclerview에 연결
//        val linearLayoutManager = LinearLayoutManager(this)
//        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
//        binding.rvToDoList.layoutManager = linearLayoutManager
//
//        binding.rvToDoList.adapter = todoAdapter

        // 업무추가 버튼 누르면 recyclerview 등장
        binding.clAddToDo.setOnClickListener {
            if(binding.rvToDoList.visibility == View.GONE) {
                binding.rvToDoList.visibility = View.VISIBLE
            }

            toDoList.add(EditTodoData(null,"",""))
            todoAdapter.notifyDataSetChanged()
        }
    }

    override fun onGetPositionInfoSuccess(response: GetPositionInfoResponse) {
        dismissLoadingDialog()

        for (i in 0 until response.data.taskList.size){
            toDoList.add(EditTodoData(response.data.taskList[i].id,response.data.taskList[i].title,response.data.taskList[i].content))
        }

        // recyclerview 연결
        todoAdapter = EditTodoListAdapter(toDoList,this@EditWorkerTwoActivity)

        // 리사이클러 뷰 타입 설정
        // 만든 어댑터 recyclerview에 연결
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvToDoList.layoutManager = linearLayoutManager

        binding.rvToDoList.adapter = todoAdapter

    }

    override fun onGetPositionInfoFailure(message: String) {
        dismissLoadingDialog()
    }

    // 근무자 편집 성공
    override fun onPostPositionInfoSuccess(response: BaseResponse) {
        dismissLoadingDialog()
        finish()
    }

    override fun onPostPositionInfoFailure(message: String) {
        dismissLoadingDialog()
    }
}