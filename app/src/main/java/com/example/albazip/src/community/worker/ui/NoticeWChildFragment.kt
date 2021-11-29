package com.example.albazip.src.community.worker.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentWNoticeBinding
import com.example.albazip.src.community.manager.network.GetBoardListFragmentView
import com.example.albazip.src.community.manager.network.GetBoardListResponse
import com.example.albazip.src.community.manager.network.GetBoardNoticeService
import com.example.albazip.src.community.worker.adapter.NoticeWListAdapter
import com.example.albazip.src.community.worker.data.NoticeWData


class NoticeWChildFragment: BaseFragment<ChildFragmentWNoticeBinding>(
    ChildFragmentWNoticeBinding::bind,
    R.layout.child_fragment_w_notice), GetBoardListFragmentView {

    private var noticeArray = ArrayList<NoticeWData>()
    private lateinit var noticeAdapter: NoticeWListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        // 공지사항 리스트 조회
        GetBoardNoticeService(this).tryGetBoardList()
        showLoadingDialog(requireContext())
    }

    // 공지리스트 조회 (성공)
    override fun onBoardListGetSuccess(response: GetBoardListResponse) {
        dismissLoadingDialog()

        noticeArray.clear()

        if(response.data.size == 0){
            binding.llNoContent.visibility = View.VISIBLE
        }

        for (i in 0 until response.data.size){
            noticeArray.add(NoticeWData(response.data[i].id ,response.data[i].title,response.data[i].registerDate.substring(0, 10).replace("-", ".") + ".",response.data[i].pin,response.data[i].confirm))
        }

        noticeAdapter = NoticeWListAdapter(requireContext(),noticeArray)

        binding.rvCommunityMainNoticeW.adapter = noticeAdapter

        // recyclerview 스크롤 리스너
        binding.rvCommunityMainNoticeW.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 마지막으로 스크롤 된 항목 위치
                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                // 항목 전체 개수
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1
                if (lastVisibleItemPosition == itemTotalCount) {
                    Log.d("SCROLL", "last Position...")
                }

            }
        })
    }

    override fun onBoardListGetFailure(message: String) {
        dismissLoadingDialog()
    }
}