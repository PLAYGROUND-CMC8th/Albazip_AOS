package com.playground.albazip.src.mypage.manager.workerlist.editposition.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.databinding.ActivityEditWorkerTwoBinding
import com.playground.albazip.src.mypage.manager.adapter.EditTodoListAdapter
import com.playground.albazip.src.mypage.manager.workerlist.data.local.EditTodoData
import com.playground.albazip.src.mypage.manager.workerlist.editposition.network.*
import com.playground.albazip.src.update.setworker.network.RequestAddPosition

class EditWorkerTwoActivity :
    BaseActivity<ActivityEditWorkerTwoBinding>(ActivityEditWorkerTwoBinding::inflate),
    GetPositionInfoFragmentView, PostPositionInfoFragmentView {

    private var toDoList = ArrayList<EditTodoData>()
    private lateinit var todoAdapter: EditTodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 편집 정보 조회하기
        val intentPositionId = intent.getIntExtra("positionId", 0)
        GetPositionInfoService(this).tryGetPositionInfo(intentPositionId)
        showLoadingDialog(this)
    }

    private fun initAdapter() {
        val intentPositionId = intent.getIntExtra("positionId", 0)

        // 뒤로가기
        binding.ibtnBack.setOnClickListener {
            finish()
        }

        // 편집완료
        binding.tvNext.setOnClickListener {

            val workerDataList: ArrayList<String> =
                intent.getSerializableExtra("workerStringList") as ArrayList<String>
            val workSchedule: ArrayList<RequestAddPosition.WorkSchedule> =
                intent.getSerializableExtra("workSchedule") as ArrayList<RequestAddPosition.WorkSchedule>
            workSchedule.toMutableSet().toMutableList()

            Log.d("kite", workSchedule.toString())
            todoAdapter.notifyItemRangeChanged(0, todoAdapter.itemList.size + 1)

            val salaryType = when (workerDataList[4]) {
                "시급" -> 0
                "주급" -> 1
                "월급" -> 2
                else -> {
                    0
                }
            }

            var taskList = ArrayList<EditTodoData>()
            taskList = todoAdapter.itemList

            // recyclerview 데이터가 하나라도 존재할 때
            if (binding.rvToDoList.isNotEmpty()) {
                // title 값 체크하고 서버통신
                //for(i in 0 until todoAdapter.itemCount) {
                //if (todoAdapter.itemList[i].titleTxt.isEmpty()) { // '제목'부분이 비어있다면
                //    showCustomToast("작성이 미완료된 업무가 있습니다!")
                //    break
                //}else{
                val postRequest = PostEditWorkerRequest(
                    rank = workerDataList[0],
                    title = workerDataList[1],
                    breakTime = workerDataList[2],
                    salary = workerDataList[3].replace(",", ""),
                    salaryType = salaryType,
                    workSchedule = workSchedule,
                    taskList = taskList
                )

                showLoadingDialog(this)
                PostPositionInfoService(this).tryPostPositionInfo(intentPositionId, postRequest)

                Log.d("kite", postRequest.toString())
            } else {
                //val workerDataList :ArrayList<Any> = arrayListOf(rank,title,startTime,endTime,workDays,breakTime,salary,salaryType)
                if (taskList.isEmpty()) {
                    val postRequest = PostEditWorkerRequest(
                        rank = workerDataList[0],
                        title = workerDataList[1],
                        breakTime = workerDataList[2],
                        salary = workerDataList[3].replace(",", ""),
                        salaryType = salaryType,
                        workSchedule = workSchedule,
                        taskList = null
                    )

                    showLoadingDialog(this)
                    PostPositionInfoService(this).tryPostPositionInfo(intentPositionId, postRequest)

                    Log.d("kite", postRequest.toString())

                    Log.d("kite", intentPositionId.toString())
                }
            }

        }

        // 업무추가 버튼 누르면 recyclerview 등장
        binding.clAddToDo.setOnClickListener {
            if (binding.rvToDoList.visibility == View.GONE) {
                binding.rvToDoList.visibility = View.VISIBLE
            }

            toDoList.add(EditTodoData(null, "", ""))
            todoAdapter.notifyDataSetChanged()
        }
    }

    override fun onGetPositionInfoSuccess(response: GetPositionInfoResponse) {
        dismissLoadingDialog()

        for (i in 0 until response.data.taskList.size) {
            toDoList.add(
                EditTodoData(
                    response.data.taskList[i].id,
                    response.data.taskList[i].title,
                    response.data.taskList[i].content
                )
            )
        }

        // recyclerview 연결
        todoAdapter = EditTodoListAdapter(toDoList, this@EditWorkerTwoActivity)

        // 리사이클러 뷰 타입 설정
        // 만든 어댑터 recyclerview에 연결
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvToDoList.layoutManager = linearLayoutManager

        binding.rvToDoList.adapter = todoAdapter

        initAdapter()
    }

    override fun onGetPositionInfoFailure(message: String) {
        dismissLoadingDialog()
    }

    // 근무자 편집 성공
    override fun onPostPositionInfoSuccess(response: BaseResponse) {
        dismissLoadingDialog()

        Log.d("kite", response.message.toString())

        finish()
    }

    override fun onPostPositionInfoFailure(message: String) {
        dismissLoadingDialog()
    }
}