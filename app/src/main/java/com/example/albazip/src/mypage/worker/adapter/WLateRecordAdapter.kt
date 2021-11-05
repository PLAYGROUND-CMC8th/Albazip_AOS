package com.example.albazip.src.mypage.worker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.albazip.databinding.ItemRvLateRecordBinding
import com.example.albazip.src.mypage.worker.data.local.WLateRecordData

class WLateRecordAdapter(private val itemList:ArrayList<WLateRecordData>):RecyclerView.Adapter<WLateRecordAdapter.LateRecordHolder>() {

    private lateinit var binding: ItemRvLateRecordBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WLateRecordAdapter.LateRecordHolder {
        binding =  ItemRvLateRecordBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return LateRecordHolder(binding)
    }

    override fun onBindViewHolder(holder: WLateRecordAdapter.LateRecordHolder, position: Int) {
       holder.setRecordList(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class LateRecordHolder(val binding: ItemRvLateRecordBinding): RecyclerView.ViewHolder(binding.root){

        fun setRecordList(recordData: WLateRecordData){
            // 날짜
            binding.tvDate.text = recordData.dateTxt
            // 출근시간
            binding.tvGoWorkTime.text = recordData.goTxt
            // 퇴근 시간
            binding.tvOffWorkTime.text = recordData.offTxt
        }
    }
}