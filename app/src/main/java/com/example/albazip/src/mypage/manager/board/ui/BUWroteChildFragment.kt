package com.example.albazip.src.mypage.manager.board.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentWroteBeforeUpdateBinding
import com.example.albazip.src.mypage.manager.adapter.NoticeListAdapter
import com.example.albazip.src.mypage.manager.board.data.local.NoticeData
import com.example.albazip.src.mypage.manager.board.data.remote.GetBoardResponse
import com.example.albazip.src.mypage.manager.board.network.BoardFragmentView
import com.example.albazip.src.mypage.manager.board.network.BoardService
import com.example.albazip.src.mypage.manager.init.data.NoticeInfo
import com.example.albazip.src.mypage.manager.init.data.PostInfo

class BUWroteChildFragment(
    val serverNoticeList: ArrayList<NoticeInfo>,
    val serverPostList: ArrayList<PostInfo>
) : BaseFragment<ChildFragmentWroteBeforeUpdateBinding>(
    ChildFragmentWroteBeforeUpdateBinding::bind,
    R.layout.child_fragment_wrote_before_update
), BoardFragmentView {
    // 공지 리스트
    private var getNoticeList = serverNoticeList
    private var noticeList = ArrayList<NoticeData>()
    private lateinit var noticeListAdapter: NoticeListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 새로고침
        binding.swipelayout.setOnRefreshListener {
            BoardService(this).tryGetBoard()
        }

        // 작성글 존재여부 체크 -> UI 변경
        checkNoticeEmpty()

        for (i in 0 until getNoticeList.size) {
            noticeList.add(
                NoticeData(
                    getNoticeList[i].title,
                    getNoticeList[i].registerDate.substring(0, 10).replace("-", ".") + ".",
                    getNoticeList[i].pin
                )
            )
        }

        // 공지사항 리스트 불러오기
        loadNoticeList()

    }

    private fun checkNoticeEmpty() {
        if (getNoticeList.size == 0) {
            binding.clNoWroteList.visibility = View.VISIBLE
        }
    }

    private fun loadNoticeList() {

        // noticeList.add(NoticeData("코로나 매장 관리 공지","2021. 08. 20.",false))
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvWritingList.layoutManager = linearLayoutManager
        noticeListAdapter = NoticeListAdapter(noticeList)
        binding.rvWritingList.adapter = noticeListAdapter
    }

    // 새로고침 성공
    override fun onBoardGetSuccess(response: GetBoardResponse) {
        binding.swipelayout.isRefreshing = false

        if (response.code == 200) {

            if(noticeList.size != 0){
                noticeList.clear()
                binding.rvWritingList.recycledViewPool.clear()
                noticeListAdapter.notifyDataSetChanged()
                binding.clNoWroteList.visibility = View.GONE
            }

            if (response.data.noticeInfo.size == 0) {
                binding.clNoWroteList.visibility = View.VISIBLE
            } else {
                for (i in 0 until response.data.noticeInfo.size) {
                    noticeList.add(
                        NoticeData(
                            response.data.noticeInfo[i].title,
                            response.data.noticeInfo[i].registerDate.substring(0, 10)
                                .replace("-", ".") + ".",
                            response.data.noticeInfo[i].pin
                        )
                    )
                }
            }

        }

    }

    override fun onBoardGetFailure(message: String) {
        binding.swipelayout.isRefreshing = false
    }
}