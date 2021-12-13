package com.playground.albazip.src.mypage.worker.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.R
import com.playground.albazip.databinding.ItemRvUndoneBinding
import com.playground.albazip.src.mypage.worker.data.local.DailyUnDoneWorkListData

class DailyUnDoneAdapter(private val itemList:ArrayList<DailyUnDoneWorkListData>,private val context:Context): RecyclerView.Adapter<DailyUnDoneAdapter.UnDoneWorkHolder>() {

    private lateinit var binding: ItemRvUndoneBinding
    private var myContext = context
    private var flags = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnDoneWorkHolder {
        binding =  ItemRvUndoneBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return UnDoneWorkHolder(binding)
    }

    override fun onBindViewHolder(holder: UnDoneWorkHolder, position: Int) {

        holder.setItemList(itemList[position])

        // 내용 펼치기
        holder.itemView.setOnClickListener {
            if(flags == 0){
                openContents(holder)
            }else if(flags == 1){
                closeContents(holder)
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    inner class UnDoneWorkHolder(val binding: ItemRvUndoneBinding): RecyclerView.ViewHolder(binding.root){

        fun setItemList(unDoneData: DailyUnDoneWorkListData){
            // 타이틀
            binding.tvTitle.text = unDoneData.titleTxt

            // 내용
            if(unDoneData.contentTxt == "" || unDoneData.contentTxt == null){ // 내용없음
                binding.tvContents.text = "내용없음"
            }else{ // 내용존재
                binding.tvContents.text = unDoneData.contentTxt
            }

            // 작성 날짜
            binding.tvWriterAndDate.text = unDoneData.writerAndDateTxt
        }
    }

    // 내용 펼치기
    fun openContents(holder:UnDoneWorkHolder){
        // Divider 보이기
        holder.binding.divider.visibility = View.VISIBLE
        // 날짜 및 작성자 함수 보이기
        holder.binding.tvWriterAndDate.visibility = View.VISIBLE
        // 패딩 넣기
        holder.binding.tvContents.setPadding(0,16,0,16)
        //text max Line 없애기
        holder.binding.tvContents.maxLines = 20
        // 텍스트 색상 변경
        holder.binding.tvContents.setTextColor(Color.parseColor("#6f6f6f"))

        // 배경 (테두리)설정
        holder.binding.root.background = ContextCompat.getDrawable(
            myContext,
            R.drawable.rectangle_fill_light_yellow_radius_yellow_20
        )

        flags = 1
    }

    // 내용 접기
    fun closeContents(holder:UnDoneWorkHolder){
        // Divider 감추기
        holder.binding.divider.visibility = View.GONE
        // 날짜 및 작성자 함수 감추기
        holder.binding.tvWriterAndDate.visibility = View.GONE
        // 패딩 제거
        holder.binding.tvContents.setPadding(0,0,0,0)
        // contents max Line 설정
        holder.binding.tvContents.maxLines = 1
        // 배경 (테두리)설정
        holder.binding.root.background = ContextCompat.getDrawable(
            myContext,
            R.drawable.rectangle_fill_light_yellow_radius_main_yellow_20
        )
        // 텍스트 색상 변경
        holder.binding.tvContents.setTextColor(Color.parseColor("#919191"))

        flags = 0
    }
}