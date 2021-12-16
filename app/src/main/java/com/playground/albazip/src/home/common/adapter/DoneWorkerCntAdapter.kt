package com.playground.albazip.src.home.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.playground.albazip.databinding.ItemRvWorkerCntPopUpBinding
import com.playground.albazip.src.home.common.data.DoneWorkerCntData
import com.playground.albazip.src.home.manager.worklist.network.InnerCoWorker


class DoneWorkerCntAdapter(
    private val itemList: ArrayList<DoneWorkerCntData>,
    private val context: Context,
): RecyclerView.Adapter<DoneWorkerCntAdapter.DoneWorkerCntHolder>() {

    private lateinit var binding: ItemRvWorkerCntPopUpBinding
    private var myContext = context

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
            Glide.with(myContext).load(doneData.profile).circleCrop().into(binding.ivProfile)

            // 포지션
            binding.tvPosition.text = doneData.position

            // 이름
            binding.tvFirstName.text = doneData.firstName

            // 확인한 사람 수
            if(doneData.doneCnt == -1) {
                binding.tvWorkCnt.visibility = View.GONE
            }else{ // 완료한 사람 수
                binding.tvWorkCnt.text = doneData.doneCnt.toString()
            }
        }
    }
}