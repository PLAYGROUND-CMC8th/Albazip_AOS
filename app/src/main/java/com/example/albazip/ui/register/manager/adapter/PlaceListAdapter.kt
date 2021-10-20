package com.example.albazip.ui.register.manager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.albazip.databinding.ItemRvSearchResultBinding
import com.example.albazip.ui.register.manager.PlaceData

class PlaceListAdapter(var context: Context, var itemList: ArrayList<PlaceData>) :
    RecyclerView.Adapter<PlaceListAdapter.PlaceHolder>() {

    val rvContext: Context = context
    private lateinit var binding: ItemRvSearchResultBinding

    interface ItemClick
    {
        fun onClick(view: View, position: Int)
    }
    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceHolder {
        val binding =
            ItemRvSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PlaceHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceHolder, position: Int) {
        val placeList = itemList[position]
        holder.setItemList(placeList)

        if(itemClick != null)
        {
            holder?.itemView?.setOnClickListener { v ->
                itemClick?.onClick(v, position)
            }
        }

        // 배경 활성화 여부 결정
        holder.itemView.isSelected = placeList.flags == true
    }

    override fun getItemCount(): Int = itemList.size

    inner class PlaceHolder(private val binding: ItemRvSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setItemList(itemList: PlaceData) {
            binding.tvPlaceName.text = itemList.name
            binding.tvPlaceLocation.text = itemList.address
        }
    }
}