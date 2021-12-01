package com.playground.albazip.src.home.common.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.config.ApplicationClass.Companion.prefs
import com.playground.albazip.databinding.ItemRvShopListBinding
import com.playground.albazip.src.home.common.data.ShopListData
import com.playground.albazip.src.home.manager.editshop.ui.DelShopActivity
import com.playground.albazip.src.home.manager.editshop.ui.EditShopInfoOneActivity

class ShopListAdapter(private val itemList:ArrayList<ShopListData>,context:Context): RecyclerView.Adapter<ShopListAdapter.ShopListHolder>() {

    private lateinit var binding: ItemRvShopListBinding
    var myContext = context

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
            if(shopData.shop_name == prefs.getString("login_shop_name","")){
                binding.rootLl.setBackgroundColor(Color.parseColor("#fffaea"))
            }

            // 매장이름
            binding.tvStoreName.text = shopData.shop_name

            // id 정보
            if(shopData.worker_id != 0 && shopData.manger_id == 0){ // 근무자 매장일 때
                binding.ibtnEdit.visibility = View.GONE
                binding.ibtnTrash.visibility = View.GONE
            }

            // 매장 편집하기
            binding.ibtnEdit.setOnClickListener {
                val nexIntent = Intent(myContext,EditShopInfoOneActivity::class.java)
                nexIntent.putExtra("positionId",shopData.manger_id)
                myContext.startActivity(nexIntent)
            }

            // 매장 삭제하기
            binding.ibtnTrash.setOnClickListener {
                val nextIntet = Intent(myContext,DelShopActivity::class.java)
                nextIntet.putExtra("positionId",shopData.manger_id)
                nextIntet.putExtra("shop_name",shopData.shop_name)
                myContext.startActivity(nextIntet)
            }
        }
    }
}