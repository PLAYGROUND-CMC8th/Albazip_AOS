package com.playground.albazip.src.mypage.worker.myInfo.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivitySelectDayListBinding
import com.playground.albazip.src.mypage.common.workerdata.taskinfo.data.GetDayTaskResponse
import com.playground.albazip.src.mypage.common.workerdata.taskinfo.network.DayTaskFragmentView
import com.playground.albazip.src.mypage.common.workerdata.taskinfo.network.DayTaskService
import com.playground.albazip.src.mypage.worker.adapter.DailyDoneAdapter
import com.playground.albazip.src.mypage.worker.adapter.DailyUnDoneAdapter
import com.playground.albazip.src.mypage.worker.data.local.DailyDoneWorkListData
import com.playground.albazip.src.mypage.worker.data.local.DailyUnDoneWorkListData

class SelectDayListActivity :
    BaseActivity<ActivitySelectDayListBinding>(ActivitySelectDayListBinding::inflate),DayTaskFragmentView {

    private val unDoneList = ArrayList<DailyUnDoneWorkListData>()
    private val doneList = ArrayList<DailyDoneWorkListData>()

    private lateinit var unDoneListAdapter: DailyUnDoneAdapter
    private lateinit var doneListAdapter: DailyDoneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 이전 액티비티에서 title 받아오기
        if (intent.hasExtra("title")) {
            binding.tvWeekTitle.text = intent.getStringExtra("title")
            val year = intent.getStringExtra("year")
            val month = binding.tvWeekTitle.text.substring(0,2)
            val date = binding.tvWeekTitle.text.substring(3,5)

            DayTaskService(this).tryGetDayTask(year!!,month,date)
            showLoadingDialog(this)
        }

        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 미완료 업무 리스트
        //unDoneList.add(DailyUnDoneWorkListData("간판 안으로 들여놓기", "문 뒤에 들여놓으세요.","사장님 김형준 · 2021.10.21."))

        // 완료 업무 리스트
        //doneList.add(DailyDoneWorkListData("에어콘 끄기", "완료 22:04"))

    }

    override fun onDayTaskGetSuccess(response: GetDayTaskResponse) {
        dismissLoadingDialog()

        // 미완료 업무 리스트 호출
        if(response.data.nonCompleteTaskData.size != 0) {
            for (i in 0 until response.data.nonCompleteTaskData.size)
                unDoneList.add(
                    DailyUnDoneWorkListData(
                        response.data.nonCompleteTaskData[i].title,
                        response.data.nonCompleteTaskData[i].content,
                        response.data.nonCompleteTaskData[i].writer_position + " "
                                + response.data.nonCompleteTaskData[i].writer_name + " · " + response.data.nonCompleteTaskData[i].register_date.substring(
                            0,
                            10
                        ).replace("-", ".") + "."
                    )
                )
        }

        // 완료 업무 리스트 호출
        if(response.data.completeTaskData.size != 0) {
            for (i in 0 until response.data.completeTaskData.size)
                doneList.add(
                    DailyDoneWorkListData(
                        response.data.completeTaskData[i].title,
                        "완료 " + response.data.completeTaskData[i].complete_date.substring(11, 16)
                    )
                )
        }

        // 미완료/완료 업무리스트 갯수 count
        binding.tvUndoneCnt.text = unDoneList.size.toString()
        binding.tvDoneCnt.text = doneList.size.toString()

        isListEmpty()
    }

    override fun onDayTaskGetFailure(message: String) {
        dismissLoadingDialog()
    }

    fun isListEmpty(){
        /////////////////////////////// 미완료 업무 리스트 연결 ////////////////////////////////////
        if(unDoneList.isEmpty()){
            binding.rlNoUndoneWork.visibility = View.VISIBLE
        }else{
            unDoneListAdapter = DailyUnDoneAdapter(unDoneList,this@SelectDayListActivity)
            binding.rvUndone.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvUndone.adapter = unDoneListAdapter
        }

        ////////////////////////////// 완료 업무 리스트 연결 ///////////////////////////////////////
        // 업무리스트가 비어있으면 -> rv 연결 x and visibility true 로 설정
        if (doneList.isEmpty()) {
            binding.rlNoDoneWork.visibility = View.VISIBLE
        } else {
            // 업무리스트가 존재하면 -> rv 연결 o
            doneListAdapter = DailyDoneAdapter(this@SelectDayListActivity, doneList)
            binding.rvDone.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvDone.adapter = doneListAdapter
        }
    }
}