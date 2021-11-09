package com.example.albazip.src.mypage.worker.myInfo

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivitySelectDayListBinding
import com.example.albazip.src.mypage.worker.adapter.DailyDoneAdapter
import com.example.albazip.src.mypage.worker.adapter.DailyUnDoneAdapter
import com.example.albazip.src.mypage.worker.adapter.WorkDoneAdapter
import com.example.albazip.src.mypage.worker.data.local.DailyDoneWorkListData
import com.example.albazip.src.mypage.worker.data.local.DailyUnDoneWorkListData

class SelectDayListActivity :
    BaseActivity<ActivitySelectDayListBinding>(ActivitySelectDayListBinding::inflate) {

    private val unDoneList = ArrayList<DailyUnDoneWorkListData>()
    private val doneList = ArrayList<DailyDoneWorkListData>()

    private lateinit var unDoneListAdapter: DailyUnDoneAdapter
    private lateinit var doneListAdapter: DailyDoneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 이전 액티비티에서 title 받아오기
        if (intent.hasExtra("title")) {
            binding.tvWeekTitle.text = intent.getStringExtra("title")
        }

        // 미완료 업무 리스트
        unDoneList.add(DailyUnDoneWorkListData("간판 안으로 들여놓기", "문 뒤에 들여놓으세요.","사장님 김형준 · 2021.10.21."))
        unDoneList.add(DailyUnDoneWorkListData("홀 쓸고, 닦기", "닦기는 오픈 때 하면되니 시간없으면 메모 남겨주시면 됩니다~","사장님 김형준 · 2021.10.21."))
        unDoneList.add(DailyUnDoneWorkListData("원두, 커피머신 쇼케이스 전원끄기", "원두는 1번과 3번 동시에 누르면 되고 커피머신은 가운데를 살포시 눌러주세용용용용용용용용용.","사장님 김형준 · 2021.10.21."))
        unDoneList.add(DailyUnDoneWorkListData("원두, 커피머신 쇼케이스 전원끄기", "원두는 1번과 3번 동시에 누르면 되고 커피머신은 가운데를 살포시 눌러주세용용용용용용용용용.","사장님 김형준 · 2021.10.21."))
        unDoneList.add(DailyUnDoneWorkListData("원두, 커피머신 쇼케이스 전원끄기", "원두는 1번과 3번 동시에 누르면 되고 커피머신은 가운데를 살포시 눌러주세용용용용용용용용용.","사장님 김형준 · 2021.10.21."))

        // 완료 업무 리스트
        doneList.add(DailyDoneWorkListData("에어콘 끄기", "완료 22:04"))
        doneList.add(DailyDoneWorkListData("실내등 조명 끄기", "완료 21:04"))
        doneList.add(DailyDoneWorkListData("에어콘 끄기", "완료 22:04"))
        doneList.add(DailyDoneWorkListData("에어콘 끄기", "완료 22:04"))
        doneList.add(DailyDoneWorkListData("에어콘 끄기", "완료 22:04"))
        doneList.add(DailyDoneWorkListData("에어콘 끄기", "완료 22:04"))
        doneList.add(DailyDoneWorkListData("에어콘 끄기", "완료 22:04"))

        // 미완료/완료 업무리스트 갯수 count
        binding.tvUndoneCnt.text = unDoneList.size.toString()
        binding.tvDoneCnt.text = doneList.size.toString()

        /////////////////////////////// 미완료 업무 리스트 연결 ////////////////////////////////////
        if(unDoneList.isEmpty()){
            binding.rlNoUndoneWork.visibility = View.VISIBLE
        }else{
            unDoneListAdapter = DailyUnDoneAdapter(unDoneList)
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