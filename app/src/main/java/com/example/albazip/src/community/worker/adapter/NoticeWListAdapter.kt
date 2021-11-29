package com.example.albazip.src.community.worker.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.albazip.R
import com.example.albazip.databinding.ItemRvNoticeListWBinding
import com.example.albazip.src.community.worker.data.NoticeWData

class NoticeWListAdapter(val context: Context, val itemList:ArrayList<NoticeWData>): RecyclerView.Adapter<NoticeWListAdapter.NoticeHolder>() {

    private lateinit var binding: ItemRvNoticeListWBinding

    private var myContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeWListAdapter.NoticeHolder {
        binding =  ItemRvNoticeListWBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return NoticeHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeHolder, position: Int) {
        holder.setNoticeList(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class NoticeHolder(val binding: ItemRvNoticeListWBinding): RecyclerView.ViewHolder(binding.root){

        fun setNoticeList(noticeData: NoticeWData){

            binding.tvTitle.text = noticeData.titleTxt // 글제목
            binding.tvDate.text = noticeData.dateTxt // 날짜

            // 핀 고정 여부
            if (noticeData.pinState == 0){
                binding.ivPin.visibility = View.GONE
            }else{
                binding.ivPin.visibility = View.VISIBLE
            }

            // 게시글 확인 여부
            if (noticeData.confirm == 0){ // 미확인

                binding.rlDidRead.background =  ContextCompat.getDrawable(
                    myContext,
                    R.drawable.rectangle_fill_light_red_radius_4
                )

                binding.tvConfirm.setTextColor(Color.parseColor("#fb3a00"))

                // 배경
                binding.clRoot.background =  ContextCompat.getDrawable(
                    myContext,
                    R.drawable.bg_rv_item_read_15
                )
            }else{ // 확인
                binding.rlDidRead.background =  ContextCompat.getDrawable(
                    myContext,
                    R.drawable.rectangle_fill_light_green_radius_4
                )
                binding.tvConfirm.setTextColor(Color.parseColor("#1dbe4e"))

                // 배경
                binding.clRoot.background =  ContextCompat.getDrawable(
                    myContext,
                    R.drawable.bg_rv_item_wrote_15
                )
            }

        }
    }
}