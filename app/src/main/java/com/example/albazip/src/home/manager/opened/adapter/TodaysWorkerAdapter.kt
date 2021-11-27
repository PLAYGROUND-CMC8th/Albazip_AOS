package com.example.albazip.src.home.manager.opened.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.albazip.R
import com.example.albazip.databinding.ItemRvTodaysWorkerBinding
import com.example.albazip.src.home.manager.opened.data.TodayWorkerData

class TodaysWorkerAdapter(val itemList:ArrayList<TodayWorkerData>,context:Context):RecyclerView.Adapter<TodaysWorkerAdapter.TodayWorkerHolder>() {

    private lateinit var binding:ItemRvTodaysWorkerBinding
    private var myContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayWorkerHolder {
        binding =  ItemRvTodaysWorkerBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return TodayWorkerHolder(binding)
    }

    override fun onBindViewHolder(holder: TodayWorkerHolder, position: Int) {
        holder.setWorkerList(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class TodayWorkerHolder(val binding:ItemRvTodaysWorkerBinding):RecyclerView.ViewHolder(binding.root){
        fun setWorkerList(workerData:TodayWorkerData){

            // 프로필 이미지 불러오기
            if(workerData.workerProfile != null) {
                Glide.with(myContext).load(workerData.workerProfile).circleCrop().into(binding.ivProfile)
            }else{ // null 이면 기본이미지 보여주기
                Glide.with(myContext).load(R.drawable.img_profile_w_58_px_2).circleCrop().into(binding.ivProfile)
            }

            // 포지션 설정
            binding.tvPosition.text = workerData.position

            // 이름 설정
            binding.tvFirstName.text = workerData.first_name
        }
    }
}