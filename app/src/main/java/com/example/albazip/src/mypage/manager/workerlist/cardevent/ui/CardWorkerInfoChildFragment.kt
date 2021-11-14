package com.example.albazip.src.mypage.manager.workerlist.cardevent.ui

import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentMyInfoBinding
import com.example.albazip.src.mypage.manager.workerlist.cardevent.data.ExistWorkerInfo


class CardWorkerInfoChildFragment(existWorkerInfo: ExistWorkerInfo):BaseFragment<ChildFragmentMyInfoBinding>(ChildFragmentMyInfoBinding::bind,
    R.layout.child_fragment_my_info) {

    val getWorkerInfo = existWorkerInfo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val phonNum = getWorkerInfo.userInfo.phone

        // 휴대전화
        binding.tvMyNum.text =
            phonNum.substring(0, 3) + "-" + phonNum.substring(3, 7) + "-" + phonNum.substring(7, 11)
        // 나이
        binding.tvMyBirth.text = getWorkerInfo.userInfo.birthyear + "년생"
        // 성별
        if (getWorkerInfo.userInfo.gender == 0) {
            binding.tvMySex.text = "여자"
        } else {
            binding.tvMySex.text = "남자"
        }

        // 지각횟수
        binding.tvLate.text = getWorkerInfo.workInfo.lateCount.toString()
        // 공동업무 참여 횟수
        binding.tvTogether.text = getWorkerInfo.workInfo.coTaskCount.toString()
        // 업무완수율
        binding.tvSuccess.text = getWorkerInfo.workInfo.completeTaskCount.toString()

        // 합류 날짜
        binding.tvJoinDate.text = getWorkerInfo.joinDate.substring(2, 10).replace("-", ".") + "."

    }
}