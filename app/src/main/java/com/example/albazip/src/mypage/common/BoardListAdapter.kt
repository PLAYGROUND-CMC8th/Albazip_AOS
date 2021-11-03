package com.example.albazip.src.mypage.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.albazip.databinding.ItemRvBoardListBinding

class BoardListAdapter(val itemList:ArrayList<BoardData>): RecyclerView.Adapter<BoardListAdapter.BoardHolder>() {

    private lateinit var binding:ItemRvBoardListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardHolder {
        binding =  ItemRvBoardListBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return BoardHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardHolder, position: Int) {
       holder.setBoardList(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class BoardHolder(val binding:ItemRvBoardListBinding):RecyclerView.ViewHolder(binding.root){

        fun setBoardList(boardData:BoardData){
            binding.tvTitle.text = boardData.title // 글제목
            binding.tvContent.text = boardData.content// 글 내용
            binding.tvDate.text = boardData.date// 글 날짜
            binding.tvCommentCnt.text = boardData.commentCnt // 댓글 갯수
        }
    }
}