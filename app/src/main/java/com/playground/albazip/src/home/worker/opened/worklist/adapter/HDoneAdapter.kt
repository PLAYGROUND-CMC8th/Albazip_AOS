package com.playground.albazip.src.home.worker.opened.worklist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.databinding.ItemRvDoneCheckBinding
import com.playground.albazip.src.home.worker.opened.worklist.data.HDoneWorkListData

class HDoneAdapter(private val context: Context, private val itemList:ArrayList<HDoneWorkListData>): RecyclerView.Adapter<HDoneAdapter.DoneWorkHolder>() {

    private lateinit var binding: ItemRvDoneCheckBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoneWorkHolder {
        binding =  ItemRvDoneCheckBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return DoneWorkHolder(binding)
    }

    override fun onBindViewHolder(holder: DoneWorkHolder, position: Int) {

        holder.itemView.setOnClickListener {
            Toast.makeText(context,"이미 완료된 업무입니다.", Toast.LENGTH_SHORT).show()
        }

        holder.setItemList(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class DoneWorkHolder(val binding: ItemRvDoneCheckBinding): RecyclerView.ViewHolder(binding.root){

        fun setItemList(doneData: HDoneWorkListData){

            // 업무 명
            binding.tvTitle.text = doneData.titleTxt

            // 완료 시각
            binding.tvTime.text = doneData.timeTxt

            // 완료 여부
            binding.checkboxFinish.isChecked = doneData.doneFlags != 0
        }
    }
}