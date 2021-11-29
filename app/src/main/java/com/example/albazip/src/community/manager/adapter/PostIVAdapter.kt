package com.example.albazip.src.community.manager.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.albazip.databinding.ItemRvCommunityPhotoBinding

class PostIVAdapter(val itemList:ArrayList<PostImgData>, var context: Context): RecyclerView.Adapter<PostIVAdapter.BoardIVHolder>() {

    private lateinit var binding: ItemRvCommunityPhotoBinding
    var myContext: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardIVHolder {
        binding =  ItemRvCommunityPhotoBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return BoardIVHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardIVHolder, position: Int) {
        holder.setItemList(itemList[position])

        // 사진 삭제하기
        holder.binding.ibtnDelImg.setOnClickListener {
            itemList.removeAt(position)
            notifyDataSetChanged()
        }

        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int = itemList.size

    inner class BoardIVHolder(val binding: ItemRvCommunityPhotoBinding): RecyclerView.ViewHolder(binding.root){

        fun setItemList(postImgData: PostImgData){
            Glide.with(myContext).load(postImgData.img_path).into(binding.ivPutNoticeImg)
        }
    }
}

data class PostImgData(val img_path: Uri)