package com.example.albazip.src.home.manager.worklist.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isNotEmpty
import com.example.albazip.config.BaseActivity
import com.example.albazip.config.BaseResponse
import com.example.albazip.databinding.ActivityAddTogehterWorkListBinding
import com.example.albazip.src.home.manager.worklist.network.PostCoTask
import com.example.albazip.src.home.manager.worklist.network.PostCoWorkFragmentView
import com.example.albazip.src.home.manager.worklist.network.PostCoWorkRequest
import com.example.albazip.src.home.manager.worklist.network.PostTogetherWorkService
import com.example.albazip.src.main.ManagerMainActivity
import com.example.albazip.src.mypage.manager.adapter.ToDoListAdapter
import com.example.albazip.src.mypage.manager.workerlist.data.local.TodoData

class AddTogetherWorkListActivity:BaseActivity<ActivityAddTogehterWorkListBinding>(ActivityAddTogehterWorkListBinding::inflate),PostCoWorkFragmentView {

    private val toDoList = ArrayList<TodoData>()
    private lateinit var todoAdapter: ToDoListAdapter
    var stopFlags = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뒤로가기 버튼
        binding.ibtnBack.setOnClickListener {
            finish()
        }

        // recyclerview 연결
        todoAdapter = ToDoListAdapter(toDoList,this@AddTogetherWorkListActivity)
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

            stopFlags = 0

            val taskList = ArrayList<PostCoTask>()
            todoAdapter.notifyDataSetChanged()

            if (todoAdapter.itemList.isNotEmpty()) {
                for (i in 0 until todoAdapter.itemCount) {
                    taskList.add(
                        PostCoTask(
                            todoAdapter.itemList[i].titleTxt,
                            todoAdapter.itemList[i].contextTxt
                        )
                    )
                }
            }

            // recyclerview 데이터가 하나라도 존재할 때
            // title 값 체크하고 서버통신
            /*for(i in 0 until todoAdapter.itemCount) {
                if (todoAdapter.itemList[i].titleTxt.isEmpty()) { // '제목' 부분이 비어있다면
                    //binding.tvNext.isEnabled = false
                    //binding.tvNext.setTextColor(Color.parseColor("#adadad"))
                    //showCustomToast("작성이 미완료된 업무가 있습니다!")
                    showCustomToast("추가된 업무가 없습니다!")
                    break
                }
            }*/

            for (i in 0 until todoAdapter.itemCount){
                // 타이틀 o 하지만 내용 x
                if(todoAdapter.itemList[i].titleTxt.isEmpty() && todoAdapter.itemList[i].contextTxt.isNotEmpty()){
                    showCustomToast("작성이 미완료된 업무가 있습니다!")
                    stopFlags = 1 // 멈춰!
                }

                // 타이틀 내용 몽땅 x
                if(todoAdapter.itemList[i].titleTxt.isEmpty() && todoAdapter.itemList[i].contextTxt.isEmpty()){
                    showCustomToast("작성이 미완료된 업무가 있습니다!")
                    stopFlags = 1
                }
            }

            if(stopFlags != 1) {
                // 생성된 업무가 있을 때
                if (binding.rvToDoList.isNotEmpty()) {
                    val postRequest = PostCoWorkRequest(
                        coTaskList = taskList
                    )
                    showLoadingDialog(this) // 추가하기
                    PostTogetherWorkService(this).tryPostCoWorkInfo(postRequest)
                } else { // 비어있을 때
                    showCustomToast("추가된 업무가 없습니다!")
                    //setBtnIsActive()
                }

                // 추가하고 삭제햇을 때 전부 비게 될 때
                if (todoAdapter.itemList.isEmpty()) {
                    showCustomToast("추가된 업무가 없습니다!")
                    //setBtnIsActive()
                }
            }
        }
    }

    // 버튼 활성화 여부 체크
    fun setBtnIsActive(){
        if(toDoList.isEmpty()){ // 버튼 비활성화
            binding.tvNext.isEnabled = false
            binding.tvNext.setTextColor(Color.parseColor("#adadad"))
        }else{ // 버튼 활성화
            binding.tvNext.isEnabled = true
            binding.tvNext.setTextColor(Color.parseColor("#ffc400"))
        }
        todoAdapter.notifyDataSetChanged()
    }

    override fun onPostWorkSuccess(response: BaseResponse) {
        dismissLoadingDialog()
        val nextIntent = Intent(this,ManagerMainActivity::class.java)
        startActivity(nextIntent)
        finishAffinity()
    }

    override fun onPostWorkFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast("업무 추가 실패")
    }
}