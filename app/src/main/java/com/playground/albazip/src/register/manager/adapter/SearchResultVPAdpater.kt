package com.playground.albazip.src.register.manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.databinding.ItemVpSearchResultBinding
import com.playground.albazip.src.register.manager.data.local.PlaceData

class SearchResultVPAdpater(private val itemList:ArrayList<PlaceData>): RecyclerView.Adapter<SearchResultVPAdpater.SearchResultPVHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultVPAdpater.SearchResultPVHolder {
        val binding = ItemVpSearchResultBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchResultPVHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultVPAdpater.SearchResultPVHolder, position: Int) {
        val resultList = itemList[position]

        holder.bind(resultList)

        // 배경 활성화 여부 결정
        holder.itemView.isSelected = resultList.flags == true
    }

    override fun getItemCount(): Int = itemList.size

    inner class SearchResultPVHolder(var binding : ItemVpSearchResultBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(data: PlaceData){
            binding.tvPlaceName.text = data.name // 가게명
            binding.tvPlaceLocation.text = data.address // 주소
        }

    }
}