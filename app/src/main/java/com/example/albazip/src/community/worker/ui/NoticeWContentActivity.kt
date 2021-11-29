package com.example.albazip.src.community.worker.ui

import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.albazip.config.BaseActivity
import com.example.albazip.config.BaseResponse
import com.example.albazip.databinding.ActivityNoticeContentBinding
import com.example.albazip.src.community.common.adapter.BoardIVAdapter
import com.example.albazip.src.community.common.adapter.BoardImgData
import com.example.albazip.src.community.common.network.GetReadNoticeFragmentView
import com.example.albazip.src.community.common.network.GetReadNoticeService
import com.example.albazip.src.community.common.network.ReadNoticeResponse
import com.example.albazip.src.community.worker.custom.NoticeReportBottomSheetDialog
import com.example.albazip.src.community.worker.network.PutConfirmNoticeFragmentView
import com.example.albazip.src.community.worker.network.PutConfirmNoticeService
import com.example.albazip.src.mypage.custom.LogoutBottomSheetDialog

// 공지글
class NoticeWContentActivity:
    BaseActivity<ActivityNoticeContentBinding>(ActivityNoticeContentBinding::inflate),
    GetReadNoticeFragmentView,PutConfirmNoticeFragmentView {

    private lateinit var boardIVAdapter: BoardIVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GetReadNoticeService(this).tryGetNoticeRead(intent.getIntExtra("noticeId",0))
        showLoadingDialog(this)

        // 뒤로가기 버튼
        binding.ibtnBack.setOnClickListener {
            finish()
        }

        // 메뉴선택 (신고하기)
        binding.ivMenu.setOnClickListener {
            NoticeReportBottomSheetDialog(intent.getIntExtra("noticeId",0)).show(supportFragmentManager, "NoticeWAlert")
        }
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
        // 데이터를 다 불러온 후 공지 <<확인>> 체크
        PutConfirmNoticeService(this).tryPutNoticeRead(intent.getIntExtra("noticeId",0))
        showLoadingDialog(this)
    }

    override fun onGetReadNoticeFailure(message: String) {
        dismissLoadingDialog()
    }

    // 공지확인 성공
    override fun onPutConfirmNoticeSuccess(response: BaseResponse) {
        dismissLoadingDialog()
    }

    // 공지확인 실패
    override fun onPutConfirmNoticeFailure(message: String) {
        dismissLoadingDialog()
    }
}