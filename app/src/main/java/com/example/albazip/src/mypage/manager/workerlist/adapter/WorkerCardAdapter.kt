package com.example.albazip.src.mypage.manager.workerlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
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

    interface OnItemClickListener{
        fun onItemClick(v : View, position : Int, outStatus:Int)
    }

    private var listener : OnItemClickListener?=null

    fun setOnItemClickListener(listener:OnItemClickListener){
        this.listener=listener
    }


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

            // 클릭 이벤트 -> 메인으로 값 전달
            binding.root.setOnClickListener {
                listener?.onItemClick(binding.root,adapterPosition,cardData.status)
            }

            // 활성화 상태
            // 비활성화 카드면 -> 배경 off , 이미지 기본
            if(cardData.status == 0){
                // 타임
                binding.tvPosition.text = cardData.title
            }else{
                // 활성화 카드면 -> 배경 on
                binding.clParent.background =  ContextCompat.getDrawable(
                    myContext,
                    R.drawable.bg_card_active
                )

                // 프로필 이미지 불러오기
                if(cardData.image_path != "null") {
                    Glide.with(myContext).load(cardData.image_path).circleCrop().into(binding.ivProfile)
                }else{ // null 이면 기본이미지 보여주기
                    Glide.with(myContext).load(R.drawable.img_profile_w_58_px_2).circleCrop().into(binding.ivProfile)
                }
                // 직급
                binding.tvTitle.text = cardData.rank

                // 이름
                binding.tvRegister.text = cardData.first_name

                // 타임
                binding.tvPosition.text = cardData.title

                // 퇴사요청 알람
                if(cardData.status == 2){
                    binding.ivAlarm.visibility = View.VISIBLE
                }

            }

        }
    }
}