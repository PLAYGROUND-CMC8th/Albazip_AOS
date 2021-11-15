package com.example.albazip.src.mypage.worker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.albazip.R
import com.example.albazip.databinding.ItemRvBoardListBinding
import com.example.albazip.src.mypage.common.setting.BoardData

class WBoardListAdapter(val itemList:ArrayList<BoardData>, var context: Context): RecyclerView.Adapter<WBoardListAdapter.BoardHolder>() {

    private lateinit var binding: ItemRvBoardListBinding
    var myContext:Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardHolder {
        binding =  ItemRvBoardListBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return BoardHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardHolder, position: Int) {
        holder.setBoardList(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class BoardHolder(val binding: ItemRvBoardListBinding): RecyclerView.ViewHolder(binding.root){

        fun setBoardList(boardData: BoardData){

            // 프로필 설정
            Glide.with(myContext).load(R.drawable.img_w_profile_1_24_px).circleCrop().into(binding.ivProfile)
            // 포지션 설정
            binding.tvPosition.text = boardData.writerJob
            //binding.tvPosition.setTextColor(Color.parseColor("#ffb100"))
            binding.tvName.text = boardData.writerName // 이름 설정
            binding.tvTitle.text = boardData.title // 글제목
            binding.tvContent.text = boardData.content// 글 내용
            binding.tvDate.text = boardData.date// 글 날짜
            binding.tvCommentCnt.text = boardData.commentCnt // 댓글 갯수
        }
    }
}