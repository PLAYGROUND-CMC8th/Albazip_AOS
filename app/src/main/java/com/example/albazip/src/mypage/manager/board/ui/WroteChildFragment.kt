package com.example.albazip.src.mypage.manager.board.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentWroteBinding
import com.example.albazip.src.mypage.common.BoardData
import com.example.albazip.src.mypage.manager.adapter.MBoardListAdapter
import com.example.albazip.src.mypage.manager.adapter.NoticeListAdapter
import com.example.albazip.src.mypage.manager.board.data.local.NoticeData

class WroteChildFragment : BaseFragment<ChildFragmentWroteBinding>(
    ChildFragmentWroteBinding::bind,
    R.layout.child_fragment_wrote
) {
    // 공지 리스트
    private var noticeList = ArrayList<NoticeData>()
    private lateinit var noticeListAdapter: NoticeListAdapter

    // 게시판 리스트
    private var boardList = ArrayList<BoardData>()
    private lateinit var bordListAdapter: MBoardListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 공지사항 리스트 불러오기
        loadNoticeList()

        // 텍스트 클릭 시
        binding.tvNotice.setOnClickListener {
            loadNoticeList()    // 공지사항 리스트 불러오기
            binding.tvNotice.setTypeface(null,Typeface.BOLD)  // 선택 텍스트 굵기 변경
            binding.tvNotice.setTextColor(Color.parseColor("#ff9d00"))// 선택 텍스트 색상 변경
            binding.tvBoard.setTypeface(null,Typeface.NORMAL)// 해제 텍스트 굵기 변경
            binding.tvBoard.setTextColor(Color.parseColor("#6f6f6f"))// 해제 텍스트 색상 변경
        }

        binding.tvBoard.setOnClickListener {
            loadBoardList()     // 게시판 리스트 불러오기
            binding.tvBoard.setTypeface(null,Typeface.BOLD)  // 선택 텍스트 굵기 변경
            binding.tvBoard.setTextColor(Color.parseColor("#ff9d00"))// 선택 텍스트 색상 변경
            binding.tvNotice.setTypeface(null,Typeface.NORMAL)// 해제 텍스트 굵기 변경
            binding.tvNotice.setTextColor(Color.parseColor("#6f6f6f"))// 해제 텍스트 색상 변경
        }
    }

    private fun loadNoticeList(){
        noticeList.add(NoticeData("코로나 매장 관리 공지","2021. 08. 20.",false))
        noticeList.add(NoticeData("빙수기계 작동법","2021. 08. 22.",false))
        noticeList.add(NoticeData("더치 앰플 판매 관련","2021. 08. 24.",false))
        noticeList.add(NoticeData("코로나 관련 매장 공지","2021. 08. 24.",false))
        noticeList.add(NoticeData("빙수기계 작동법","2021. 08. 28.",false))
        noticeList.add(NoticeData("빙수기계 작동법","2021. 08. 28.",false))
        noticeList.add(NoticeData("빙수기계 작동법","2021. 08. 28.",false))
        noticeList.add(NoticeData("빙수기계 작동법","2021. 08. 28.",false))
        noticeList.add(NoticeData("빙수기계 작동법","2021. 08. 28.",false))


        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvWritingList.layoutManager = linearLayoutManager
        noticeListAdapter = NoticeListAdapter(noticeList)
        binding.rvWritingList.adapter = noticeListAdapter
    }

    private fun loadBoardList(){
        boardList.add(BoardData("오차드별 아이스티 품절","오차드별 아이스티 남아있던 거 다 팔았습니다!\n" +
                "오늘부로 판매 종료입니다~","2","2021. 08. 15."))
        boardList.add(BoardData("오차드별 아이스티 품절","오차드별 아이스티 남아있던 거 다 팔았습니다!\n" +
                "오늘부로 판매 종료입니다~","2","2021. 08. 15."))
        boardList.add(BoardData("오차드별 아이스티 품절","오차드별 아이스티 남아있던 거 다 팔았습니다!\n" +
                "오늘부로 판매 종료입니다~","2","2021. 08. 15."))
        boardList.add(BoardData("오차드별 아이스티 품절","오차드별 아이스티 남아있던 거 다 팔았습니다!\n" +
                "오늘부로 판매 종료입니다~","2","2021. 08. 15."))
        boardList.add(BoardData("오차드별 아이스티 품절","오차드별 아이스티 남아있던 거 다 팔았습니다!\n" +
                "오늘부로 판매 종료입니다~","2","2021. 08. 15."))


        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvWritingList.layoutManager = linearLayoutManager
        bordListAdapter = MBoardListAdapter(boardList,requireContext())
        binding.rvWritingList.adapter = bordListAdapter
    }
}