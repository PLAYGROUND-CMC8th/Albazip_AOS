package com.example.albazip.src.mypage.manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.albazip.databinding.ItemRvNoticeListBinding
import com.example.albazip.src.mypage.manager.board.data.local.NoticeData
import okhttp3.internal.notifyAll

class NoticeListAdapter(val itemList:ArrayList<NoticeData>): RecyclerView.Adapter<NoticeListAdapter.NoticeHolder>() {

    private lateinit var binding: ItemRvNoticeListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeListAdapter.NoticeHolder {
        binding =  ItemRvNoticeListBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return NoticeHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeHolder, position: Int) {
        holder.setNoticeList(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class NoticeHolder(val binding: ItemRvNoticeListBinding): RecyclerView.ViewHolder(binding.root){

        fun setNoticeList(noticeData: NoticeData){

            binding.itemTvTitle.text = noticeData.titleTxt // 글제목
            binding.itemTvDate.text = noticeData.dateTxt // 날짜

            binding.cbPin.isChecked = noticeData.pinState != 0 // 핀 고정 여부

            // 체크 상태를 저장 (여기서 아마 서버 통신)
            binding.cbPin.setOnClickListener {
                if(noticeData.pinState == 0){
                    noticeData.pinState = 1
                }else{
                    noticeData.pinState = 0
                }
                        //noticeData.pinState = binding.cbPin.isChecked
                notifyItemChanged(adapterPosition)
            }
        }
    }
}