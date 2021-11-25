package com.example.albazip.src.mypage.worker.myInfo.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.R
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityLateCheckBinding
import com.example.albazip.src.mypage.common.workerdata.commute.data.GetCommuteInfoResponse
import com.example.albazip.src.mypage.common.workerdata.commute.network.CommuteFragmentView
import com.example.albazip.src.mypage.common.workerdata.commute.network.CommuteService
import com.example.albazip.src.mypage.worker.adapter.WBoardListAdapter
import com.example.albazip.src.mypage.worker.adapter.WLateRecordAdapter
import com.example.albazip.src.mypage.worker.data.local.WLateRecordData
import com.example.albazip.src.mypage.worker.myInfo.network.RecentCommuteFragmentView
import com.example.albazip.src.mypage.worker.myInfo.network.RecentCommuteInfoService

// 지각(출석) 체크
class LateCheckActivity:BaseActivity<ActivityLateCheckBinding>(ActivityLateCheckBinding::inflate),RecentCommuteFragmentView,CommuteFragmentView {

    private lateinit var lateRecordAdapter:WLateRecordAdapter
    private val lateRecordList = ArrayList<WLateRecordData>()

    private var currentYear:String? = null // 최근 년도
    private var currentMonth:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 이전화면으로 돌아가기
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 이전 달 데이터 불러오기
        binding.ibtnPrevMonth.setOnClickListener {
            currentMonth = currentMonth?.minus(1)
            if (currentMonth!! < 1){
                currentMonth = 12
            }

            CommuteService(this).tryGetCommuteInfo(currentYear!!,currentMonth.toString())
        }

        // 다음 달 데이터 불러오기
        binding.ibtnNextMonth.setOnClickListener {
            currentMonth = currentMonth?.plus(1)
            if(currentMonth!! > 12){
                currentMonth = 1
            }
            CommuteService(this).tryGetCommuteInfo(currentYear!!,currentMonth.toString())
        }

        RecentCommuteInfoService(this).tryGetRecentCommuteInfo()
        showLoadingDialog(this)
    }

    // 최근 근무 달의 정보 불러오기 - 성공
    override fun onRecentCommuteInfoGetSuccess(response: GetCommuteInfoResponse) {
        dismissLoadingDialog()

        // 최근년도
        currentYear = response.data.year

        // 최근달
        currentMonth = response.data.month.toInt()

        // 최근달 ui
        binding.tvMonth.text = response.data.month + "월"

        if(response.data.commuteData.size == 0){ // rv 연결 x
            binding.llNoData.visibility = View.VISIBLE
        }else{
            binding.llNoData.visibility = View.GONE
            for (i in 0 until response.data.commuteData.size){
                lateRecordList.add(
                    WLateRecordData(response.data.commuteData[i].year,response.data.commuteData[i].month,response.data.commuteData[i].day,response.data.commuteData[i].start_time,response.data.commuteData[i].end_time,
                        response.data.commuteData[i].is_late)
                )
            }

            // divider custom
            val dividerItemDecoration = DividerItemDecoration(this,1)
            dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.line_divider_late, null))

            // 아이템 구분선 삽입
            binding.rvRecord.addItemDecoration(dividerItemDecoration)

            lateRecordAdapter = WLateRecordAdapter(lateRecordList)
            binding.rvRecord.adapter = lateRecordAdapter
        }

    }

    override fun onRecentCommuteInfoGetFailure(message: String) {
        dismissLoadingDialog()
    }

    // 달별 정보 불러오기 - 성공
    override fun onCommuteGetSuccess(response: GetCommuteInfoResponse) {
        dismissLoadingDialog()

        binding.tvMonth.text = response.data.month + "월"

        lateRecordList.clear()
        binding.rvRecord.recycledViewPool.clear()
        lateRecordAdapter.notifyDataSetChanged()

        if(response.data.commuteData.size == 0){ // rv 연결 x
            binding.llNoData.visibility = View.VISIBLE
        }else {
            binding.llNoData.visibility = View.GONE
            for (i in 0 until response.data.commuteData.size){
                lateRecordList.add(
                    WLateRecordData(response.data.commuteData[i].year,response.data.commuteData[i].month,response.data.commuteData[i].day,response.data.commuteData[i].start_time,response.data.commuteData[i].end_time,
                        response.data.commuteData[i].is_late)
                )
            }
            lateRecordAdapter = WLateRecordAdapter(lateRecordList)
            binding.rvRecord.adapter = lateRecordAdapter
        }

    }

    override fun onCommuteGetFailure(message: String) {
        dismissLoadingDialog()
    }
}