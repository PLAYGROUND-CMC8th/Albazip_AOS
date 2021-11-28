package com.example.albazip.src.home.manager.worklist.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityHomePositionPersonalBinding
import com.example.albazip.src.home.manager.worklist.network.GetPositionPerFragmentView
import com.example.albazip.src.home.manager.worklist.network.GetPositionPerWorkService
import com.example.albazip.src.home.worker.opened.worklist.network.GetWPerTaskResponse
import com.example.albazip.src.mypage.worker.adapter.DailyDoneAdapter
import com.example.albazip.src.mypage.worker.adapter.DailyUnDoneAdapter
import com.example.albazip.src.mypage.worker.data.local.DailyDoneWorkListData
import com.example.albazip.src.mypage.worker.data.local.DailyUnDoneWorkListData

class HomePersonalPositionActivity :
    BaseActivity<ActivityHomePositionPersonalBinding>(ActivityHomePositionPersonalBinding::inflate),
    GetPositionPerFragmentView {

    private val unDoneList = ArrayList<DailyUnDoneWorkListData>()
    private val doneList = ArrayList<DailyDoneWorkListData>()

    private lateinit var unDoneListAdapter: DailyUnDoneAdapter
    private lateinit var doneListAdapter: DailyDoneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 이전 액티비티에서 title 받아오기
        if (intent.hasExtra("title")) {
            binding.tvWeekTitle.text = intent.getStringExtra("title")+ " 업무"
        }

        val workId = intent.getIntExtra("workId",0)

        // 개인 업무 조회 서버 통신
        GetPositionPerWorkService(this).tryGetPositionPerWorkInfo(workId)
        showLoadingDialog(this@HomePersonalPositionActivity)

        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 미완료 업무 리스트
        //unDoneList.add(DailyUnDoneWorkListData("간판 안으로 들여놓기", "문 뒤에 들여놓으세요.","사장님 김형준 · 2021.10.21."))

        // 완료 업무 리스트
        //doneList.add(DailyDoneWorkListData("에어콘 끄기", "완료 22:04"))

    }

    fun isListEmpty(){
        /////////////////////////////// 미완료 업무 리스트 연결 ////////////////////////////////////
        if(unDoneList.isEmpty()){
            binding.rlNoUndoneWork.visibility = View.VISIBLE
        }else{
            unDoneListAdapter = DailyUnDoneAdapter(unDoneList,this@HomePersonalPositionActivity)
            binding.rvUndone.adapter = unDoneListAdapter
        }

        ////////////////////////////// 완료 업무 리스트 연결 ///////////////////////////////////////
        // 업무리스트가 비어있으면 -> rv 연결 x and visibility true 로 설정
        if (doneList.isEmpty()) {
            binding.rlNoDoneWork.visibility = View.VISIBLE
        } else {
            // 업무리스트가 존재하면 -> rv 연결 o
            doneListAdapter = DailyDoneAdapter(this@HomePersonalPositionActivity, doneList)
            binding.rvDone.adapter = doneListAdapter
        }
    }

    override fun onGetMTaskSuccess(response: GetWPerTaskResponse) {
        dismissLoadingDialog()

        // 미완료 업무 리스트 호출
        if(response.data.nonComPerTask.size != 0) {
            for (i in 0 until response.data.nonComPerTask.size)
                unDoneList.add(
                    DailyUnDoneWorkListData(
                        response.data.nonComPerTask[i].takTitle,
                        response.data.nonComPerTask[i].taskContent,
                        response.data.nonComPerTask[i].writerTitle + " "
                                + response.data.nonComPerTask[i].writerName + " · " + response.data.nonComPerTask[i].registerDate.substring(
                            0,
                            10
                        ).replace("-", ".") + "."
                    )
                )
        }
        // 완료 업무 리스트 호출
        if(response.data.compPerTask.size !=0) {
            for (i in 0 until response.data.compPerTask.size)
                doneList.add(
                    DailyDoneWorkListData(
                        response.data.compPerTask[i].takTitle,
                        "완료 " + response.data.compPerTask[i].completeTime.substring(11, 16)
                    )
                )
        }
        isListEmpty()

        // 미완료/완료 업무리스트 갯수 count
        binding.tvUndoneCnt.text = unDoneList.size.toString()
        binding.tvDoneCnt.text = doneList.size.toString()
    }

    override fun onGetMTaskFailure(message: String) {
        dismissLoadingDialog()
    }
}

