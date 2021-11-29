package com.example.albazip.src.community.worker.custom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.albazip.config.BaseResponse
import com.example.albazip.databinding.DialogFragmentLogoutBinding
import com.example.albazip.databinding.DialogFragmentNoticeMenuForMBinding
import com.example.albazip.databinding.DialogFragmentReportReasonBinding
import com.example.albazip.src.community.worker.network.NoticeReportRequest
import com.example.albazip.src.community.worker.network.PutNoticeReportFragmentView
import com.example.albazip.src.community.worker.network.PutNoticeReportService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NoticeReasonBottomSheetDialog(val noticedId: Int) : BottomSheetDialogFragment(),
    PutNoticeReportFragmentView {
    private lateinit var binding: DialogFragmentReportReasonBinding

    private var getNoticeId = noticedId

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentReportReasonBinding.inflate(inflater, container, false)

        Log.d("whyrano",noticedId.toString())


        binding.rlRowOne.setOnClickListener { // 욕설/비하
            val request = NoticeReportRequest(
                getNoticeId,
                binding.tvOne.text.toString()
            )
            PutNoticeReportService(this).tryPutNoticeReport(request)
        }

        binding.rlRowTwo.setOnClickListener { // 음란물/불건전 대화
            val request = NoticeReportRequest(
                getNoticeId,
                binding.tvTwo.text.toString()
            )
            PutNoticeReportService(this).tryPutNoticeReport(request)
        }

        binding.rlRowThree.setOnClickListener { //낚시/놀람/도배
            val request = NoticeReportRequest(
                getNoticeId,
                binding.tvThree.text.toString()
            )
            PutNoticeReportService(this).tryPutNoticeReport(request)
        }

        binding.rlRowFour.setOnClickListener { // 유출/사칭/사기
            val request = NoticeReportRequest(
                getNoticeId,
                binding.tvFour.text.toString()
            )
            PutNoticeReportService(this).tryPutNoticeReport(request)
        }

        binding.rlRowFive.setOnClickListener { // 게시판 성격에 부적절함
            val request = NoticeReportRequest(
                getNoticeId,
                binding.tvFive.text.toString()
            )
            PutNoticeReportService(this).tryPutNoticeReport(request)
        }

        return binding.root
    }

    // 신고 성공
    override fun onPutReportSuccess(response: BaseResponse) {
        Log.d("testingGo", response.message.toString()+response.code.toString())
        dismiss()
    }

    // 신고 실패
    override fun onPutReportFailure(message: String) {
        Log.d("testingGo", message)
        dismiss()
    }
}