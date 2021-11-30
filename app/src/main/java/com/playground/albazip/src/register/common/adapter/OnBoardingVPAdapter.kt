package com.playground.albazip.src.register.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.playground.albazip.databinding.ItemVpOnBoardingBinding
import com.playground.albazip.src.register.common.data.local.OnBoardData

class OnBoardingVPAdapter(val context:Context, private val itemList:ArrayList<OnBoardData>): RecyclerView.Adapter<OnBoardingVPAdapter.OnBoardHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardHolder {
        val binding = ItemVpOnBoardingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OnBoardHolder(binding)
    }

    override fun onBindViewHolder(holder: OnBoardHolder, position: Int) {
        val boardList = itemList[position]
        holder.bind(boardList)
    }

    override fun getItemCount(): Int = itemList.size

    inner class OnBoardHolder(var binding : ItemVpOnBoardingBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(data: OnBoardData){
            Glide.with(context).load(data.img).into((binding.ivOnboard)) // 온보딩 이미지
            binding.tvVpTitle.text = data.titleTxt // 타이틀
            binding.tvVpContents.text = data.contextTxt //내용
        }

    }


}