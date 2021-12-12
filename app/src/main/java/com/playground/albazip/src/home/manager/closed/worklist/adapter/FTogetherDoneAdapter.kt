package com.playground.albazip.src.home.manager.closed.worklist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.databinding.ItemRvDoneCheckTogetherBinding
import com.playground.albazip.src.home.common.custom.DoneCancelBottomSheetDialog
import com.playground.albazip.src.home.manager.worklist.network.InnerCoWorker
import com.playground.albazip.src.home.worker.opened.worklist.data.HDoneWorkListData

class FTogetherDoneAdapter(private val fm: FragmentManager, private val context: Context, private val itemList:ArrayList<HDoneWorkListData>,comWorker: ArrayList<InnerCoWorker>): RecyclerView.Adapter<FTogetherDoneAdapter.DoneWorkHolder>() {

    private lateinit var binding: ItemRvDoneCheckTogetherBinding
    private var popComWorker = comWorker

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoneWorkHolder {
        binding =  ItemRvDoneCheckTogetherBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return DoneWorkHolder(binding)
    }

    override fun onBindViewHolder(holder: DoneWorkHolder, position: Int) {

        holder.itemView.setOnClickListener {
            Toast.makeText(context,"이미 완료된 업무입니다.", Toast.LENGTH_SHORT).show()
        }

        holder.setItemList(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class DoneWorkHolder(val binding: ItemRvDoneCheckTogetherBinding): RecyclerView.ViewHolder(binding.root){

        fun setItemList(doneData: HDoneWorkListData){

            // 업무 명
            binding.tvTitle.text = doneData.titleTxt

            // 완료 시각
            binding.tvTime.text = doneData.timeTxt

            // 체크버튼 숨기기 -> 여기는 완료된 구역이니까 ㅎ
            binding.checkboxFinish.visibility = View.GONE
            binding.frameDisable.visibility = View.GONE

            // 완료 여부
            binding.checkboxFinish.isChecked = doneData.doneFlags != 0


            // 막는 뷰 초기화
            if(binding.checkboxFinish.isChecked == true){ // 체크가 되어 있는 상태라면
                binding.frameDisable.visibility = View.VISIBLE // 체크 일단 막기
            }else{
                binding.frameDisable.visibility = View.GONE
            }

        }
    }

    fun showCancelBottomSheetDialog(cb: CheckBox, delView: View, taskId:Int){
        DoneCancelBottomSheetDialog(cb,delView,taskId,0,popComWorker).show(fm, "cancel")
    }

}