package com.example.albazip.src.mypage.manager.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.albazip.config.BaseResponse
import com.example.albazip.databinding.ItemRvNoticeListBinding
import com.example.albazip.src.community.common.ui.NoticeContentActivity
import com.example.albazip.src.community.manager.network.PutNoticePinListFragmentView
import com.example.albazip.src.community.manager.network.PutNoticePinService
import com.example.albazip.src.community.worker.WCommunityFragment
import com.example.albazip.src.mypage.manager.board.data.local.NoticeData
import okhttp3.internal.notifyAll

class NoticeListAdapter(context: Context, val itemList:ArrayList<NoticeData>): RecyclerView.Adapter<NoticeListAdapter.NoticeHolder>(),PutNoticePinListFragmentView {

    private var myContext = context
    private lateinit var binding: ItemRvNoticeListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeListAdapter.NoticeHolder {
        binding =  ItemRvNoticeListBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return NoticeHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeHolder, position: Int) {


        holder.setNoticeList(itemList[position])

        //holder.binding.cbPin.setOnCheckedChangeListener { buttonView, isChecked ->
        //    PutNoticePinService(this).tryPutNoticePin(itemList[holder.adapterPosition].id)
        //}

        // 공지사항 핀 고정 확인
        holder.binding.cbPin.setOnCheckedChangeListener { buttonView, isChecked ->
            PutNoticePinService(this).tryPutNoticePin(itemList[holder.adapterPosition].id)
        }

        // 공지사항 읽기 화면으로 이동
        holder.binding.root.setOnClickListener {
            val readIntent = Intent(myContext,NoticeContentActivity::class.java)
            readIntent.putExtra("noticeId",itemList[holder.adapterPosition].id)
            (myContext as Activity).startActivity(readIntent)
        }

        holder.setIsRecyclable(false)

        // 체크 상태를 저장 (여기서 아마 서버 통신)
       /* holder.binding.cbPin.setOnClickListener {
            if(itemList[holder.adapterPosition].pinState == 0){
                PutNoticePinService(this).tryPutNoticePin(itemList[holder.adapterPosition].id,holder.binding.cbPin)
            }else{
                PutNoticePinService(this).tryPutNoticePin(itemList[holder.adapterPosition].id,holder.binding.cbPin)
            }
            //noticeData.pinState = binding.cbPin.isChecked
            notifyItemChanged(holder.adapterPosition)
        }*/
    }

    override fun getItemCount(): Int = itemList.size

    inner class NoticeHolder(val binding: ItemRvNoticeListBinding): RecyclerView.ViewHolder(binding.root){

        fun setNoticeList(noticeData: NoticeData){

            binding.itemTvTitle.text = noticeData.titleTxt // 글제목
            binding.itemTvDate.text = noticeData.dateTxt // 날짜

            binding.cbPin.isChecked = noticeData.pinState != 0 // 핀 고정 여부
        }
    }


    // 핀 등록 성공
    override fun onNoticePinPutSuccess(response: BaseResponse) {
        if(response.code == 200) {
        }else if(response.code == 202){
            Toast.makeText(myContext,"핀 고정은 최대 5개 입니다.",Toast.LENGTH_SHORT).show()
            //itemList[pos].pinState = 0
            // checkBox.isChecked = false
        }
    }

    // 핀 등록 실패
    override fun onNoticePinPutFailure(message: String) {
        Toast.makeText(myContext,message,Toast.LENGTH_SHORT).show()
    }
}