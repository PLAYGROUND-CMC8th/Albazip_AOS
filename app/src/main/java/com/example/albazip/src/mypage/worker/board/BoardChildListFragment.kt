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
import com.example.albazip.src.mypage.worker.init.data.WBoardInfo

class BoardChildListFragment(val boardInfoList:WBoardInfo):BaseFragment<ChildFragmentBoardBinding>(ChildFragmentBoardBinding::bind,
    R.layout.child_fragment_board) {

    // 게시판 리스트
    private var getBoard = boardInfoList
    private var boardList = ArrayList<BoardData>()
    private lateinit var bordListAdapter: WBoardListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 게시글 리스트 불러오기
        loadBoardList()
    }

    private fun loadBoardList(){

        for(i in 0 until getBoard.postInfo.size){
            boardList.add(
                BoardData(getBoard.postInfo[i].writerName,getBoard.postInfo[i].writerJob,getBoard.postInfo[i].title,getBoard.postInfo[i].content,
            getBoard.postInfo[i].commentCount.toString(),getBoard.postInfo[i].registerDate.substring(0,10).replace("-",".")+"."))
        }

        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvWritingList.layoutManager = linearLayoutManager
        bordListAdapter = WBoardListAdapter(boardList,requireContext())
        binding.rvWritingList.adapter = bordListAdapter
    }
}