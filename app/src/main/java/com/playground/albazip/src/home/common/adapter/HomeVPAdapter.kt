package com.playground.albazip.src.home.manager.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.R
import com.playground.albazip.databinding.ItemVpHomeCommunicateBinding
import com.playground.albazip.src.community.worker.ui.NoticeWContentActivity
import com.playground.albazip.src.home.common.data.HomeCommuData

class HomeVPAdapter(private val context: Context,private val itemList: ArrayList<HomeCommuData>,jobFlags:Int) :
    RecyclerView.Adapter<HomeVPAdapter.HomeVPHolder>() {

    val myContext = context
    val getJobFlags = jobFlags // 0 관리자 ,1 근무자

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeVPHolder {
        val binding =
            ItemVpHomeCommunicateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeVPHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeVPHolder, position: Int) {
        val dataList = itemList[position]
        holder.bind(dataList)

        // 공지사항 읽기 화면으로 이동
        holder.binding.root.setOnClickListener {
            val readIntent = Intent(myContext, NoticeWContentActivity::class.java)
            readIntent.putExtra("noticeId",itemList[holder.adapterPosition].noticeId)

            // 미확인 화면이라면 flags 로 0 보내기
            if(itemList[position].confirm == 0) {
                readIntent.putExtra("readSwitch", -1)
            }

            (myContext as Activity).startActivity(readIntent)
        }
    }

    override fun getItemCount(): Int = itemList.size

    inner class HomeVPHolder(var binding: ItemVpHomeCommunicateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: HomeCommuData) {

            // 미확인 뷰 체크 -> 근무자 일 때만
            if(getJobFlags == 1) {
                binding.rlNew.visibility = View.VISIBLE
                if (data.newFlags == 0) { // 미확인
                    // 배경 색상변경
                    binding.rlNew.background = ContextCompat.getDrawable(
                        myContext,
                        R.drawable.rectangle_fill_light_red_radius_4
                    )
                    // 글씨색 변경
                    binding.tvDidRead.setTextColor(Color.parseColor("#fb3a00"))

                    // 텍스트 변경
                    binding.tvDidRead.text = "미확인"
                } else {
                    binding.rlNew.background = ContextCompat.getDrawable(
                        myContext,
                        R.drawable.rectangle_fill_light_green_radius_4
                    )
                    binding.tvDidRead.setTextColor(Color.parseColor("#1dbe4e"))
                    binding.tvDidRead.text = "확인"
                }
            }

            // 텍스트 받아오기
            binding.tvVpTitle.text = data.contentTxt
        }

    }

    fun dpToPx(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }

}