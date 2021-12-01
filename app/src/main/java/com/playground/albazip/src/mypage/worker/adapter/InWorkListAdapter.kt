package com.playground.albazip.src.mypage.worker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.databinding.InnerItemRvWorklistBinding
import com.playground.albazip.src.mypage.worker.data.local.InWorkListData

class InWorkListAdapter(private val itemList:ArrayList<InWorkListData>): RecyclerView.Adapter<InWorkListAdapter.InWorkListHolder>() {

    private lateinit var binding: InnerItemRvWorklistBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InWorkListAdapter.InWorkListHolder {
        binding =  InnerItemRvWorklistBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return InWorkListHolder(binding)
    }

    override fun onBindViewHolder(holder: InWorkListAdapter.InWorkListHolder, position: Int) {
        holder.setInWordList(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class InWorkListHolder(val binding: InnerItemRvWorklistBinding): RecyclerView.ViewHolder(binding.root){

        fun setInWordList(inWorkListData: InWorkListData){
            // 내용
            binding.tvContent.text = inWorkListData.contentTxt
            // 날짜
            binding.tvTime.text = inWorkListData.timeTxt
        }
    }
}