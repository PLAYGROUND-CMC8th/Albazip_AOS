package com.playground.albazip.src.register.manager.moreinfo.adater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.databinding.ItemRvRunningTimeBinding
import com.playground.albazip.src.register.manager.moreinfo.data.TimeData

class RunningTimeAdapter() : ListAdapter<TimeData, RunningTimeAdapter.RunningTimeViewHolder>(
    timeDiffUtil
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunningTimeViewHolder {
        val binding =
            ItemRvRunningTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RunningTimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RunningTimeViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class RunningTimeViewHolder(val binding: ItemRvRunningTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: TimeData) {
            // 요일
            binding.tvDays.text = data.daysTxt
            // 휴무일
            binding.cbRestDay.isChecked = data.restState
            // 24시간
            binding.cb24Hour.isChecked = data.allDayState
            // 오픈시간
            binding.tvOpenHour.text = data.openTimeTxt
            // 마감시간
            binding.tvCloseHour.text = data.closeTimeTxt
            // 총시간
            binding.tvTotalTime.text = data.totalTimeTxt
        }
    }


    companion object {
        private val timeDiffUtil = object : DiffUtil.ItemCallback<TimeData>() {
            override fun areItemsTheSame(oldItem: TimeData, newItem: TimeData): Boolean =
                oldItem.daysTxt == newItem.daysTxt

            override fun areContentsTheSame(oldItem: TimeData, newItem: TimeData): Boolean =
                oldItem == newItem
        }
    }
}