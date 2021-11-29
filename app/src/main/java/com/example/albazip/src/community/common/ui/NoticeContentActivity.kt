package com.example.albazip.src.community.common.ui

import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityNoticeContentBinding
import com.example.albazip.src.community.common.adapter.BoardIVAdapter
import com.example.albazip.src.community.common.adapter.BoardImgData
import com.example.albazip.src.community.common.network.GetReadNoticeFragmentView
import com.example.albazip.src.community.common.network.GetReadNoticeService
import com.example.albazip.src.community.common.network.ReadNoticeResponse

// 공지글
class NoticeContentActivity:BaseActivity<ActivityNoticeContentBinding>(ActivityNoticeContentBinding::inflate),GetReadNoticeFragmentView {

    private lateinit var boardIVAdapter: BoardIVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GetReadNoticeService(this).tryGetNoticeRead(intent.getIntExtra("noticeId",0))
        showLoadingDialog(this)
    }

    override fun onGetReadNoticeSuccess(response: ReadNoticeResponse) {
        dismissLoadingDialog()

        // writerInfo
        // 직업
        binding.tvPosition.text = response.data.writerInfo.title
        // 이름
        binding.tvName.text = response.data.writerInfo.name
        // 프로필
        Glide.with(this).load(response.data.writerInfo.image).circleCrop().into(binding.ivWriterProfile)

        // boardInfo
        // 제목
        binding.tvTitle.text = response.data.boardInfo.title
        // 내용
        binding.tvContent.text = response.data.boardInfo.content
        // 날짜
        binding.tvWroteDate.text = response.data.boardInfo.registerDate.substring(0,10).replace("-",".")+"."
        // 이미지
        var imgList = ArrayList<BoardImgData>()
        for(i in 0 until response.data.boardInfo.image.size){
            imgList.add(BoardImgData(response.data.boardInfo.image[i].id,response.data.boardInfo.image[i].image_path))
        }
        BoardIVAdapter(imgList,this)

        boardIVAdapter = BoardIVAdapter(imgList,this)
        binding.rvNoticeImageList.adapter = boardIVAdapter

        // confirmInfo
    }

    override fun onGetReadNoticeFailure(message: String) {
        dismissLoadingDialog()
    }
}