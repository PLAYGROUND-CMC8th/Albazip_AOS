package com.playground.albazip.src.register.manager.moreinfo.adater

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.R
import com.playground.albazip.databinding.ItemRvRunningTimeBinding
import com.playground.albazip.src.register.manager.moreinfo.RunningTimeActivity
import com.playground.albazip.src.register.manager.moreinfo.custom.RunningTimePickerBottomSheetDialog
import com.playground.albazip.src.register.manager.moreinfo.data.TimeData
import com.playground.albazip.util.GetTimeDiffUtil

class RunningTimeAdapter(val checkDoneTxtState: () -> Unit) :
    ListAdapter<TimeData, RunningTimeAdapter.RunningTimeViewHolder>(
        timeDiffUtil
    ) {

    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunningTimeViewHolder {
        val binding =
            ItemRvRunningTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RunningTimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RunningTimeViewHolder, position: Int) {
        holder.onBind(getItem(position))
        holder.setUI(getItem(position))
        holder.binding.clOpen.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

        holder.binding.clClose.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.setTimeUI(getItem(position))
    }

    inner class RunningTimeViewHolder(val binding: ItemRvRunningTimeBinding) :
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
                Log.d("test", "에잉?" + binding.tvTotalTime.text.toString())
            } else {
                binding.clOpen.isEnabled = true
                binding.clClose.isEnabled = true
                Log.d("test", "떼잉?" + binding.tvTotalTime.text.toString())
            }

            // '24시간' 체크를 할 때 변화 감지 리스너
            binding.cb24Hour.setOnCheckedChangeListener { buttonView, isChecked ->

                if (buttonView.isChecked) { // 누르면 데이터 변경
                    data.restState = false // "휴무일" 버튼 체크 풀기

                    binding.clOpen.isEnabled = false
                    binding.clClose.isEnabled = false
                    // 텍스트 색상 변경
                    binding.tvOpenHour.setTextColor(binding.root.context.getColor(R.color.gray6_cecece))
                    binding.tvCloseHour.setTextColor(binding.root.context.getColor(R.color.gray6_cecece))

                    // 총시간 설정
                    binding.tvOpenHour.text = "00:00"
                    binding.tvCloseHour.text = "00:00"
                    binding.tvTotalTime.text = "24시간"

                    // 휴무일 비활성화 -> 24시간 비활성화 상태일 때
                    if (data.allDayState == false) {
                        binding.cbRestDay.isChecked = false
                    }

                    data.allDayState = true // 24시간 영업

                    /** 24시간 체크되었을 때 해당 position 입력 true*/
                    data.inputState = true
                    checkDoneTxtState()
                    Log.d("test", "24시간 눌렀을 때 24true 휴무일 false")

                } else {
                    data.allDayState = false
                    binding.clOpen.isEnabled = true
                    binding.clClose.isEnabled = true
                    binding.tvTotalTime.text = "0시간"

                    // 둘다 체크 x 면
                    if (data.restState == false && data.allDayState == false) {
                        data.inputState = false
                        checkDoneTxtState()
                    }
                }
            }

            // '휴무일' 체크를 할 때 변화 감지 리스너
            binding.cbRestDay.setOnCheckedChangeListener { buttonView, isChecked ->

                if (buttonView.isChecked) { // 누르면 데이터 변경
                    data.allDayState = false // "24시간" 버튼 체크 풀기

                    /** 휴무일 체크되었을 때 해당 position 입력 true*/
                    data.inputState = true
                    checkDoneTxtState()
                    Log.d("test", "휴무일 눌렀을 때 휴무일 true 24시간 false")

                    // 24시 비활성화 -> 휴무일이 비활성화 상태일 때
                    if (data.restState == false) {
                        binding.cb24Hour.isChecked = false
                    }

                    data.restState = true // 휴무일 미영업
                    binding.clOpen.isEnabled = false
                    binding.clClose.isEnabled = false
                    // 텍스트 색상 변경
                    binding.tvOpenHour.setTextColor(binding.root.context.getColor(R.color.gray6_cecece))
                    binding.tvCloseHour.setTextColor(binding.root.context.getColor(R.color.gray6_cecece))

                    // 총시간 설정
                    binding.tvOpenHour.text = "00:00"
                    binding.tvCloseHour.text = "00:00"
                    binding.tvTotalTime.text = "0시간"

                    Log.d("test", binding.tvTotalTime.text.toString())
                } else {
                    data.restState = false
                    binding.clOpen.isEnabled = true
                    binding.clClose.isEnabled = true

                    // 둘다 체크 x 면
                    if (data.restState == false && data.allDayState == false) {
                        data.inputState = false
                        checkDoneTxtState()
                    }
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

        fun setTimeUI(data: TimeData) {
            // 시간차 텍스트 설정
            GetTimeDiffUtil().getTimeDiff(
                binding.tvOpenHour,
                binding.tvCloseHour,
                binding.tvTotalTime
            )

            if (binding.tvTotalTime.text != "0시간") {
                binding.apply { // 시간 차 o -> 텍스트 활성화
                    tvOpenHour.setTextColor(binding.root.context.getColor(R.color.text4_343434))
                    tvCloseHour.setTextColor(binding.root.context.getColor(R.color.text4_343434))

                    data.inputState = true
                    checkDoneTxtState()
                }
            } else {
                if (!data.allDayState) { // 24시간 check 상태가 아닐 때
                    binding.apply { // 시간 차 x -> 텍스트 비활성화
                        tvOpenHour.setTextColor(binding.root.context.getColor(R.color.gray5_e2e2e2))
                        tvCloseHour.setTextColor(binding.root.context.getColor(R.color.gray5_e2e2e2))

                        data.inputState = false
                        checkDoneTxtState()
                    }
                } else {  // 24시간 check 상태일 때
                    binding.tvTotalTime.text = "24시간"
                    data.inputState = true
                    checkDoneTxtState()
                }
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