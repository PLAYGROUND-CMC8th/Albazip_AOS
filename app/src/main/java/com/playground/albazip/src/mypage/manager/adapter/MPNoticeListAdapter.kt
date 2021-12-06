package com.playground.albazip.src.mypage.manager.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.playground.albazip.R
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.databinding.ItemRvNoticeListBinding
import com.playground.albazip.src.community.common.ui.NoticeContentActivity
import com.playground.albazip.src.community.manager.MCommunityFragment
import com.playground.albazip.src.community.manager.adapter.NoticeListAdapter
import com.playground.albazip.src.community.manager.network.PutNoticePinListFragmentView
import com.playground.albazip.src.community.manager.network.PutNoticePinService
import com.playground.albazip.src.mypage.manager.board.data.local.NoticeData

class MPNoticeListAdapter(context: Context, val itemList:ArrayList<NoticeData>): RecyclerView.Adapter<MPNoticeListAdapter.NoticeHolder>(),
    PutNoticePinListFragmentView {

    private var myContext = context
    private lateinit var binding: ItemRvNoticeListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeHolder {
        binding =  ItemRvNoticeListBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return NoticeHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeHolder, position: Int) {

        holder.setNoticeList(itemList[position])

        // 공지사항 핀 고정 확인
        holder.binding.ivPin.setOnClickListener {
            PutNoticePinService(this).tryPutNoticePin(itemList[holder.adapterPosition].id,holder.adapterPosition)
        }

        // 공지사항 읽기 화면으로 이동
        holder.binding.root.setOnClickListener {
            val readIntent = Intent(myContext, NoticeContentActivity::class.java)
            readIntent.putExtra("noticeId",itemList[holder.adapterPosition].id)
            (myContext as Activity).startActivity(readIntent)
        }

        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int = itemList.size

    inner class NoticeHolder(val binding: ItemRvNoticeListBinding): RecyclerView.ViewHolder(binding.root){

        fun setNoticeList(noticeData: NoticeData){

            binding.itemTvTitle.text = noticeData.titleTxt // 글제목
            binding.itemTvDate.text = noticeData.dateTxt // 날짜

            if(noticeData.pinState == 0){
                Glide.with(myContext).load(R.drawable.ic_pushpin_inactive).into(binding.ivPin)
            }else{
                Glide.with(myContext).load(R.drawable.ic_pushpin_active).into(binding.ivPin)
            }
        }
    }


    // 핀 등록 성공
    override fun onNoticePinPutSuccess(response: BaseResponse,position: Int) {
        if(response.code == 200) {
            if(itemList[position].pinState ==0){
                itemList[position].pinState = 1
            }else{
                itemList[position].pinState = 0
            }
        }else if(response.code == 202){
            Toast.makeText(myContext,"핀 고정은 최대 5개 입니다.", Toast.LENGTH_SHORT).show()
        }

        // ui 변경
        notifyDataSetChanged()
    }

    // 핀 등록 실패
    override fun onNoticePinPutFailure(message: String) {
        Toast.makeText(myContext,message, Toast.LENGTH_SHORT).show()
    }
}