package com.example.albazip.src.mypage.worker.board

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentBoardBinding
import com.example.albazip.src.mypage.common.BoardData
import com.example.albazip.src.mypage.worker.adapter.WBoardListAdapter
import com.example.albazip.src.mypage.worker.board.data.GetWorkerBoardResponse
import com.example.albazip.src.mypage.worker.board.network.WorkBoardFragmentView
import com.example.albazip.src.mypage.worker.board.network.WorkerBoardService
import com.example.albazip.src.mypage.worker.init.data.WBoardInfo

class BoardChildListFragment(val boardInfoList:WBoardInfo):BaseFragment<ChildFragmentBoardBinding>(ChildFragmentBoardBinding::bind,
    R.layout.child_fragment_board), WorkBoardFragmentView {

    // 게시판 리스트
    private var getBoard = boardInfoList
    private var boardList = ArrayList<BoardData>()
    private lateinit var bordListAdapter: WBoardListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 게시글 리스트 불러오기
        loadBoardList()

        // 새로고침 시도
        binding.swipelayout.setOnRefreshListener {
            // 서버 통신 시도
            WorkerBoardService(this).tryGetSingleWorkerList()
        }
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

    override fun onGetSuccess(response: GetWorkerBoardResponse) {
        binding.swipelayout.isRefreshing = false // 새로고침 로딩 중지

        boardList.clear()
        for(i in 0 until  response.boardData.postInfo.size){
            boardList.add(BoardData(response.boardData.postInfo[i].writerName,response.boardData.postInfo[i].writerJob,response.boardData.postInfo[i].title,response.boardData.postInfo[i].content,response.boardData.postInfo[i].commentCount.toString(),
                response.boardData.postInfo[i].registerDate.substring(0,10).replace("-",".")+"."))
        }

        bordListAdapter = WBoardListAdapter(boardList,requireContext())
        binding.rvWritingList.adapter = bordListAdapter
    }

    override fun onGetFailure(message: String) {
        binding.swipelayout.isRefreshing = false // 새로고침 로딩 중지
        showCustomToast(message)
    }
}