package com.example.albazip.src.mypage.manager.workerlist.cardevent.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentMyInfoBinding
import com.example.albazip.src.mypage.manager.workerlist.cardevent.data.ExistWorkerInfo
import com.example.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.commute.DetailLateCheckActivity
import com.example.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.taskinfo.DetailDoneWorkActivity
import com.example.albazip.src.mypage.manager.workerlist.outworker.custom.ResponseRealOutBottomSheetDialog


class CardWorkerInfoChildFragment(existWorkerInfo: ExistWorkerInfo,val positionId:Int,val name:String):BaseFragment<ChildFragmentMyInfoBinding>(ChildFragmentMyInfoBinding::bind,
    R.layout.child_fragment_my_info) {

    val getWorkerInfo = existWorkerInfo
    private val getPositionId = positionId // 근무자 고유 id
    private val worker_name = name

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

        // 퇴사시키기
        binding.ibtnExit.setOnClickListener {
            ResponseRealOutBottomSheetDialog(getPositionId,worker_name).show(requireActivity().supportFragmentManager,"real_response_out")
        }

        // 지각횟수
        binding.tvLate.text = getWorkerInfo.workInfo.lateCount.toString()
        // 공동업무 참여 횟수
        binding.tvTogether.text = getWorkerInfo.workInfo.coTaskCount.toString()
        // 업무완수율
        binding.tvSuccess.text = getWorkerInfo.workInfo.completeTaskCount.toString()

        // 합류 날짜
        binding.tvJoinDate.text = getWorkerInfo.joinDate.substring(2, 10).replace("-", ".") + "."

        // *********** 상세정보 조회 **********
        // 지각횟수
        binding.rlLate.setOnClickListener {
            val nextIntent = Intent(requireContext(), DetailLateCheckActivity::class.java)
            nextIntent.putExtra("positionId",getPositionId)
            startActivity(nextIntent)
        }

        // 공동업무 참여횟수
        binding.rlTogether.setOnClickListener {

        }

        // 업무 완수율
        binding.rlSuccess.setOnClickListener {
            val nextIntent = Intent(requireContext(), DetailDoneWorkActivity::class.java)
            nextIntent.putExtra("positionId",getPositionId)
            startActivity(nextIntent)
        }

    }
}