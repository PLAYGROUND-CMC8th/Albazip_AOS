package com.example.albazip.src.mypage.worker.myInfo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.R
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityLateCheckBinding
import com.example.albazip.src.mypage.common.workerdata.commute.data.GetCommuteInfoResponse
import com.example.albazip.src.mypage.worker.adapter.WBoardListAdapter
import com.example.albazip.src.mypage.worker.adapter.WLateRecordAdapter
import com.example.albazip.src.mypage.worker.data.local.WLateRecordData
import com.example.albazip.src.mypage.worker.myInfo.network.RecentCommuteFragmentView
import com.example.albazip.src.mypage.worker.myInfo.network.RecentCommuteInfoService

// 지각(출석) 체크
class LateCheckActivity:BaseActivity<ActivityLateCheckBinding>(ActivityLateCheckBinding::inflate),RecentCommuteFragmentView {

    private lateinit var lateRecordAdapter:WLateRecordAdapter
    private val lateRecordList = ArrayList<WLateRecordData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RecentCommuteInfoService(this).tryGetRecentCommuteInfo()
        showLoadingDialog(this)
    }

    override fun onRecentCommuteInfoGetSuccess(response: GetCommuteInfoResponse) {
        dismissLoadingDialog()

        binding.tvMonth.text = response.data.month + "월"

        showCustomToast(response.message.toString())
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

    override fun onRecentCommuteInfoGetFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }
}