package com.example.albazip.src.mypage.manager.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.albazip.databinding.ItemRvNoticeListBinding
import com.example.albazip.src.main.ManagerMainActivity
import com.example.albazip.src.mypage.manager.data.local.NoticeData
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
            binding.cbPin.isChecked = noticeData.pinState // 핀 고정 여부

            // 체크 상태를 저장
            binding.cbPin.setOnClickListener {
                noticeData.pinState = binding.cbPin.isChecked
                notifyItemChanged(adapterPosition)
            }
        }
    }
}