package com.playground.albazip.src.community.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.playground.albazip.databinding.ItemVpOriginalImgBinding
import okhttp3.internal.notify


class ImgListVPAdapter(val context: Context, private val itemList: List<String>) :
    RecyclerView.Adapter<ImgListVPAdapter.ImgListVPHolder>() {

    val myContext = context

    interface OnItemClickListener{
        fun onItemClick(v : View, position : Int)
    }

    private var listener : OnItemClickListener?=null

    fun setOnItemClickListener(listener:OnItemClickListener){
        this.listener=listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgListVPHolder {
        val binding =
            ItemVpOriginalImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImgListVPHolder(binding)
    }

    override fun onBindViewHolder(holder: ImgListVPHolder, position: Int) {
        val dataList = itemList[position]
        holder.bind(dataList)
    }

    override fun getItemCount(): Int = itemList.size

    inner class ImgListVPHolder(var binding: ItemVpOriginalImgBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data:String) {
            // 이미지 리스트 받아오기
            Glide.with(myContext).load(data).into(binding.ivOriginalImg)
            binding.ivOriginalImg.setOnClickListener {
                listener?.onItemClick(binding.ivOriginalImg,adapterPosition)
            }
        }
    }
}

data class ImgListData(val img_uri:String)