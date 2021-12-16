package com.playground.albazip.src.home.worker.opened.worklist.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.R
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.databinding.ItemRvUndoneCheckBinding
import com.playground.albazip.src.home.common.network.PutTodayHomeTaskFragmentView
import com.playground.albazip.src.home.common.network.PutTodayHomeTaskService
import com.playground.albazip.src.home.manager.custom.DelCoWorkBottomSheetDialog
import com.playground.albazip.src.home.worker.opened.worklist.data.HUnDoneWorkListData
import com.playground.albazip.src.home.worker.opened.worklist.ui.HomeWTodayToDoListActivity


class HWUnDoneAdapter(
    private val itemList: ArrayList<HUnDoneWorkListData>,
    context: Context,
    dialogView: View,
) : RecyclerView.Adapter<HWUnDoneAdapter.UnDoneWorkHolder>(), PutTodayHomeTaskFragmentView {

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

        // 해당 업무 완료
        holder.binding.checkboxFinish.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked == true) { // flag 변경
                showDoneDialog(itemList[holder.adapterPosition].taskId)
                itemList[position].doneFlags = 1
            } else {
                itemList[position].doneFlags = 0
            }
        }

        // 해당 업무 삭제
        holder.binding.ibtnWorkDelete.setOnClickListener {
            // 업무 삭제
            DelCoWorkBottomSheetDialog(itemList[holder.adapterPosition].taskId).show((myContext as AppCompatActivity).supportFragmentManager, "delCoAlert")
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

        // 해당 item의 checkbutton 보이기
        holder.binding.checkboxFinish.visibility = View.VISIBLE

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

    // 모든 작업 완료 다이얼로그 띄우기
    fun showDoneDialog(taskId:Int) {
        // 다이얼로그 띄우기
        val mBuilder = AlertDialog.Builder(myContext, R.style.MyDialogTheme).setView(dialogView)


        // view의 중복 사용을 방지하기 위한 코드
        if (dialogView.parent != null)
            (dialogView.parent as ViewGroup).removeView(dialogView)

        val mAlertDialog = mBuilder.show()

        PutTodayHomeTaskService(this).tryPutTodayTask(taskId)

        /*GlobalScope.launch {
            delay(10000L)
            print("in Coroutine ...")
        }*/

    }

    // 업무완료 - 미완료
    override fun onPutTodayTaskSuccess(response: BaseResponse) {

        // fragment 교체
        val nextIntent = Intent(myContext, HomeWTodayToDoListActivity::class.java)
        myContext.startActivity(nextIntent)
        (myContext as Activity).finish()
    }

    override fun onPutTodayTaskFailure(message: String) {

    }
}