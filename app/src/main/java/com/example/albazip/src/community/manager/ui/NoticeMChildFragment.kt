package com.example.albazip.src.community.manager.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentNoticeBinding
import com.example.albazip.src.mypage.manager.adapter.NoticeListAdapter
import com.example.albazip.src.mypage.manager.board.data.local.NoticeData

class NoticeMChildFragment:BaseFragment<ChildFragmentNoticeBinding>(ChildFragmentNoticeBinding::bind,
    R.layout.child_fragment_notice) {

    private var noticeArray = ArrayList<NoticeData>()
    private lateinit var noticeAdapter:NoticeListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        noticeArray.add(NoticeData("[필독] 음료 제조시 주의사항","2020.08.02.",1))
        noticeArray.add(NoticeData("[필독] 재료 소분시 주의사항","2020.08.04.",1))
        noticeArray.add(NoticeData("[필독] 손님 응대는 이렇게 해주세요.","2020.08.20.",1))

        noticeArray.add(NoticeData("코로나 관련 매장 공지","2020.08.20.",0))
        noticeArray.add(NoticeData("빙수기계 작동법","2020.08.20.",0))
        noticeArray.add(NoticeData("더치 앰플 판매 관련","2020.08.20.",0))
        noticeArray.add(NoticeData("더치 앰플 판매 관련","2020.08.20.",0))
        noticeArray.add(NoticeData("더치 앰플 판매 관련","2020.08.20.",0))

        noticeAdapter = NoticeListAdapter(noticeArray)

        binding.rvCommunityMainNotice.adapter = noticeAdapter

        // recyclerview 스크롤 리스너
        binding.rvCommunityMainNotice.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.rvCommunityMainNotice.canScrollVertically(1)) {
                    showCustomToast("최하단 도달")
                }

            }
        })

    }
}