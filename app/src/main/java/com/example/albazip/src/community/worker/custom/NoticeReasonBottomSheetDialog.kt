package com.example.albazip.src.community.worker.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.albazip.config.BaseResponse
import com.example.albazip.databinding.DialogFragmentReportReasonBinding
import com.example.albazip.src.community.worker.network.NoticeReportRequest
import com.example.albazip.src.community.worker.network.PostNoticeReportService
import com.example.albazip.src.community.worker.network.PutNoticeReportFragmentView
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

        binding.rlRowOne.setOnClickListener { // 욕설/비하
            val request = NoticeReportRequest(
                getNoticeId,
                binding.tvOne.text.toString()
            )
            PostNoticeReportService(this).tryPutNoticeReport(request)
        }

        binding.rlRowTwo.setOnClickListener { // 음란물/불건전 대화
            val request = NoticeReportRequest(
                getNoticeId,
                binding.tvTwo.text.toString()
            )
            PostNoticeReportService(this).tryPutNoticeReport(request)
        }

        binding.rlRowThree.setOnClickListener { //낚시/놀람/도배
            val request = NoticeReportRequest(
                getNoticeId,
                binding.tvThree.text.toString()
            )
            PostNoticeReportService(this).tryPutNoticeReport(request)
        }

        binding.rlRowFour.setOnClickListener { // 유출/사칭/사기
            val request = NoticeReportRequest(
                getNoticeId,
                binding.tvFour.text.toString()
            )
            PostNoticeReportService(this).tryPutNoticeReport(request)
        }

        binding.rlRowFive.setOnClickListener { // 게시판 성격에 부적절함
            val request = NoticeReportRequest(
                getNoticeId,
                binding.tvFive.text.toString()
            )
            PostNoticeReportService(this).tryPutNoticeReport(request)
        }

        return binding.root
    }

    // 신고 성공
    override fun onPutReportSuccess(response: BaseResponse) {
        Toast.makeText(requireContext(),"신고가 접수되었습니다.",Toast.LENGTH_SHORT).show()
        dismiss()
    }

    // 신고 실패
    override fun onPutReportFailure(message: String) {
        dismiss()
    }
}