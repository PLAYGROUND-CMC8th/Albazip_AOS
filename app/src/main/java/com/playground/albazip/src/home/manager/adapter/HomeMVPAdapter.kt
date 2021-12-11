package com.playground.albazip.src.home.manager.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.databinding.ItemVpHomeCommunicateMBinding
import com.playground.albazip.src.community.common.ui.NoticeContentActivity
import com.playground.albazip.src.home.common.data.HomeCommuData

class HomeMVPAdapter(private val context: Context, private val itemList: ArrayList<HomeCommuData>, jobFlags:Int) :
    RecyclerView.Adapter<HomeMVPAdapter.HomeVPHolder>() {

    val myContext = context
    val getJobFlags = jobFlags // 0 관리자 ,1 근무자

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeVPHolder {
        val binding =
            ItemVpHomeCommunicateMBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeVPHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeVPHolder, position: Int) {
        val dataList = itemList[position]
        holder.bind(dataList)

        // 공지사항 읽기 화면으로 이동
        holder.binding.root.setOnClickListener {
            val readIntent = Intent(myContext, NoticeContentActivity::class.java)
            readIntent.putExtra("noticeId",itemList[holder.adapterPosition].noticeId)
            (myContext as Activity).startActivity(readIntent)
        }
    }

    override fun getItemCount(): Int = itemList.size

    inner class HomeVPHolder(var binding: ItemVpHomeCommunicateMBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: HomeCommuData) {
            // 텍스트 받아오기
            binding.tvVpTitle.text = data.contentTxt
        }
    }

}