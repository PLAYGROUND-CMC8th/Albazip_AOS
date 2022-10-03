package com.playground.albazip.src.update.setworker.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.playground.albazip.R
import com.playground.albazip.databinding.ItemUpdateRvSetWorkerTimeBinding
import com.playground.albazip.src.update.setworker.data.WorkerTimeData
import com.playground.albazip.util.GetTimeDiffUtil


class WorkerTimeAdapter(
    workerTimeList: MutableList<WorkerTimeData>,
    val context: Context
) :
    RecyclerView.Adapter<WorkerTimeAdapter.WorkerTimeViewHolder>() {

    private lateinit var binding: ItemUpdateRvSetWorkerTimeBinding
    private var itemList = workerTimeList

    private lateinit var onWorkerTimeItemClickListener: OnWorkerTimeItemClickListener

    interface OnWorkerTimeItemClickListener {
        fun onWorkerTimeItemClick(view: View, position: Int, timeFlag: Int)
    }

    fun setOnWorkerTimeItemClickListener(listener: OnWorkerTimeItemClickListener) {
        this.onWorkerTimeItemClickListener = listener
    }

    inner class WorkerTimeViewHolder(val binding: ItemUpdateRvSetWorkerTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setItemList(workerTimeData: WorkerTimeData) {

            binding.tvDay.text = workerTimeData.workDay

            binding.tvOpenHour.text = workerTimeData.openTime
            binding.tvCloseHour.text = workerTimeData.closeTime

            binding.ivCheckboxDay.isSelected = workerTimeData.isSelected!!

            binding.tvTotalTime.text = workerTimeData.totalTime
        }

        fun setCheckEvent(workerTimeData: WorkerTimeData) {
            binding.ivCheckboxDay.setOnClickListener {
                if (binding.ivCheckboxDay.isSelected) { // 선택되어있는 상태면 -> 비활성화 시키기
                    binding.ivCheckboxDay.isSelected = false
                    workerTimeData.isSelected = false
                    Glide.with(binding.root).load(R.drawable.ic_circle_check_inactive)
                        .into(binding.ivCheckboxDay)

                    binding.llWorkerTime.visibility = View.GONE
                    binding.tvTotalTime.visibility = View.GONE
                } else { // 비활성화 -> 활성화
                    binding.ivCheckboxDay.isSelected = true
                    workerTimeData.isSelected = true
                    Glide.with(binding.root).load(R.drawable.ic_circle_check_active)
                        .into(binding.ivCheckboxDay)

                    binding.llWorkerTime.visibility = View.VISIBLE
                    binding.tvTotalTime.visibility = View.VISIBLE
                }
            }
        }

        fun setWorkerTime(workerTimeData: WorkerTimeData) {

            // 텍스트 설정
            if (workerTimeData.openTime != "00:00") {
                binding.tvOpenHour.setTextColor(
                    context.resources.getColor(
                        R.color.text4_343434,
                        null
                    )
                )
            } else {
                binding.tvOpenHour.setTextColor(
                    context.resources.getColor(
                        R.color.gray5_e2e2e2,
                        null
                    )
                )
            }

            if (workerTimeData.closeTime != "00:00") {
                binding.tvCloseHour.setTextColor(
                    context.resources.getColor(
                        R.color.text4_343434,
                        null
                    )
                )
            } else {
                binding.tvCloseHour.setTextColor(
                    context.resources.getColor(
                        R.color.gray5_e2e2e2,
                        null
                    )
                )
            }

            // totalTime 설정
            if (workerTimeData.openTime != "00:00" && workerTimeData.closeTime != "00:00") {
                GetTimeDiffUtil().getTimeDiff(
                    binding.tvOpenHour,
                    binding.tvCloseHour,
                    binding.tvTotalTime
                )
            }

            binding.clOpen.setOnClickListener { // 오픈
                onWorkerTimeItemClickListener.onWorkerTimeItemClick(it, adapterPosition, 0)
            }

            binding.clClose.setOnClickListener { // 마감
                onWorkerTimeItemClickListener.onWorkerTimeItemClick(it, adapterPosition, 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerTimeViewHolder {
        binding = ItemUpdateRvSetWorkerTimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return WorkerTimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkerTimeViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.setItemList(itemList[position])
        holder.setCheckEvent(itemList[position])
        holder.setWorkerTime(itemList[position])
    }

    override fun getItemCount() = itemList.size

    /** 선택한 근무시간이 있는지 체크 하는 함수
     * 이는 모든 근무시간을 적용할 수 없을 때
     * 즉, 토스트메세지를 띄울지의 여부를 결정하는 함수이다.
     * true 면 토스티메세지를 띄워준다.*/
    fun checkIfListNull(): Boolean {

        for (i in itemList.indices) {
            if (itemList[i].isSelected == true) {
                return false
            }
        }

        return true
    }

    /** 출근 or 퇴근 시간이 달라졌는 지 체크하는 함수
     * 이는 모든 근무시간의 버튼을 끄기 위한 함수이다.
     * 같으면(=문제가 없으면) true 반환
     * 다르면 false 반환*/
    fun checkIfListSame(timeFlag: Int): Boolean {

        // 출근 시간 체크
        if (timeFlag == 0) {
            for (i in 0..6) {
                if (itemList[i].openTime != itemList[i + 1].openTime) {
                    return false
                }
            }

            return true
        }
        // 퇴근 시간 체크
        else {
            for (i in 0..6) {
                if (itemList[i].closeTime != itemList[i + 1].closeTime) {
                    return false
                }
            }
            return true
        }
    }

    /** 선택한 요소들의 정보를 받기 위한 함수
     *  이는 완료버튼을 눌렀을 때 서버와 통신할 데이터를 넘겨받기 위함이다.*/
    fun getSelectedList(): MutableList<Int> {
        val checkedPositionList = mutableListOf<Int>()
        for (i in itemList.indices) {
            if (itemList[i].isSelected == true) { // 선택된 요소들 체크해서
                checkedPositionList.add(i) // 리스트에 넣어주기
            }
        }

        Log.d("kite", "getItemList: $checkedPositionList")

        return checkedPositionList
    }

    fun setSelectedList(allOpenTime: String, allCloseTime: String, allTotalTime: String) {

        for (position in getSelectedList()) {
            itemList[position].openTime = allOpenTime
            itemList[position].closeTime = allCloseTime
            itemList[position].totalTime = allTotalTime

            Log.d("kite",position.toString())

            notifyItemChanged(position)
        }
    }

    /** 개별 시간 요소를 완료한 후의 UI 변경 작업
     * */
    fun setLayoutAfterTimeSelected(displayTime: String, flag: Int, position: Int) {

        if (flag == 0) { // 오픈 시간
            itemList[position].openTime = displayTime
        } else { // 마감 시간
            itemList[position].closeTime = displayTime
        }

        notifyItemChanged(position)
    }
}