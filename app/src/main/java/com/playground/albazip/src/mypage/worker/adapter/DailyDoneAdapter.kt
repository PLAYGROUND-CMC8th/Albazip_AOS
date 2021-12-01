package com.playground.albazip.src.mypage.worker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.databinding.ItemRvDoneBinding
import com.playground.albazip.src.mypage.worker.data.local.DailyDoneWorkListData


class DailyDoneAdapter(private val context: Context,private val itemList:ArrayList<DailyDoneWorkListData>): RecyclerView.Adapter<DailyDoneAdapter.DoneWorkHolder>() {

    private lateinit var binding: ItemRvDoneBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoneWorkHolder {
        binding =  ItemRvDoneBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return DoneWorkHolder(binding)
    }

    override fun onBindViewHolder(holder: DoneWorkHolder, position: Int) {

        holder.itemView.setOnClickListener {
            Toast.makeText(context,"이미 완료된 업무입니다.",Toast.LENGTH_SHORT).show()
        }

        holder.setItemList(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class DoneWorkHolder(val binding: ItemRvDoneBinding): RecyclerView.ViewHolder(binding.root){

        fun setItemList(doneData: DailyDoneWorkListData){

            // 업무 명
            binding.tvTitle.text = doneData.titleTxt

            // 완료 시각
            binding.tvTime.text = doneData.timeTxt
        }
    }
}