package com.playground.albazip.src.mypage.manager.workerlist.cardevent.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.playground.albazip.R
import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.databinding.ChildFragmentCardPositionInfoBinding
import com.playground.albazip.src.mypage.manager.MMyPageFragment
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.network.GetPPositionInfoResponse
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.network.PPositionInfoFragmentView
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.network.PPositionInfoService
import com.playground.albazip.src.mypage.manager.workerlist.custom.DelPositionBottomSheetDialog
import com.playground.albazip.src.mypage.manager.workerlist.network.DelPositionFragmentView
import com.playground.albazip.src.mypage.manager.workerlist.network.DelPositionService
import com.playground.albazip.src.mypage.worker.init.data.PositionInfo
import com.playground.albazip.src.mypage.worker.position.adapter.WorkScheduleAdapter
import java.text.DecimalFormat

class CardPositionChildFragment(positionInfo: PositionInfo, flags: Int, positionId: Int) :
    BaseFragment<ChildFragmentCardPositionInfoBinding>(
        ChildFragmentCardPositionInfoBinding::bind,
        R.layout.child_fragment_card_position_info
    ), DelPositionBottomSheetDialog.BottomSheetClickListener, DelPositionFragmentView,
    PPositionInfoFragmentView {

    private val getPositionId = positionId
    private val getPositionInfo = positionInfo
    private val getFlags = flags

    private lateinit var workScheduleAdapter: WorkScheduleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 새로고침 기능 달아주기
        binding.swipelayout.setOnRefreshListener {
            // 서버 통신 시도
            PPositionInfoService(this).tryGetPositionInfo(getPositionId)
        }

        setWorkingDayUI()
        setDeleteEvent() // 근무자 삭제 이벤트
        setRestTimeUI()
        setSalaryUI()

    }

    // 근무일
    private fun setWorkingDayUI() {
        workScheduleAdapter = WorkScheduleAdapter()
        workScheduleAdapter.scheduleList = getPositionInfo.workSchedule
        binding.rvWorkSchedule.adapter = workScheduleAdapter
    }

    // 근무자 삭제 이벤트
    private fun setDeleteEvent() {
        if (getFlags == 0) { // 근무자 부재
            binding.rlDeleteBtn.setOnClickListener {
                DelPositionBottomSheetDialog().show(childFragmentManager, "del_position")
            }
        } else { // 근무자 존재
            binding.rlDeleteBtn.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.rectangle_fill_custom_white_radius_20
            )

            binding.rlDeleteBtn.setOnClickListener {
                Snackbar.make(binding.root, "근무자가 등록된 상태에선 포지션을 삭제할 수 없습니다.", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(Color.parseColor("#5b5b5b"))
                    .show()
            }
        }
    }

    // 쉬는시간
    private fun setRestTimeUI() {
        binding.tvRestDay.text = getPositionInfo.breakTime
    }

    // 페이
    private fun setSalaryUI() {
        var salaryType = ""
        when (getPositionInfo.salaryType) {
            0 -> {
                salaryType = "시급"
            }
            1 -> {
                salaryType = "주급"
            }
            2 -> {
                salaryType = "월급"
            }
        }

        binding.tvSalary.text =
            salaryType + DecimalFormat("#,###").format(getPositionInfo.salary.toInt()) + "원"
    }

    override fun onItemSelected(isDeleteClicked: Boolean) {
        // 바텀시트에서 포지션 삭제버튼 클릭 했을 때
        if (isDeleteClicked == true) {

            // 포지션 삭제 서버 통신
            DelPositionService(this).tryDelWorkerPosition(getPositionId)
            showLoadingDialog(requireContext())
        }
    }

    // 포지션 삭제 성공
    override fun onPositionDelSuccess(response: BaseResponse) {
        dismissLoadingDialog()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.manager_main_frm, MMyPageFragment()).commitNow()
        ApplicationClass.prefs.setInt("backStackState", 0) // 백스택 관리
    }

    // 포지션 삭제 실패
    override fun onPositionDelFailure(message: String) {
        dismissLoadingDialog()
    }

    // 포지션 정보 새로고침 성공
    override fun onGetPositionInfoSuccess(response: GetPPositionInfoResponse) {
        binding.swipelayout.isRefreshing = false


    }

    override fun onGetPositionInfoFailure(message: String) {
        binding.swipelayout.isRefreshing = false
    }
}