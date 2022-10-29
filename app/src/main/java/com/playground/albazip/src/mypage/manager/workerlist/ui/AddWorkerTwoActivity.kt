package com.playground.albazip.src.mypage.manager.workerlist.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityAddWorkerTwoBinding
import com.playground.albazip.src.mypage.manager.adapter.ToDoListAdapter
import com.playground.albazip.src.mypage.manager.workerlist.data.local.TodoData
import com.playground.albazip.src.update.setworker.network.MMyPageService
import com.playground.albazip.src.update.setworker.network.RequestAddPosition
import com.playground.albazip.util.enqueueUtil

class AddWorkerTwoActivity :
    BaseActivity<ActivityAddWorkerTwoBinding>(ActivityAddWorkerTwoBinding::inflate) {

    private val toDoList = ArrayList<TodoData>()
    private lateinit var todoAdapter: ToDoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뒤로가기 버튼
        binding.ibtnBack.setOnClickListener {
            finish()
        }

        // recyclerview 연결
        todoAdapter = ToDoListAdapter(toDoList, this@AddWorkerTwoActivity)

        // 리사이클러 뷰 타입 설정
        // 만든 어댑터 recyclerview에 연결
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvToDoList.layoutManager = linearLayoutManager

        binding.rvToDoList.itemAnimator = null
        binding.rvToDoList.animation = null
        binding.rvToDoList.adapter = todoAdapter

        // 업무추가 버튼 누르면 recyclerview 등장
        binding.clAddToDo.setOnClickListener {
            if (binding.rvToDoList.visibility == View.GONE) {
                binding.rvToDoList.visibility = View.VISIBLE
            }

            toDoList.add(TodoData("", ""))
            todoAdapter.notifyItemInserted(todoAdapter.itemList.lastIndex)
        }

        // 완료 버튼 -> 근무자 추가 POST
        // BUT 타이틀 없으면 등록 x
        binding.tvNext.setOnClickListener {
            val workerDataList: ArrayList<String> =
                intent.getSerializableExtra("workerStringList") as ArrayList<String>
            val workSchedule: ArrayList<RequestAddPosition.WorkSchedule> =
                intent.getSerializableExtra("workSchedule") as ArrayList<RequestAddPosition.WorkSchedule>

            todoAdapter.notifyItemRangeChanged(0, todoAdapter.itemList.size + 1)

            var taskList = ArrayList<TodoData>()
            taskList = todoAdapter.itemList

            Log.d("kite",taskList.toString())

            // recyclerview 데이터가 하나라도 존재할 때
            if (binding.rvToDoList.isNotEmpty()) {
                // title 값 체크하고 서버통신
                //for(i in 0 until todoAdapter.itemCount) {
                //if (todoAdapter.itemList[i].titleTxt.isEmpty()) { // '제목'부분이 비어있다면
                //    showCustomToast("작성이 미완료된 업무가 있습니다!")
                //    break
                //}else{
                val postRequest = RequestAddPosition(
                    rank = workerDataList[0].toString(),
                    title = workerDataList[1].toString(),
                    breakTime = workerDataList[2].toString(),
                    salary = workerDataList[3].toString(),
                    salaryType = workerDataList[4].toString(),
                    workSchedule = workSchedule,
                    taskLists = taskList
                )

                showLoadingDialog(this)
                tryPostWorker(postRequest)

                // }
                //}
            } else {
                //val workerDataList :ArrayList<Any> = arrayListOf(rank,title,startTime,endTime,workDays,breakTime,salary,salaryType)
                if (taskList.isEmpty()) {
                    val postRequest = RequestAddPosition(
                        rank = workerDataList[0].toString(),
                        title = workerDataList[1].toString(),
                        breakTime = workerDataList[2].toString(),
                        salary = workerDataList[3].toString(),
                        salaryType = workerDataList[4].toString(),
                        workSchedule = workSchedule,
                        taskLists = null
                    )

                    showLoadingDialog(this)
                    tryPostWorker(postRequest)

                }
            }

        }
    }

    private fun tryPostWorker(requestAddPosition: RequestAddPosition) {
        dismissLoadingDialog()
        val mMyPageService: MMyPageService =
            ApplicationClass.sRetrofit.create(MMyPageService::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        val call = mMyPageService.postAddPosition(token, requestAddPosition)
        call.enqueueUtil(
            getResultCode = { it.code },
            onSuccess200 = {
                showCustomToast("근무자 등록 성공")
                finish()
            },
            onError200 = { showCustomToast(it.message.toString()) },
            onError400 = { showCustomToast(it.message.toString()) }
        )
    }
}