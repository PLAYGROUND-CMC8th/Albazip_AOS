package com.example.albazip.src.mypage.manager.workerlist.cardevent.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentCardPositionInfoBinding
import com.example.albazip.src.mypage.manager.workerlist.cardevent.data.WorkerInfo
import com.example.albazip.src.mypage.manager.workerlist.custom.DelPositionBottomSheetDialog
import com.example.albazip.src.mypage.worker.init.data.PositionInfo
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat

class CardPositionChildFragment(positionInfo: PositionInfo,flags:Int,positionId:Int): BaseFragment<ChildFragmentCardPositionInfoBinding>(ChildFragmentCardPositionInfoBinding::bind,
    R.layout.child_fragment_card_position_info) {

    private val getPositionId = positionId
    private val getPositionInfo = positionInfo
    private val getFlags = flags

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 삭제 버튼 이벤트
        if (getFlags == 0){ // 근무자 부재
            binding.rlDeleteBtn.setOnClickListener {
                DelPositionBottomSheetDialog(getPositionId).show(childFragmentManager,"del_position")
            }
        }else{ // 근무자 존재
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

        // 월급 타입
        var salaryType = ""
        when(getPositionInfo.salaryType){
            0 -> { salaryType = "시급"}
            1 -> {salaryType = "주급"}
            2 -> {salaryType = "월급"}
        }

        // 총 근무시간
        var workTime = ""
        if(getPositionInfo.workTime.substring(0,1) == "0"){ // 0100, 0130
            if(getPositionInfo.workTime.substring(2,4) == "00"){ // 1시간
                workTime = getPositionInfo.workTime.substring(1,2) + "시간)"
            }else{ // 1시간 30분
                workTime = getPositionInfo.workTime.substring(1,2) + "시간 " + getPositionInfo.workTime.substring(3,4)+"분)"
            }
        }else{ // 1030, 1000
            if(getPositionInfo.workTime.substring(2,4) == "00"){ // 10시간
                workTime = getPositionInfo.workTime.substring(0,2) + "시간)"
            }else{ // 10시간 30분
                workTime = getPositionInfo.workTime.substring(0,2) + "시간 " + getPositionInfo.workTime.substring(3,4)+"분)"
            }
        }

        // 급여 단위 표시
        //DecimalFormat 객체 선언 실시 (소수점 표시 안함)
        val t_dec_up = DecimalFormat("#,###")
        var salary = t_dec_up.format(getPositionInfo.salary)

        // 근무시간
        binding.tvWorkTime.text = getPositionInfo.startTime.substring(0,2) + ":" + getPositionInfo.startTime.substring(2,4) + " ~ " + getPositionInfo.endTime.substring(0,2) +
                ":"+getPositionInfo.endTime.substring(2,4) + " 까지 (총 " + workTime

        // 휴게시간
        binding.tvRestTime.text = "휴게시간 " + getPositionInfo.breakTime

        // 근무요일
        binding.tvWorkingDay.text = getPositionInfo.workDay

        // 급여
        binding.tvSalary.text = salaryType + " " + salary + "원"

    }
}