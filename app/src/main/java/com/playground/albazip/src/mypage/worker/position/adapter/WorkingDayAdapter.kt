package com.playground.albazip.src.mypage.worker.position.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.databinding.ItemRvWorkScheduleBinding
import com.playground.albazip.src.mypage.worker.init.data.PositionInfo
import com.playground.albazip.util.GetTimeDiffUtil

class WorkScheduleAdapter :
    RecyclerView.Adapter<WorkScheduleAdapter.WorkScheduleViewHolder>() {

    var scheduleList = listOf<PositionInfo.WorkSchedule>()

    inner class WorkScheduleViewHolder(private val binding: ItemRvWorkScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: PositionInfo.WorkSchedule) {
            binding.apply {
                tvDay.text = data.day

                val startTime =
                    data.startTime.substring(0, 2) + ":" + data.startTime.substring(2, 4)
                val endTime = data.endTime.substring(0, 2) + ":" + data.endTime.substring(2, 4)

                val totalTime = GetTimeDiffUtil().getTimeDiffTxt(startTime, endTime)

                tvHour.text = "$startTime ~ $endTime (${totalTime})"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkScheduleViewHolder {
        return WorkScheduleViewHolder(
            ItemRvWorkScheduleBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: WorkScheduleViewHolder, position: Int) {
        holder.onBind(scheduleList[position])
    }

    override fun getItemCount(): Int = scheduleList.size
}