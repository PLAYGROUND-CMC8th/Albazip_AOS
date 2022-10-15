package com.playground.albazip.src.update.runtime.adater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.databinding.ItemRvRunningTimeBinding
import com.playground.albazip.src.update.runtime.data.RunningTimeData
import com.playground.albazip.util.GetTimeDiffUtil

class RunningTimeAdapter(private val setAllCheckBtnOff: () -> Unit) : RecyclerView.Adapter<RunningTimeAdapter.RunningTimeViewHolder>() {
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

        var myData: RunningTimeData? = null

        init {
            setBtnEvent() // 버튼 선택 이벤트
            setTimeEvent() // 시간 설정 이벤트
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

        /**  24시간과 휴무일을 선택에 따른 클릭 이벤트를 설정해주는 함수  */
        private fun setBtnEvent() {
            // 휴무일 버튼 클릭
            binding.cbRestDay.setOnClickListener {
                if (myData?.time24State == true) { // 24시간 버튼이 켜져있었다면
                    myData?.time24State = false // 24시간 버튼 꺼주기
                }
                myData?.restState = myData?.restState != true
                setBtnUI(myData?.restState!!, myData?.time24State!!)

                setAllCheckBtnOff()
            }
            // 24시간 버튼 클릭
            binding.cb24Hour.setOnClickListener {
                if (myData?.restState == true) {  // 휴무일 버튼이 켜져 있었다면
                    myData?.restState = false // 휴무일 버튼 꺼주기
                }
                myData?.time24State = myData?.time24State != true
                setBtnUI(myData?.restState!!, myData?.time24State!!)

                setAllCheckBtnOff()
            }
        }

        /** 24시간과 휴무일을 선택에 따른 UI를 변경해주는 함수
         *  */
        private fun setBtnUI(restState: Boolean, time24State: Boolean) {
            binding.cbRestDay.isSelected = restState
            binding.cb24Hour.isSelected = time24State

            // 24시간이 체크되었다면
            if (time24State) {
                binding.tvTotalTime.text = "24시간"
            } else if(restState) { // 휴무체크
                binding.tvTotalTime.text = "0시간"
            }

            // 한쪽이라도 체크 상태면 -> ui 잠금
            if (restState || time24State) {
                binding.apply {
                    // 터치 비활성화
                    clOpen.isEnabled = false
                    clClose.isEnabled = false

                    tvOpenHour.isEnabled = false
                    tvCloseHour.isEnabled = false
                }
            }

            // 둘다 체크 안된 상태면 -> ui 잠금해제 => 총시간 다시 계산
            if (!restState && !time24State) {
                binding.apply {
                    // 터치 활성화 및 배경 활성화
                    clOpen.isEnabled = true
                    clClose.isEnabled = true

                    tvOpenHour.isEnabled = true
                    tvCloseHour.isEnabled = true

                    if (tvOpenHour.text.toString() != "00:00" && tvCloseHour.text.toString() != "00:00"){
                        GetTimeDiffUtil().getTimeDiff(tvOpenHour,tvCloseHour,tvTotalTime) // 총시간 계산해주기
                    }
                }

            }
        }

        /** 시간 설정 이벤트를 다루는 함수 */
        private fun setTimeEvent() {
            // 오픈 시간 설정
            binding.clOpen.setOnClickListener {
                itemClickListener.onClick(it, adapterPosition, 0)
            }

            // 마감 시간 설정
            binding.clClose.setOnClickListener {
                itemClickListener.onClick(it, adapterPosition, 1)
            }
        }


        fun setTimeTxtUI(data: RunningTimeData) {
            if (data.openInputFlag) {
                binding.tvOpenHour.isEnabled = true
            }

            if (data.closeInputFlag) {
                binding.tvCloseHour.isEnabled = true
            }
        }

        // 24시간 설정시 UI 대응
        fun set24HourUI(data: RunningTimeData) {
            if (data.openTime == "00:00" && data.closeTime == "00:00" && data.totalTime == "24시간") {
                binding.apply {
                    cb24Hour.isSelected = true
                    clOpen.isEnabled = false
                    clClose.isEnabled = false

                    tvOpenHour.isEnabled = false
                    tvCloseHour.isEnabled = false
                }
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
        holder.setTimeTxtUI(runningTimeItemList[position])

        holder.set24HourUI(runningTimeItemList[position])
    }


    override fun getItemCount(): Int = runningTimeItemList.size


    fun set24Hour(selectedItemPosition:Int) {
        runningTimeItemList[selectedItemPosition].apply {
            totalTime = "24시간"
            openTime = "00:00"
            closeTime = "00:00"
        }

    }
}