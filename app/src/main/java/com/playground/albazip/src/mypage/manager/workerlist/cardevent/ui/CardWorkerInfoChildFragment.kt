package com.playground.albazip.src.mypage.manager.workerlist.cardevent.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.playground.albazip.R
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.ChildFragmentMyInfoBinding
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.data.ExistWorkerInfo
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.commute.DetailLateCheckActivity
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.cotask.DetailTogetherWorkActivity
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.taskinfo.DetailDoneWorkActivity
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.network.PMyInfoFragmentView
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.network.PMyInfoResponse
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.network.PMyInfoService
import com.playground.albazip.src.mypage.manager.workerlist.outworker.custom.ResponseRealOutBottomSheetDialog


class CardWorkerInfoChildFragment(existWorkerInfo: ExistWorkerInfo,val positionId:Int,val name:String):BaseFragment<ChildFragmentMyInfoBinding>(ChildFragmentMyInfoBinding::bind,
    R.layout.child_fragment_my_info),PMyInfoFragmentView {

    val getWorkerInfo = existWorkerInfo
    private val getPositionId = positionId // 근무자 고유 id
    private val worker_name = name

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 새로고침 기능 달아주기
        binding.swipelayout.setOnRefreshListener {
            // 서버 통신 시도
            PMyInfoService(this).tryGetExistCard(getPositionId)
        }

        val phonNum = getWorkerInfo.userInfo.phone

        // 휴대전화
        binding.tvMyNum.text =
            phonNum.substring(0, 3) + "-" + phonNum.substring(3, 7) + "-" + phonNum.substring(7, 11)
        // 나이
        binding.tvMyBirth.text = getWorkerInfo.userInfo.birthyear + "년생"
        // 성별
        if (getWorkerInfo.userInfo.gender == 0) {
            binding.tvMySex.text = "남자"
        } else {
            binding.tvMySex.text = "여자"
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
        binding.tvSuccess.text = (((getWorkerInfo.workInfo.completeTaskCount).toDouble() / (getWorkerInfo.workInfo.totalTaskCount).toDouble()) * 100).toInt().toString()

        // 합류 날짜
        binding.tvJoinDate.text = getWorkerInfo.joinDate.substring(2, 10).replace("-", ".") + "."

        // *********** 상세정보 조회 **********
        // 지각횟수
        binding.rlLate.setOnClickListener {
            val nextIntent = Intent(requireContext(), DetailLateCheckActivity::class.java)
            nextIntent.putExtra("lateCnt",binding.tvLate.text.toString())
            nextIntent.putExtra("positionId",getPositionId)
            startActivity(nextIntent)
        }

        // 공동업무 참여횟수
        binding.rlTogether.setOnClickListener {
            val nextIntent = Intent(requireContext(), DetailTogetherWorkActivity::class.java)
            nextIntent.putExtra("positionId",getPositionId)
            startActivity(nextIntent)
        }

        // 업무 완수율
        binding.rlSuccess.setOnClickListener {
            val nextIntent = Intent(requireContext(), DetailDoneWorkActivity::class.java)
            nextIntent.putExtra("positionId",getPositionId)
            startActivity(nextIntent)
        }

    }

    // 새로고침 성공
    override fun onGetMyInfoSuccess(response: PMyInfoResponse) {
        binding.swipelayout.isRefreshing = false // 새로고침 끝
        if (response.code == 200) {
            val phonNum = response.data.userInfo.phone

            // 휴대전화
            binding.tvMyNum.text =
                phonNum.substring(0, 3) + "-" + phonNum.substring(3, 7) + "-" + phonNum.substring(7, 11)
            // 나이
            binding.tvMyBirth.text = response.data.userInfo.birthyear + "년생"
            // 성별
            if (response.data.userInfo.gender == 0) {
                binding.tvMySex.text = "남자"
            } else {
                binding.tvMySex.text = "여자"
            }

            // 지각횟수
            binding.tvLate.text = response.data.workInfo.lateCount.toString()
            // 공동업무 참여 횟수
            binding.tvTogether.text = response.data.workInfo.coTaskCount.toString()
            // 업무완수율
            binding.tvSuccess.text =  (((response.data.workInfo.completeTaskCount).toDouble() / (response.data.workInfo.totalTaskCount).toDouble()) * 100).toInt().toString()
            // 합류 날짜
            binding.tvJoinDate.text = response.data.joinDate.substring(2, 10).replace("-", ".") + "."

        } else {
            showCustomToast("새로고침 실패")
        }
    }

    override fun onGetMyInfoFailure(message: String) {
        binding.swipelayout.isRefreshing = false // 새로고침 끝
    }
}