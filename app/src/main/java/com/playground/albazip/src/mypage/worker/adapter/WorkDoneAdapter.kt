package com.playground.albazip.src.mypage.worker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.databinding.ItemRvWorkDoneMonthBinding
import com.playground.albazip.src.mypage.worker.data.local.WorkListData

class WorkDoneAdapter(private val itemList:ArrayList<WorkListData>): RecyclerView.Adapter<WorkDoneAdapter.WorkListHolder>() {

    private lateinit var binding: ItemRvWorkDoneMonthBinding

    // activity 에서 발생할 클릭이벤트
    interface OnItemClickListener{
        fun onItemClick(v : View, position : Int)
    }

    private var listener : OnItemClickListener?=null

    fun setOnItemClickListener(listener:OnItemClickListener){
        this.listener=listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkDoneAdapter.WorkListHolder {
        binding =  ItemRvWorkDoneMonthBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return WorkListHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkListHolder, position: Int) {
        holder.setItemList(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class WorkListHolder(val binding: ItemRvWorkDoneMonthBinding): RecyclerView.ViewHolder(binding.root){

        fun setItemList(workListData: WorkListData){
            // 업무 달
            binding.tvMonth.text = workListData.monthTxt

            // 전체 개수
            binding.tvAllWorkCnt.text = "/"+workListData.allCnt.toString()

            // 완료한 개수
            binding.tvDoneCnt.text = workListData.doneCnt.toString()

            // 프로그래스바 상태
            binding.progressBar.progress = (((workListData.doneCnt).toDouble() / (workListData.allCnt).toDouble()) * 100).toInt()

            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION)
            {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView,pos)
                }
            }


        }
    }
}