package com.playground.albazip.src.community.manager.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.playground.albazip.R
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.ChildFragmentNoticeBinding
import com.playground.albazip.src.community.manager.network.GetBoardListFragmentView
import com.playground.albazip.src.community.manager.network.GetBoardListResponse
import com.playground.albazip.src.community.manager.network.GetBoardNoticeService
import com.playground.albazip.src.community.manager.adapter.NoticeListAdapter
import com.playground.albazip.src.mypage.manager.board.data.local.NoticeData

class NoticeMChildFragment:BaseFragment<ChildFragmentNoticeBinding>(ChildFragmentNoticeBinding::bind,
    R.layout.child_fragment_notice),GetBoardListFragmentView {

    private var noticeArray = ArrayList<NoticeData>()
    private lateinit var noticeAdapter: NoticeListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        // 공지사항 리스트 조회
        GetBoardNoticeService(this).tryGetBoardList()
        showBeeLoadingDialog(requireContext())
        //showLoadingDialog(requireContext())
    }

    // 공지리스트 조회 (성공)
    override fun onBoardListGetSuccess(response: GetBoardListResponse) {
        dismissBeeLoadingDialog()
        //dismissLoadingDialog()

        noticeArray.clear()

        if(response.data.size == 0){
            binding.llNoContent.visibility = View.VISIBLE
        }else{
            binding.llNoContent.visibility = View.GONE
        }

        for (i in 0 until response.data.size){
            noticeArray.add(NoticeData(response.data[i].id ,response.data[i].title,response.data[i].registerDate.substring(0, 10).replace("-", ".") + ".",response.data[i].pin))
        }

        noticeAdapter = NoticeListAdapter(requireContext(),noticeArray)

        binding.rvCommunityMainNotice.adapter = noticeAdapter

        // recyclerview 스크롤 리스너
       /* binding.rvCommunityMainNotice.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        })*/
    }

    override fun onBoardListGetFailure(message: String) {
        dismissBeeLoadingDialog()
        //dismissLoadingDialog()
    }
}