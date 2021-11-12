package com.example.albazip.src.mypage.worker.board

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentBoardBinding
import com.example.albazip.src.mypage.common.BoardData
import com.example.albazip.src.mypage.manager.adapter.MBoardListAdapter
import com.example.albazip.src.mypage.worker.adapter.WBoardListAdapter

class BoardChildListFragment:BaseFragment<ChildFragmentBoardBinding>(ChildFragmentBoardBinding::bind,
    R.layout.child_fragment_board) {

    // 게시판 리스트
    private var boardList = ArrayList<BoardData>()
    private lateinit var bordListAdapter: WBoardListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 게시글 리스트 불러오기
        loadBoardList()
    }

    private fun loadBoardList(){
//        boardList.add(BoardData("오차드별 아이스티 품절","오차드별 아이스티 남아있던 거 다 팔았습니다!\n" +
//                "오늘부로 판매 종료입니다~","2","2021. 08. 15."))
//        boardList.add(BoardData("오차드별 아이스티 품절","오차드별 아이스티 남아있던 거 다 팔았습니다!\n" +
//                "오늘부로 판매 종료입니다~","2","2021. 08. 15."))
//        boardList.add(BoardData("오차드별 아이스티 품절","오차드별 아이스티 남아있던 거 다 팔았습니다!\n" +
//                "오늘부로 판매 종료입니다~","2","2021. 08. 15."))
//        boardList.add(BoardData("오차드별 아이스티 품절","오차드별 아이스티 남아있던 거 다 팔았습니다!\n" +
//                "오늘부로 판매 종료입니다~","2","2021. 08. 15."))
//        boardList.add(BoardData("오차드별 아이스티 품절","오차드별 아이스티 남아있던 거 다 팔았습니다!\n" +
//                "오늘부로 판매 종료입니다~","2","2021. 08. 15."))


        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvWritingList.layoutManager = linearLayoutManager
        bordListAdapter = WBoardListAdapter(boardList,requireContext())
        binding.rvWritingList.adapter = bordListAdapter
    }
}