package com.example.albazip.src.home.manager.opened.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.albazip.databinding.ItemRvTodaysWorkerBinding
import com.example.albazip.src.home.manager.opened.data.TodayWorkerData

class TodaysWorkerAdapter(val itemList:ArrayList<TodayWorkerData>):RecyclerView.Adapter<TodaysWorkerAdapter.TodayWorkerHolder>() {

    private lateinit var binding:ItemRvTodaysWorkerBinding

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
            // 이미지 설정

            // 포지션 설정
            binding.tvPosition.text = workerData.position

            // 이름 설정
            binding.tvFirstName.text = workerData.first_name
        }
    }
}