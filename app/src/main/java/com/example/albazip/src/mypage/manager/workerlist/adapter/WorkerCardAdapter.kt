package com.example.albazip.src.mypage.manager.workerlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.albazip.R
import com.example.albazip.databinding.ItemRvWorkerCardBinding
import com.example.albazip.src.mypage.manager.workerlist.data.local.CardData

class WorkerCardAdapter(val itemList:ArrayList<CardData>,val context: Context): RecyclerView.Adapter<WorkerCardAdapter.CardHolder>() {

    private lateinit var binding: ItemRvWorkerCardBinding
    private var myContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        binding =  ItemRvWorkerCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return CardHolder(binding)
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.setCardList(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class CardHolder(val binding: ItemRvWorkerCardBinding): RecyclerView.ViewHolder(binding.root){

        fun setCardList(cardData: CardData){

            // 활성화 상태
            // 비활성화 카드면 -> 배경 off , 이미지 기본
            if(cardData.status == 0){

            }else{
                // 활성화 카드면 -> 배경 on
                binding.clParent.background =  ContextCompat.getDrawable(
                    myContext,
                    R.drawable.bg_card_active
                )

                // 프로필 이미지 불러오기
                Glide.with(myContext).load(R.drawable.img_w_profile_1_24_px).circleCrop().into(binding.ivProfile)

                // 직급
                binding.tvTitle.text = cardData.rank

                // 이름
                binding.tvRegister.text = cardData.first_name

                // 타임
                binding.tvPosition.text = cardData.title
            }

        }
    }
}