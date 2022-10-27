package com.playground.albazip.src.update.runtime.adater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.databinding.ItemRvRunningTimeBinding
import com.playground.albazip.src.update.runtime.data.RunningTimeData

class RunningTimeAdapter() : RecyclerView.Adapter<RunningTimeAdapter.RunningTimeViewHolder>() {
    var runningTimeItemList = mutableListOf<RunningTimeData>()
    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(v: View, position: Int, flags: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    inner class RunningTimeViewHolder(private val binding: ItemRvRunningTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // 1. 기본 UI 를 설정한다.
        fun onBind(data: RunningTimeData) {
            binding.apply {
                tvDays.text = data.day // 요일

                tvOpenHour.text = data.openTime // 오픈 시간
                tvCloseHour.text = data.closeTime // 마감 시간
                tvTotalTime.text = data.totalTime // 총 시간
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunningTimeViewHolder {
        val binding =
            ItemRvRunningTimeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return RunningTimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RunningTimeViewHolder, position: Int) {
        holder.onBind(runningTimeItemList[position])
    }


    override fun getItemCount(): Int = runningTimeItemList.size
}