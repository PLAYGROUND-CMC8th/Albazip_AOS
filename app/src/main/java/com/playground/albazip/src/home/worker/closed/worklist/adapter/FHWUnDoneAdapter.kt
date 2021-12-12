package com.playground.albazip.src.home.worker.closed.worklist.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.R
import com.playground.albazip.databinding.ItemRvUndoneCheckBinding
import com.playground.albazip.src.home.worker.opened.worklist.data.HUnDoneWorkListData

class FHWUnDoneAdapter(
    private val itemList: ArrayList<HUnDoneWorkListData>,
    context: Context,
    dialogView: View,
) : RecyclerView.Adapter<FHWUnDoneAdapter.UnDoneWorkHolder>() {

    private lateinit var binding: ItemRvUndoneCheckBinding
    private var myContext = context
    private var flags = 0

    private var dialogView = dialogView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnDoneWorkHolder {
        binding =
            ItemRvUndoneCheckBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return UnDoneWorkHolder(binding)
    }

    override fun onBindViewHolder(holder: UnDoneWorkHolder, position: Int) {

        holder.setItemList(itemList[position])

        // 내용 펼치기
        holder.itemView.setOnClickListener {
            if (flags == 0) {
                openContents(holder)
            } else if (flags == 1) {
                closeContents(holder)
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    inner class UnDoneWorkHolder(val binding: ItemRvUndoneCheckBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setItemList(unDoneData: HUnDoneWorkListData) {
            // 타이틀
            binding.tvTitle.text = unDoneData.titleTxt

            // 내용
            binding.tvContents.text = unDoneData.contentTxt

            // 작성 날짜
            binding.tvWriterAndDate.text = unDoneData.writerAndDateTxt

            // 완료 여부
            binding.checkboxFinish.isChecked = unDoneData.doneFlags != 0

            // 체크박스 숨기기
            binding.checkboxFinish.visibility = View.GONE
        }
    }

    // 내용 펼치기
    fun openContents(holder: UnDoneWorkHolder) {

        // 해당 item의 checkbutton 없애기
        holder.binding.checkboxFinish.visibility = View.GONE

        // Divider 보이기
        holder.binding.divider.visibility = View.VISIBLE
        // 날짜 및 작성자 함수 보이기
        holder.binding.tvWriterAndDate.visibility = View.VISIBLE
        // 패딩 넣기
        holder.binding.tvContents.setPadding(0, 16, 0, 16)
        //text max Line 없애기
        holder.binding.tvContents.maxLines = 20

        // 텍스트 색 변경
        holder.binding.tvContents.setTextColor(Color.parseColor("#6f6f6f"))

        // 배경 (테두리)설정
        holder.binding.root.background = ContextCompat.getDrawable(
            myContext,
            R.drawable.rectangle_fill_light_yellow_radius_yellow_20
        )

        // 삭제 버튼 보이기(관리자만)
        if(itemList[holder.adapterPosition].posFlags == 0) {
            holder.binding.ibtnWorkDelete.visibility = View.VISIBLE
        }
        flags = 1
    }

    // 내용 접기
    fun closeContents(holder: UnDoneWorkHolder) {

        // 해당 item의 checkbutton 무조건 가리기
        holder.binding.checkboxFinish.visibility = View.GONE

        // Divider 감추기
        holder.binding.divider.visibility = View.GONE
        // 날짜 및 작성자 함수 감추기
        holder.binding.tvWriterAndDate.visibility = View.GONE
        // 패딩 제거
        holder.binding.tvContents.setPadding(0, 0, 0, 0)
        // contents max Line 설정
        holder.binding.tvContents.maxLines = 1
        // 배경 (테두리)설정
        holder.binding.root.background = ContextCompat.getDrawable(
            myContext,
            R.drawable.rectangle_fill_light_yellow_radius_main_yellow_20
        )
        // 삭제버튼 숨기기
        holder.binding.ibtnWorkDelete.visibility = View.GONE

        // 텍스트 색 변경
        holder.binding.tvContents.setTextColor(Color.parseColor("#919191"))

        flags = 0
    }

}