package com.example.albazip.src.mypage.worker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.albazip.databinding.ItemRvTogehterListBinding
import com.example.albazip.src.mypage.worker.data.local.OutWorkListData

class OutWorkListAdapter(val context: Context, private val itemList:List<OutWorkListData>): RecyclerView.Adapter<OutWorkListAdapter.OutWorkListHolder>() {

    private var myContext:Context = context
    private lateinit var binding: ItemRvTogehterListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutWorkListAdapter.OutWorkListHolder {
        binding =  ItemRvTogehterListBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return OutWorkListHolder(binding)
    }

    override fun onBindViewHolder(holder: OutWorkListHolder, position: Int) {
        holder.setInWordList(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class OutWorkListHolder(val binding: ItemRvTogehterListBinding): RecyclerView.ViewHolder(binding.root){

        fun setInWordList(outWorkListData: OutWorkListData){

            // 날짜
            binding.tvDate.text = outWorkListData.dateTxt

            // inner rv의 adapter 객체 생성 후 데이터 전달
            val inWorkListAdapter = InWorkListAdapter(outWorkListData.itemList)

            // inner rv의 레이아웃 설정
            binding.rvWorkList.layoutManager = LinearLayoutManager(myContext,LinearLayoutManager.VERTICAL,false)

            binding.rvWorkList.adapter = inWorkListAdapter
            inWorkListAdapter.notifyDataSetChanged()

        }
    }
}