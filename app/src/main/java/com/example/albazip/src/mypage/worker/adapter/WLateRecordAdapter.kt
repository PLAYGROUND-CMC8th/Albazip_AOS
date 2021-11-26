package com.example.albazip.src.mypage.worker.adapter

import android.graphics.Color
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
            binding.tvDate.text = recordData.year+"."+recordData.month+"."+recordData.day+"."
            // 출근시간
            binding.tvGoWorkTime.text = recordData.start_time.substring(0,2) + ":" + recordData.start_time.substring(2,4)
            // 퇴근 시간
            binding.tvOffWorkTime.text = recordData.end_time.substring(0,2) + ":" + recordData.end_time.substring(2,4)

            // 지각체크
            if(recordData.start_late == 1){ // 출근 지각
                binding.viewGoWork.setBackgroundColor(Color.parseColor("#fb3a00"))
                binding.tvGoToWork.setTextColor(Color.parseColor("#fb3a00"))
            }

            if(recordData.end_late == 1){ // 퇴근 지각
                binding.viewOffWork.setBackgroundColor(Color.parseColor("#fb3a00"))
                binding.tvGoOffWork.setTextColor(Color.parseColor("#fb3a00"))
            }
        }
    }
}