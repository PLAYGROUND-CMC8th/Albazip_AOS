package com.example.albazip.src.home.manager.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.example.albazip.databinding.ItemVpHomeCommunicateBinding
import com.example.albazip.src.home.common.data.HomeCommuData

class HomeVPAdapter(private val context: Context,private val itemList: ArrayList<HomeCommuData>) :
    RecyclerView.Adapter<HomeVPAdapter.HomeVPHolder>() {

    val myContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeVPHolder {
        val binding =
            ItemVpHomeCommunicateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeVPHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeVPHolder, position: Int) {
        val dataList = itemList[position]
        holder.bind(dataList)
    }

    override fun getItemCount(): Int = itemList.size

    inner class HomeVPHolder(var binding: ItemVpHomeCommunicateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: HomeCommuData) {

            // 미확인 뷰 체크
            if (data.newFlags == 0) { // 미확인
                binding.rlNew.visibility = View.VISIBLE
            } else {
                binding.rlNew.layoutParams = ViewGroup.LayoutParams(dpToPx(myContext,16f).toInt(),0) // constraint 관계만 남기기 위한 코드
                binding.rlNew.visibility = View.INVISIBLE
            }

            // 텍스트 받아오기
            binding.tvVpTitle.text = data.contentTxt
        }

    }

    fun dpToPx(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }

}