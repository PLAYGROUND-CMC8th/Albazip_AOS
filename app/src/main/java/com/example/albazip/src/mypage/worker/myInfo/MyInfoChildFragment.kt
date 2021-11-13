package com.example.albazip.src.mypage.worker.myInfo

import GetWorkerMyInfoResponse
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentMyInfoBinding
import com.example.albazip.src.mypage.worker.init.data.MyInfo
import com.example.albazip.src.mypage.worker.myInfo.network.WorkerInfoFragmentView
import com.example.albazip.src.mypage.worker.myInfo.network.WorkerMyInfoService
import com.example.albazip.src.mypage.worker.myInfo.ui.DoneWorkActivity
import com.example.albazip.src.mypage.worker.myInfo.ui.LateCheckActivity
import com.example.albazip.src.mypage.worker.myInfo.ui.TogetherWorkActivity

class MyInfoChildFragment(val myInfo: MyInfo) : BaseFragment<ChildFragmentMyInfoBinding>(
    ChildFragmentMyInfoBinding::bind,
    R.layout.child_fragment_my_info
), WorkerInfoFragmentView {
    // 서버에서 정보 받아오기
    val getMyInfo = myInfo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 새로고침 기능 달아주기
        binding.swipelayout.setOnRefreshListener {
            // 서버 통신 시도
            WorkerMyInfoService(this).tryGetWMyPage()
        }

        val phonNum = myInfo.userInfo.phone

        // 휴대전화
        binding.tvMyNum.text =
            phonNum.substring(0, 3) + "-" + phonNum.substring(3, 7) + "-" + phonNum.substring(7, 11)
        // 나이
        binding.tvMyBirth.text = myInfo.userInfo.birthyear + "년생"
        // 성별
        if (myInfo.userInfo.gender == 0) {
            binding.tvMySex.text = "여자"
        } else {
            binding.tvMySex.text = "남자"
        }

        // 지각횟수
        binding.tvLate.text = myInfo.workInfo.lateCount.toString()
        // 공동업무 참여 횟수
        binding.tvTogether.text = myInfo.workInfo.coTaskCount.toString()
        // 업무완수율
        binding.tvSuccess.text = myInfo.workInfo.completeTaskCount.toString()

        // 합류 날짜
        binding.tvJoinDate.text = myInfo.joinDate.substring(2, 10).replace("-", ".") + "."

        // 지각 횟수 확인
        binding.rlLate.setOnClickListener {
            val nextIntent = Intent(requireContext(), LateCheckActivity::class.java)
            startActivity(nextIntent)
        }

        // 공동업무 참여 횟수 확인
        binding.rlTogether.setOnClickListener {
            val nextIntent = Intent(requireContext(), TogetherWorkActivity::class.java)
            startActivity(nextIntent)
        }

        // 업무 완수율 확인
        binding.rlSuccess.setOnClickListener {
            val nextIntent = Intent(requireContext(), DoneWorkActivity::class.java)
            startActivity(nextIntent)
        }

    }

    // 서버 통신 성공 (새로고침)
    override fun onMyInfoGetSuccess(response: GetWorkerMyInfoResponse) {
        binding.swipelayout.isRefreshing = false // 새로고침 끝

        if (response.code == 200) {
            val phonNum = response.data.userInfo.phone

            // 휴대전화
            binding.tvMyNum.text =
                phonNum.substring(0, 3) + "-" + phonNum.substring(3, 7) + "-" + phonNum.substring(7, 11)
            // 나이
            binding.tvMyBirth.text = myInfo.userInfo.birthyear + "년생"
            // 성별
            if (response.data.userInfo.gender == 0) {
                binding.tvMySex.text = "여자"
            } else {
                binding.tvMySex.text = "남자"
            }

            // 지각횟수
            binding.tvLate.text = response.data.workInfo.lateCount.toString()
            // 공동업무 참여 횟수
            binding.tvTogether.text = response.data.workInfo.coTaskCount.toString()
            // 업무완수율
            binding.tvSuccess.text = response.data.workInfo.completeTaskCount.toString()

            // 합류 날짜
            binding.tvJoinDate.text = response.data.joinDate.substring(2, 10).replace("-", ".") + "."

        } else {
            showCustomToast("새로고침 실패")
        }
    }

    override fun onMyInfoGetFailure(message: String) {
        binding.swipelayout.isRefreshing = false // 새로고침 끝
        showCustomToast("새로고침 실패")
    }
}