package com.playground.albazip.src.community.common.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityNoticeContentBinding
import com.playground.albazip.src.community.common.adapter.BoardIVAdapter
import com.playground.albazip.src.community.common.adapter.BoardImgData
import com.playground.albazip.src.community.common.network.GetReadNoticeFragmentView
import com.playground.albazip.src.community.common.network.GetReadNoticeService
import com.playground.albazip.src.community.common.network.ReadNoticeResponse
import com.playground.albazip.src.community.manager.custom.ManagerNoticeMenuBottomSheetDialog

// 공지글(관리자)
class NoticeContentActivity:BaseActivity<ActivityNoticeContentBinding>(ActivityNoticeContentBinding::inflate),GetReadNoticeFragmentView {

    private lateinit var boardIVAdapter: BoardIVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GetReadNoticeService(this).tryGetNoticeRead(intent.getIntExtra("noticeId",0))
        showLoadingDialog(this)

        // 관리자 메뉴 조회
        binding.ivMenu.setOnClickListener {
            ManagerNoticeMenuBottomSheetDialog(intent.getIntExtra("noticeId",0)).show(supportFragmentManager!!, "DelNoticeAlert")
        }
    }

    override fun onGetReadNoticeSuccess(response: ReadNoticeResponse) {
        dismissLoadingDialog()

        // 뒤로가기
        binding.ibtnBack.setOnClickListener {
            finish()
        }

        // writerInfo
        // 직업
        if(response.data.writerInfo.title!=null) {
            binding.tvPosition.text = response.data.writerInfo.title
        }
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