package com.playground.albazip.src.update.runtime.adater

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.databinding.ItemRvRunningTimeBinding
import com.playground.albazip.src.update.runtime.data.RunningTimeData

class RunningTimeAdapter : RecyclerView.Adapter<RunningTimeAdapter.RunningTimeViewHolder>() {
    var runningTimeItemList = mutableListOf<RunningTimeData>()
    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    class RunningTimeViewHolder(private val binding: ItemRvRunningTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var myData: RunningTimeData? = null

        init {
            // 휴무일 버튼 클릭
            binding.cbRestDay.setOnClickListener {
                if (myData?.time24State == true) { // 24시간 버튼이 켜져있었다면
                    myData?.time24State = false // 24시간 버튼 꺼주기
                }

                myData?.restState = myData?.restState != true
                setBtnUI(myData?.restState!!, myData?.time24State!!)

                Log.d("kite", "rest의 restState: " + myData?.restState.toString())
                Log.d("kite", "rest의 24State: " + myData?.time24State.toString())
            }
            // 24시간 버튼 클릭
            binding.cb24Hour.setOnClickListener {
                if (myData?.restState == true) {  // 휴무일 버튼이 켜져 있었다면
                    myData?.restState = false // 휴무일 버튼 꺼주기
                }

                myData?.time24State = myData?.time24State != true
                setBtnUI(myData?.restState!!, myData?.time24State!!)

                Log.d("kite", "24의 resState:" + myData?.restState.toString())
                Log.d("kite", "24의 24State:" + myData?.time24State.toString())

            }
        }

        fun onBind(data: RunningTimeData) {

            binding.apply {
                tvDays.text = data.day // 요일 설정
                cbRestDay.isSelected = data.restState // 휴무일 선택 여부
                cb24Hour.isSelected = data.time24State // 24시간 선택 여부

                tvOpenHour.text = data.openTime // 오픈 시간
                tvCloseHour.text = data.closeTime // 마감 시간

                tvTotalTime.text = data.totalTime // 총시간

                myData = data
            }
        }

        fun setBtnUI(restState: Boolean, time24State: Boolean) {
            binding.cbRestDay.isSelected = restState
            binding.cb24Hour.isSelected = time24State
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