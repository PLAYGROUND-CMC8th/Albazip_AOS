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
import com.example.albazip.src.mypage.manager.init.data.NoticeInfo
import com.example.albazip.src.mypage.manager.init.data.PostInfo

class WroteChildFragment(val serverNoticeList:ArrayList<NoticeInfo>, val serverPostList:ArrayList<PostInfo>) : BaseFragment<ChildFragmentWroteBinding>(
    ChildFragmentWroteBinding::bind,
    R.layout.child_fragment_wrote
) {
    // 공지 리스트
    private var getNoticeList = serverNoticeList
    private var noticeList = ArrayList<NoticeData>()
    private lateinit var noticeListAdapter: NoticeListAdapter

    // 게시판 리스트
    private var getPostList = serverPostList
    private var boardList = ArrayList<BoardData>()
    private lateinit var bordListAdapter: MBoardListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 작성글 존재여부 체크 -> UI 변경
        checkNoticeEmpty()

        for(i in 0 until getNoticeList.size){
            noticeList.add(NoticeData(getNoticeList[i].title,getNoticeList[i].registerDate.substring(0,10).replace("-",".")+".",getNoticeList[i].pin))
        }

        for(i in 0 until getPostList.size){
            boardList.add(BoardData(getPostList[i].title,getPostList[i].content,getPostList[i].commentCount.toString(),getNoticeList[i].registerDate.substring(0,10).replace("-",".")+"."))
        }

        // 공지사항 리스트 불러오기
        loadNoticeList()

        // 텍스트 클릭 시
        binding.tvNotice.setOnClickListener {
            checkNoticeEmpty()
            loadNoticeList()    // 공지사항 리스트 불러오기
            binding.tvNotice.setTypeface(null,Typeface.BOLD)  // 선택 텍스트 굵기 변경
            binding.tvNotice.setTextColor(Color.parseColor("#ff9d00"))// 선택 텍스트 색상 변경
            binding.tvBoard.setTypeface(null,Typeface.NORMAL)// 해제 텍스트 굵기 변경
            binding.tvBoard.setTextColor(Color.parseColor("#6f6f6f"))// 해제 텍스트 색상 변경
        }

        binding.tvBoard.setOnClickListener {
            loadBoardList()     // 게시판 리스트 불러오기
            checkBoardEmpty()
            binding.tvBoard.setTypeface(null,Typeface.BOLD)  // 선택 텍스트 굵기 변경
            binding.tvBoard.setTextColor(Color.parseColor("#ff9d00"))// 선택 텍스트 색상 변경
            binding.tvNotice.setTypeface(null,Typeface.NORMAL)// 해제 텍스트 굵기 변경
            binding.tvNotice.setTextColor(Color.parseColor("#6f6f6f"))// 해제 텍스트 색상 변경
        }
    }

    private fun checkNoticeEmpty(){
        if(getNoticeList.size == 0){
            binding.clNoWroteList.visibility = View.VISIBLE
        }
    }

    private fun checkBoardEmpty(){
        if(getPostList.size == 0){
            binding.clNoWroteList.visibility = View.VISIBLE
        }
    }

    private fun loadNoticeList(){

       // noticeList.add(NoticeData("코로나 매장 관리 공지","2021. 08. 20.",false))
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvWritingList.layoutManager = linearLayoutManager
        noticeListAdapter = NoticeListAdapter(noticeList)
        binding.rvWritingList.adapter = noticeListAdapter
    }

    private fun loadBoardList(){
//        boardList.add(BoardData("오차드별 아이스티 품절","오차드별 아이스티 남아있던 거 다 팔았습니다!\n" +
//                "오늘부로 판매 종료입니다~","2","2021. 08. 15."))
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvWritingList.layoutManager = linearLayoutManager
        bordListAdapter = MBoardListAdapter(boardList,requireContext())
        binding.rvWritingList.adapter = bordListAdapter
    }
}