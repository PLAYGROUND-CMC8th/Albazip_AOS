package com.playground.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.commute

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.playground.albazip.R
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityLateCheckBinding
import com.playground.albazip.src.mypage.common.workerdata.commute.data.GetCommuteInfoResponse
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.commute.network.DetailCommuteFragmentView
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.commute.network.DetailCommuteService
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.commute.network.DetailReCommuteFragmentView
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.commute.network.DetailReCommuteService
import com.playground.albazip.src.mypage.worker.adapter.WLateRecordAdapter
import com.playground.albazip.src.mypage.worker.data.local.WLateRecordData

class DetailLateCheckActivity:BaseActivity<ActivityLateCheckBinding>(ActivityLateCheckBinding::inflate),DetailCommuteFragmentView,DetailReCommuteFragmentView {

    private lateinit var lateRecordAdapter: WLateRecordAdapter
    private val lateRecordList = ArrayList<WLateRecordData>()

    private var currentYear:String? = null // 최근 년도
    private var currentMonth:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.tvLateCnt.text = intent.getStringExtra("lateCnt")
        val positionId = intent.getIntExtra("positionId",0)

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

            DetailCommuteService(this).tryDetailGetCommuteInfo(positionId,currentYear!!,currentMonth.toString())
        }

        // 다음 달 데이터 불러오기
        binding.ibtnNextMonth.setOnClickListener {
            currentMonth = currentMonth?.plus(1)
            if(currentMonth!! > 12){
                currentMonth = 1
            }
            DetailCommuteService(this).tryDetailGetCommuteInfo(positionId,currentYear!!,currentMonth.toString())
        }

        DetailReCommuteService(this).tryDetailReGetCommuteInfo(positionId)
        showLoadingDialog(this)
    }

    override fun onReCommuteGetSuccess(response: GetCommuteInfoResponse) {
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
                        response.data.commuteData[i].start_late,response.data.commuteData[i].end_late)
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

    override fun onReCommuteGetFailure(message: String) {
        dismissLoadingDialog()
    }

    // 달별 정보 불러오기
    override fun onCommuteGetSuccess(response: GetCommuteInfoResponse) {
        dismissLoadingDialog()

        binding.tvMonth.text = response.data.month + "월"

        if(lateRecordList.size != 0) {
            lateRecordList.clear()
            binding.rvRecord.recycledViewPool.clear()
            lateRecordAdapter.notifyDataSetChanged()
        }

        if(response.data.commuteData.size == 0){ // rv 연결 x
            binding.llNoData.visibility = View.VISIBLE
        }else {
            binding.llNoData.visibility = View.GONE
            for (i in 0 until response.data.commuteData.size){
                lateRecordList.add(
                    WLateRecordData(response.data.commuteData[i].year,response.data.commuteData[i].month,response.data.commuteData[i].day,response.data.commuteData[i].start_time,response.data.commuteData[i].end_time,
                        response.data.commuteData[i].start_late,response.data.commuteData[i].end_late)
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