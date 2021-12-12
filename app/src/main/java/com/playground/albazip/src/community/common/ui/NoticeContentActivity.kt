package com.playground.albazip.src.community.common.ui

import android.content.Intent
import android.os.Bundle
import android.security.identity.ResultData
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityNoticeContentBinding
import com.playground.albazip.databinding.BgCntReadPopupBinding
import com.playground.albazip.src.community.common.adapter.BoardIVAdapter
import com.playground.albazip.src.community.common.adapter.BoardImgData
import com.playground.albazip.src.community.common.network.GetReadNoticeFragmentView
import com.playground.albazip.src.community.common.network.GetReadNoticeService
import com.playground.albazip.src.community.common.network.ReadNoticeResponse
import com.playground.albazip.src.community.manager.custom.ManagerNoticeMenuBottomSheetDialog
import com.playground.albazip.src.home.common.adapter.DoneWorkerCntAdapter
import com.playground.albazip.src.home.common.data.DoneWorkerCntData

// 공지글(관리자)
class NoticeContentActivity :
    BaseActivity<ActivityNoticeContentBinding>(ActivityNoticeContentBinding::inflate),
    GetReadNoticeFragmentView {

    private lateinit var boardIVAdapter: BoardIVAdapter

    // 업무 완료자 리스트
    private var workerCntList = ArrayList<DoneWorkerCntData>()
    private lateinit var doneWorkerCntAdapter: DoneWorkerCntAdapter

    // 팝업 view
    private lateinit var popUpBinding: BgCntReadPopupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        popUpBinding = BgCntReadPopupBinding.inflate(layoutInflater)

        // 관리자 메뉴 조회
        binding.ivMenu.setOnClickListener {
            ManagerNoticeMenuBottomSheetDialog(intent.getIntExtra("noticeId", 0)).show(
                supportFragmentManager!!,
                "DelNoticeAlert"
            )
        }
    }

    override fun onResume() {
        super.onResume()

        GetReadNoticeService(this).tryGetNoticeRead(intent.getIntExtra("noticeId", 0))
        showLoadingDialog(this)
    }

    override fun onGetReadNoticeSuccess(response: ReadNoticeResponse) {
        dismissLoadingDialog()

        // 뒤로가기
        binding.ibtnBack.setOnClickListener {
            finish()
        }

        // writerInfo
        // 직업
        binding.tvPosition.text = response.data.writerInfo.title

        // 이름
        binding.tvName.text = response.data.writerInfo.name
        // 프로필
        Glide.with(this).load(response.data.writerInfo.image).circleCrop()
            .into(binding.ivWriterProfile)

        // boardInfo
        // 제목
        binding.tvTitle.text = response.data.boardInfo.title
        // 내용
        binding.tvContent.text = response.data.boardInfo.content
        // 날짜
        binding.tvWroteDate.text =
            response.data.boardInfo.registerDate.substring(0, 10).replace("-", ".") + "."

        // 이미지
        var imgList = ArrayList<BoardImgData>()
        for (i in 0 until response.data.boardInfo.image.size) {
            imgList.add(
                BoardImgData(
                    response.data.boardInfo.image[i].id,
                    response.data.boardInfo.image[i].image_path
                )
            )
        }
        BoardIVAdapter(imgList, this)

        boardIVAdapter = BoardIVAdapter(imgList, this)
        binding.rvNoticeImageList.adapter = boardIVAdapter


        // 읽은 사람 목록 adpater
        for (i in 0 until response.data.confirmInfo.confirmer.size) {
            workerCntList.add(
                DoneWorkerCntData(
                    response.data.confirmInfo.confirmer[i].writerImage,
                    response.data.confirmInfo.confirmer[i].writerTitle,
                    response.data.confirmInfo.confirmer[i].writerName,
                    -1
                )
            )
        }

        var wct = workerCntList

        if(workerCntList.isNotEmpty()) {
            wct = workerCntList.distinct() as ArrayList<DoneWorkerCntData>
        }else{
            wct = workerCntList
        }
        doneWorkerCntAdapter = DoneWorkerCntAdapter(wct, this@NoticeContentActivity)
        popUpBinding.rvDoneWorkerList.adapter = doneWorkerCntAdapter

        binding.tvDonePersonCnt.text = response.data.confirmInfo.count.toString()

        // 팝업 띄우기 -> 사람 목록을 눌렀을 때
        binding.rlReadPersonCnt.setOnClickListener {

            // dp to px 단위변경
            val density = resources.displayMetrics.density
            val w_value = (140 * density).toInt()
            val h_value = ((130+40) * density).toInt()
            val moved_w_value = (100 * density).toInt()

            // val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val moved_h_value_0 = ((130-30) * density).toInt()
            val moved_h_value_1=  ((130) * density).toInt()
            val moved_h_value_2=  ((130+40) * density).toInt()
            val moved_h_value_3=  ((130+70) * density).toInt()
            var focusable = true

            popUpBinding.tvPopUp.text = "확인한 인원"

            val popupWindow = PopupWindow(popUpBinding.root, w_value, height, focusable)
            popupWindow.contentView = popUpBinding.root
            popupWindow.elevation = 5F

            when (wct.size) {

                0 -> {
                    popupWindow.showAsDropDown(binding.rlReadPersonCnt, 0, -(moved_h_value_0)) // O.K
                }
                1 -> {
                    popupWindow.showAsDropDown(binding.rlReadPersonCnt, 0, -(moved_h_value_1)) // O.K
                }
                2 -> {
                    popupWindow.showAsDropDown(binding.rlReadPersonCnt, 0, -(moved_h_value_2)) // O.K
                }
                else -> { // 3개 이상
                    val popupWindow = PopupWindow(popUpBinding.root, w_value, h_value, focusable)
                    popupWindow.showAsDropDown(binding.rlReadPersonCnt, 0, -(moved_h_value_3))
                }
            }

        }
    }

    override fun onGetReadNoticeFailure(message: String) {
        dismissLoadingDialog()
    }
}