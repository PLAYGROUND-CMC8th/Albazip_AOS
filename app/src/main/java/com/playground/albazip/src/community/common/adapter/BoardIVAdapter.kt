package com.playground.albazip.src.community.common.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.playground.albazip.databinding.ItemRvBoardImgListBinding
import com.playground.albazip.src.community.common.ui.PhotoListActivity


class BoardIVAdapter(val itemList:ArrayList<BoardImgData>, var context: Context): RecyclerView.Adapter<BoardIVAdapter.BoardIVHolder>() {

    private lateinit var binding: ItemRvBoardImgListBinding
    var myContext: Context = context
    var imgUriList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardIVHolder {
        binding =  ItemRvBoardImgListBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return BoardIVHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardIVHolder, position: Int) {
        holder.setItemList(itemList[position])

        holder.binding.root.setOnClickListener {
            val photoIntent = Intent(myContext,PhotoListActivity::class.java)

            for(i in 0 until itemList.size){
                imgUriList.add(itemList[i].img_path)
            }
            photoIntent.putExtra("imgList",imgUriList) // 이미지 배열 보내기
            (myContext as AppCompatActivity).startActivity(photoIntent)
        }
    }

    override fun getItemCount(): Int = itemList.size

    inner class BoardIVHolder(val binding: ItemRvBoardImgListBinding): RecyclerView.ViewHolder(binding.root){

        fun setItemList(boardImgData: BoardImgData){
            Glide.with(myContext).load(boardImgData.img_path).centerCrop().into(binding.ivBoardImg)
        }
    }
}

data class BoardImgData(val id:Int,val img_path:String)