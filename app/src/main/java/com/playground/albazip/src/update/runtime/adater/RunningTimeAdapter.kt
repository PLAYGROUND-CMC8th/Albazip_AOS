package com.playground.albazip.src.update.runtime.adater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.databinding.ItemRvRunningTimeBinding
import com.playground.albazip.src.update.runtime.data.RunningTimeData
import com.playground.albazip.util.GetTimeDiffUtil

class RunningTimeAdapter(val setBtnVisibilityOff: () -> Unit, val setDoneOn: () -> Unit, val setDoneOff: () -> Unit) :
    RecyclerView.Adapter<RunningTimeAdapter.RunningTimeViewHolder>() {

    var runningTimeItemList = mutableListOf<RunningTimeData>()
    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(v: View, position: Int, tag: String)
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

        // 2. 개별 item 요소의 시간을 설정할 수 있게 해준다.
        fun setItemTimeEvent(position: Int) {

            binding.clOpen.setOnClickListener { // 오픈 시간 설정
                itemClickListener.onClick(binding.clOpen, position, "SET_OPEN_HOUR")
            }

            binding.clClose.setOnClickListener { // 마감 시간 설정
                itemClickListener.onClick(binding.clOpen, position, "SET_CLOSE_HOUR")
            }
        }

        // 3. 개별 Time 을 받아온 후의 UI를 변경
        fun initItemHourUIAfterInput(data: RunningTimeData) {
            binding.tvOpenHour.isEnabled = data.openFlag // 텍스트 요소 설정
            binding.tvCloseHour.isEnabled = data.closeFlag
        }

        // 4. 휴무일 버튼 이벤트
        fun setRestDayBtnEvent(position: Int) {
            binding.cbRestDay.setOnClickListener {
                if (it.isSelected) {
                    it.isSelected = false
                    runningTimeItemList[position].restState = false
                } else {
                    if (binding.cb24Hour.isSelected) { // 24 시간 버튼 이 켜져 있는 상태면
                        binding.cb24Hour.isSelected = false // 꺼주기
                    }
                    runningTimeItemList[position].restState = true
                    runningTimeItemList[position].time24State = false
                    it.isSelected = true
                }

                initRestDayUI(it.isSelected, position)
            }
        }

        // 5. 24 시간 버튼 이벤트
        fun set24HourBtnEvent(position: Int) {
            binding.cb24Hour.setOnClickListener {
                if (it.isSelected) {
                    it.isSelected = false
                    runningTimeItemList[position].time24State = false
                } else {
                    if (binding.cbRestDay.isSelected) { // 휴일 버튼이 켜져 있는 상태면
                        binding.cbRestDay.isSelected = false // 꺼주기
                    }
                    it.isSelected = true

                    runningTimeItemList[position].time24State = true
                    runningTimeItemList[position].restState = false

                    init24HourUI(position)
                }

                init24HourUI(it.isSelected, position)
            }
        }

        // 6. 휴무일 UI 설정
        private fun initRestDayUI(flags: Boolean, position: Int) {
            if (flags == REST_DAY_ON) { // 휴무일 활성화
                // 배경 뷰 끄기
                binding.apply {
                    clOpen.isEnabled = false
                    clClose.isEnabled = false
                }

                runningTimeItemList[position].openFlag = false
                runningTimeItemList[position].closeFlag = false

                runningTimeItemList[position].totalTime = "0시간"

            } else { // 휴무일 비활성화
                // 배경 뷰 켜기
                binding.apply {
                    clOpen.isEnabled = true
                    clClose.isEnabled = true
                }

                runningTimeItemList[position].openFlag = true
                runningTimeItemList[position].closeFlag = true

                runningTimeItemList[position].totalTime = GetTimeDiffUtil().getTimeDiffTxt(
                    runningTimeItemList[position].openTime!!,
                    runningTimeItemList[position].closeTime!!
                )
            }

            notifyItemChanged(position)
            setAllCbVisibility(checkIfDiff())
            checkIsDone()
        }

        // 7. 24 시간 UI 설정
        private fun init24HourUI(flags: Boolean, position: Int) {
            // 배경 뷰 끄기
            if (flags == HOUR_24_ON) {
                binding.apply {
                    clOpen.isEnabled = false
                    clClose.isEnabled = false
                }

                runningTimeItemList[position].openFlag = false
                runningTimeItemList[position].closeFlag = false
            }

            // 배경 뷰 켜기
            else {
                binding.apply {
                    clOpen.isEnabled = true
                    clClose.isEnabled = true
                }

                runningTimeItemList[position].openFlag = true
                runningTimeItemList[position].closeFlag = true
            }

            notifyItemChanged(position)
            setAllCbVisibility(checkIfDiff())
            checkIsDone()
        }

        // 8. 24시간 클릭 시키는 이벤트
        fun initGet24HourSelected(data: RunningTimeData, position: Int) {
            if (data.time24State) {
                binding.cb24Hour.isSelected = true

                // 배경 뷰 끄기
                binding.apply {
                    clOpen.isEnabled = false
                    clClose.isEnabled = false
                }

                runningTimeItemList[position].openFlag = false
                runningTimeItemList[position].closeFlag = false
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
        holder.setItemTimeEvent(position)
        holder.initItemHourUIAfterInput(runningTimeItemList[position])
        holder.setRestDayBtnEvent(position)
        holder.set24HourBtnEvent(position)
        holder.initGet24HourSelected(runningTimeItemList[position], position)
    }

    override fun getItemCount(): Int = runningTimeItemList.size


    // 오픈, 마감 시간 설정
    fun setItemTimeUI(flags: Int, position: Int, displayTime: String) {

        if (flags == 0) {  // 오픈 시간
            runningTimeItemList[position].openTime = displayTime
            runningTimeItemList[position].openFlag = true
        } else { // 마감 시간
            runningTimeItemList[position].closeTime = displayTime
            runningTimeItemList[position].closeFlag = true
        }

        // 총시간 구하기
        runningTimeItemList[position].totalTime = GetTimeDiffUtil().getTimeDiffTxt(
            runningTimeItemList[position].openTime!!,
            runningTimeItemList[position].closeTime!!
        )

        notifyItemChanged(position)
        setAllCbVisibility(checkIfDiff())
        checkIsDone()
    }

    // 24 시간으로 뷰 설정
    fun init24HourUI(position: Int) {
        runningTimeItemList[position].openTime = "00:00"
        runningTimeItemList[position].closeTime = "00:00"
        runningTimeItemList[position].totalTime = "24시간"

        // 텍스트 뷰 비활성화
        runningTimeItemList[position].openFlag = false
        runningTimeItemList[position].closeFlag = false

        // 24 시간 flag 켜기
        runningTimeItemList[position].time24State = true

        notifyItemChanged(position)
        setAllCbVisibility(checkIfDiff())
        checkIsDone()
    }

    // 전체가 24시간으로 설정되었을때
    fun setAll24Hour(openTime:String,endTime:String,totalTIme:String) {
        runningTimeItemList.forEach {
            it.openTime = openTime
            it.closeTime = endTime
            it.totalTime = totalTIme
            it.time24State = true
            it.restState = false
        }

        runningTimeItemList.forEach {
            it.openFlag = false
            it.closeFlag = false
        }

        notifyItemRangeChanged(0, runningTimeItemList.size)
        setAllCbVisibility(checkIfDiff())
        checkIsDone()
    }

    // 전체가 일반시간으로 설정되었을떄
    fun setAllTIme(openTime:String,endTime:String,totalTIme:String) {
        runningTimeItemList.forEach {
            it.openTime = openTime
            it.closeTime = endTime
            it.totalTime = totalTIme
            it.time24State = false
            it.restState = false
        }

        runningTimeItemList.forEach {
            it.openFlag = true
            it.closeFlag = true
        }

        notifyItemRangeChanged(0, runningTimeItemList.size)
        setAllCbVisibility(checkIfDiff())
        checkIsDone()
    }

    // 값이 달라진 경우가 있다면
    private fun checkIfDiff():Boolean {

        val tempList= runningTimeItemList
        tempList.forEach { it.allTimeFlag = true }

        if (tempList.toMutableSet().size == 1) {
            return false
        }

        return true
    }

    // activity 에 있는 일괄 버튼 비활성화 시키기
    private fun setAllCbVisibility(isDiff:Boolean) {
        if (isDiff) {   // 달라진게 있다면
            setBtnVisibilityOff()// 버튼 꺼주기
        }
    }

    // 기입이 모두 완료되었는지 체크하는 함수
    fun checkIsDone() {

        // 0시간 이고, 휴무일이 체크 되었을 때
        val checkIfZeroAndRest = runningTimeItemList.filter { it.totalTime == "0시간" }.filter { it.restState }
        // 0시간 일 때
        val checkIfAllZero = runningTimeItemList.filter { it.totalTime == "0시간" }

        // 모두 0시간인 경우에서 휴무일 체크된 경우를 뺀다. 이때 0보다 크면 모순이므로 안된다.
        val checkResult = checkIfAllZero - checkIfZeroAndRest

        if (checkResult.isEmpty()) { // 정상 입력
            setDoneOn()
        } else { // 입력 부족
            setDoneOff()
        }

    }

    companion object {
        const val HOUR_24_ON = true
        const val HOUR_24_OFF = false
        const val REST_DAY_ON = true
        const val REST_DAY_OFF = false
    }
}