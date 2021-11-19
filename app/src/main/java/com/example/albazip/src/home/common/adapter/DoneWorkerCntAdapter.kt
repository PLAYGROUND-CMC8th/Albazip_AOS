package com.example.albazip.src.home.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.albazip.databinding.ItemRvWorkerCntPopUpBinding
import com.example.albazip.src.home.common.data.DoneWorkerCntData


class DoneWorkerCntAdapter(private val itemList:ArrayList<DoneWorkerCntData>): RecyclerView.Adapter<DoneWorkerCntAdapter.DoneWorkerCntHolder>() {

    private lateinit var binding: ItemRvWorkerCntPopUpBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoneWorkerCntHolder {
        binding =  ItemRvWorkerCntPopUpBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return DoneWorkerCntHolder(binding)
    }

    override fun onBindViewHolder(holder: DoneWorkerCntHolder, position: Int) {

        holder.setItemList(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class DoneWorkerCntHolder(val binding: ItemRvWorkerCntPopUpBinding): RecyclerView.ViewHolder(binding.root){

        fun setItemList(doneData: DoneWorkerCntData){

            // 프로필

            // 포지션
            binding.tvPosition.text = doneData.position

            // 이름
            binding.tvFirstName.text = doneData.firstName

            // 완료 업무 수
            binding.tvWorkCnt.text = doneData.doneCnt.toString()
        }
    }
}