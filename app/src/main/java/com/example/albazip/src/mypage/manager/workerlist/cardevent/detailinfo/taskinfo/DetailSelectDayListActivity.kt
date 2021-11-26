package com.example.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.taskinfo

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivitySelectDayListBinding
import com.example.albazip.src.mypage.common.workerdata.taskinfo.data.GetDayTaskResponse
import com.example.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.taskinfo.network.DetailDayTaskFragmentView
import com.example.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.taskinfo.network.DetailDayTaskService
import com.example.albazip.src.mypage.worker.adapter.DailyDoneAdapter
import com.example.albazip.src.mypage.worker.adapter.DailyUnDoneAdapter
import com.example.albazip.src.mypage.worker.data.local.DailyDoneWorkListData
import com.example.albazip.src.mypage.worker.data.local.DailyUnDoneWorkListData

class DetailSelectDayListActivity:BaseActivity<ActivitySelectDayListBinding>(ActivitySelectDayListBinding::inflate),DetailDayTaskFragmentView {

    private val unDoneList = ArrayList<DailyUnDoneWorkListData>()
    private val doneList = ArrayList<DailyDoneWorkListData>()

    private lateinit var unDoneListAdapter: DailyUnDoneAdapter
    private lateinit var doneListAdapter: DailyDoneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val positionId = intent.getIntExtra("positionId",0)

        // 이전 액티비티에서 title 받아오기
        if (intent.hasExtra("title")) {
            binding.tvWeekTitle.text = intent.getStringExtra("title")
            val year = intent.getStringExtra("year")
            val month = binding.tvWeekTitle.text.substring(0,2)
            val date = binding.tvWeekTitle.text.substring(3,5)

            DetailDayTaskService(this).tryGetDayTask(positionId,year!!,month,date)
            showLoadingDialog(this)
        }

        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onDetailDayTaskGetSuccess(response: GetDayTaskResponse) {
        dismissLoadingDialog()

        // 미완료 업무 리스트 호출
        for (i in 0 until response.data.nonCompleteTaskData.size)
            unDoneList.add(DailyUnDoneWorkListData(response.data.nonCompleteTaskData[i].title, response.data.nonCompleteTaskData[i].content,response.data.nonCompleteTaskData[i].writer_position+" "
                    +response.data.nonCompleteTaskData[i].writer_name+" · "+response.data.nonCompleteTaskData[i].register_date.substring(0,10).replace("-",".")+"."))

        // 완료 업무 리스트 호출
        for (i in 0 until response.data.completeTaskData.size)
            doneList.add(DailyDoneWorkListData(response.data.completeTaskData[i].title,"완료 " +response.data.completeTaskData[i].complete_date.substring(11,16)))

        // 미완료/완료 업무리스트 갯수 count
        binding.tvUndoneCnt.text = unDoneList.size.toString()
        binding.tvDoneCnt.text = doneList.size.toString()

        isListEmpty()
    }

    override fun onDetailDayTaskGetFailure(message: String) {
        dismissLoadingDialog()
    }

    fun isListEmpty(){
        /////////////////////////////// 미완료 업무 리스트 연결 ////////////////////////////////////
        if(unDoneList.isEmpty()){
            binding.rlNoUndoneWork.visibility = View.VISIBLE
        }else{
            unDoneListAdapter = DailyUnDoneAdapter(unDoneList,this@DetailSelectDayListActivity)
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
            doneListAdapter = DailyDoneAdapter(this@DetailSelectDayListActivity, doneList)
            binding.rvDone.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvDone.adapter = doneListAdapter
        }
    }

}