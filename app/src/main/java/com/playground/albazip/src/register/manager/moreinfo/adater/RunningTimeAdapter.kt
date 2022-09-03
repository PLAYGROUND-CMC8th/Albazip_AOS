package com.playground.albazip.src.register.manager.moreinfo.adater

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.R
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
        holder.setUI(getItem(position))
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

        fun setUI(data: TimeData) {

            // 액티비티에서 전체 체크를 할 때 변화 감지
            if (data.allDayState == true) {
                binding.clOpen.isEnabled = false
                binding.clClose.isEnabled = false
            } else {
                binding.clOpen.isEnabled = true
                binding.clClose.isEnabled = true
            }

            // '24시간' 체크를 할 때 변화 감지 리스너
            binding.cb24Hour.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) { // 누르면 데이터 변경
                    // 휴무일 버튼 끄기
                    data.restState = false
                    binding.cbRestDay.isChecked = false

                    data.allDayState = true // 24시간 영업
                    binding.clOpen.isEnabled = false
                    binding.clClose.isEnabled = false
                    // 텍스트 색상 변경
                    binding.tvOpenHour.setTextColor(binding.root.context.getColor(R.color.gray6_cecece))
                    binding.tvCloseHour.setTextColor(binding.root.context.getColor(R.color.gray6_cecece))

                    // 총시간 설정
                    binding.tvTotalTime.text = "24시간"
                } else {
                    data.allDayState = false
                    binding.clOpen.isEnabled = true
                    binding.clClose.isEnabled = true
                    binding.tvTotalTime.text = "0시간"
                }
            }

            // '휴무일' 체크를 할 때 변화 감지 리스너
            binding.cbRestDay.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) { // 누르면 데이터 변경
                    // 24시간 버튼 끄기
                    data.allDayState = false
                    binding.cb24Hour.isChecked = false

                    data.restState = true // 24시간 영업
                    binding.clOpen.isEnabled = false
                    binding.clClose.isEnabled = false
                    // 텍스트 색상 변경
                    binding.tvOpenHour.setTextColor(binding.root.context.getColor(R.color.gray6_cecece))
                    binding.tvCloseHour.setTextColor(binding.root.context.getColor(R.color.gray6_cecece))

                    // 총시간 설정
                    binding.tvTotalTime.text = "0시간"
                } else {
                    data.allDayState = false
                    binding.clOpen.isEnabled = true
                    binding.clClose.isEnabled = true
                }
            }

           // 텍스트 활성화 여부
           if (data.textActivate == false) {
               // 텍스트 색상 변경 -> 비활성화
               binding.tvOpenHour.setTextColor(binding.root.context.getColor(R.color.gray6_cecece))
               binding.tvCloseHour.setTextColor(binding.root.context.getColor(R.color.gray6_cecece))
           } else {
               // 텍스트 색상 변경 -> 활성화
               binding.tvOpenHour.setTextColor(binding.root.context.getColor(R.color.text4_343434))
               binding.tvCloseHour.setTextColor(binding.root.context.getColor(R.color.text4_343434))
           }
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