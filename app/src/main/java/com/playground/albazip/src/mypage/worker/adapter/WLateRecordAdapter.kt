package com.playground.albazip.src.mypage.worker.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.databinding.ItemRvLateRecordBinding
import com.playground.albazip.src.mypage.worker.data.local.WLateRecordData

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
            if(recordData.real_start_time == ""){ // 만약 출근 기록이 없다면
                binding.tvGoWorkTime.text = "기록 없음"
                binding.tvGoToWork.setTextColor(Color.parseColor("#a3a3a3")) // 텍스트 컬러 변경
                binding.viewGoWork.setBackgroundColor(Color.parseColor("#a3a3a3")) // 배경색 변경
            }else{
                binding.tvGoWorkTime.text = recordData.real_start_time.substring(0,2) + ":" + recordData.real_start_time.substring(2,4)
            }

            // 퇴근 시간
            if(recordData.real_end_time == ""){ // 만약 퇴근 기록이 없다면
                binding.tvOffWorkTime.text = "기록 없음"
                binding.tvGoOffWork.setTextColor(Color.parseColor("#a3a3a3")) // 텍스트 컬러 변경
                binding.viewOffWork.setBackgroundColor(Color.parseColor("#a3a3a3")) // 배경색 변경
            }else{
                binding.tvOffWorkTime.text = recordData.real_end_time.substring(0,2) + ":" + recordData.real_end_time.substring(2,4)
            }

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