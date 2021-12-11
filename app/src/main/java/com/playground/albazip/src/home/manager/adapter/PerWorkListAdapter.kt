package com.playground.albazip.src.home.manager.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.playground.albazip.R
import com.playground.albazip.databinding.ItemRvPersonalWorkListBinding
import com.playground.albazip.src.home.manager.data.HomePerWorkData
import com.playground.albazip.src.home.manager.worklist.ui.HomePersonalPositionActivity

class PerWorkListAdapter(val itemList:ArrayList<HomePerWorkData>, var context: Context): RecyclerView.Adapter<PerWorkListAdapter.PerWorkHolder>() {

    private lateinit var binding: ItemRvPersonalWorkListBinding
    var myContext: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerWorkHolder {
        binding =  ItemRvPersonalWorkListBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return PerWorkHolder(binding)
    }

    override fun onBindViewHolder(holder: PerWorkHolder, position: Int) {
        holder.setItemList(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class PerWorkHolder(val binding: ItemRvPersonalWorkListBinding): RecyclerView.ViewHolder(binding.root){

        fun setItemList(perWorkData: HomePerWorkData){

            // 업무 타이틀
            binding.tvTogether.text = perWorkData.workTitle + " "

            // 업무자 이름
            binding.tvWorkerName.text = perWorkData.workerName

            // 전체 업무 개수
            binding.tvTotalCntTogehter.text = "/ "+perWorkData.totalCnt.toString()

            // 완료 업무 개수
            binding.tvDoneCntTogether.text = perWorkData.completeCnt.toString()

            // 프로그래스 바
            val progress =  (((perWorkData.completeCnt).toDouble() / (perWorkData.totalCnt).toDouble()) * 100).toInt()
            binding.progressBarTogether.progress = progress

            // 꿀단지 상태(이미지뷰)
            if(progress==0){
                Glide.with(myContext).load(R.drawable.img_honey_0).into(binding.ivHoneyProgress)
            }else if(progress in 0..29){
                Glide.with(myContext).load(R.drawable.img_honey_0_to_30).into(binding.ivHoneyProgress)
            }else if(progress in 30..59){
                Glide.with(myContext).load(R.drawable.img_honey_30_to_60).into(binding.ivHoneyProgress)
            }else if(progress >=90){
                Glide.with(myContext).load(R.drawable.img_honey_90).into(binding.ivHoneyProgress)
            }

            // 포지션 별 업무 조회 화면으로 이동
            binding.clRoot.setOnClickListener {
                val nextIntent = Intent(myContext,HomePersonalPositionActivity::class.java)
                nextIntent.putExtra("workId",perWorkData.workId)
                nextIntent.putExtra("title",perWorkData.workTitle)
                myContext.startActivity(nextIntent)
            }


        }
    }
}