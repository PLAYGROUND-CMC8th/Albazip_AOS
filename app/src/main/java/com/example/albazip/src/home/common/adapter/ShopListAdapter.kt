package com.example.albazip.src.home.common.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.albazip.databinding.ItemRvShopListBinding
import com.example.albazip.src.home.common.data.ShopListData

class ShopListAdapter(private val itemList:ArrayList<ShopListData>): RecyclerView.Adapter<ShopListAdapter.ShopListHolder>() {

    private lateinit var binding: ItemRvShopListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListAdapter.ShopListHolder {
        binding =  ItemRvShopListBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ShopListHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopListAdapter.ShopListHolder, position: Int) {

        holder.setItemList(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class ShopListHolder(val binding: ItemRvShopListBinding): RecyclerView.ViewHolder(binding.root){

        fun setItemList(shopData: ShopListData){

            // 활성화 여부
            if(shopData.status == 1){
                binding.rootLl.setBackgroundColor(Color.parseColor("#fffaea"))
            }

            // 매장이름
            binding.tvStoreName.text = shopData.shop_name

            // id 정보
            if(shopData.worker_id != 0 && shopData.manger_id == 0){ // 근무자 매장일 때
                binding.ibtnEdit.visibility = View.GONE
                binding.ibtnTrash.visibility = View.GONE
            }


        }
    }
}