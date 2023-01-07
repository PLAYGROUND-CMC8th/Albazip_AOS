package com.playground.albazip.src.update.setworker.custom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.playground.albazip.databinding.DialogFragment24HourBinding
import com.playground.albazip.util.GetTimeDiffUtil

class SetAllWorkTimePickerBottomSheetDialog : BottomSheetDialogFragment(),
    SetAllWorkNextTimePickerBottomSheetDialog.BottomSheetClickListener {
    private lateinit var binding: DialogFragment24HourBinding
    lateinit var bottomSheetClickListener: BottomSheetClickListener

    var openFlag = false
    var closeFlag = false

    override fun onAttach(context: Context) {
        super.onAttach(context)

        bottomSheetClickListener =
            context as BottomSheetClickListener
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragment24HourBinding.inflate(inflater, container, false)

        setLayout()
        initTimeDialog()
        initConfirmEvent()

        return binding.root
    }

    private fun setLayout() {

        binding.apply {
            tv24Hour.visibility = View.GONE
            cb24Hour.visibility = View.GONE

            tv24HourTitle.text = "근무시간을 입력해주세요."
            tv24HourSubTitle.text = "기존에 입력했던 근무시간은 모두 사라져요."
        }
    }

    // 시간 다이얼로그 초기화(바텀시트 열기)
    private fun initTimeDialog() {
        // 오픈 선택
        binding.clOpen.setOnClickListener {
            val openHour = binding.tvOpenHour.text.split(":")[0].toInt()
            val openMin = binding.tvOpenHour.text.split(":")[1].toInt()
            SetAllWorkNextTimePickerBottomSheetDialog(0, openHour, openMin).show(
                childFragmentManager,
                "openAllTimePicker"
            )
        }

        // 마감 선택
        binding.clClose.setOnClickListener {
            val closeHour = binding.tvCloseHour.text.split(":")[0].toInt()
            val closeMin = binding.tvCloseHour.text.split(":")[1].toInt()
            SetAllWorkNextTimePickerBottomSheetDialog(1, closeHour, closeMin).show(
                childFragmentManager,
                "closeAllTimePicker"
            )
        }
    }

    /** 다음 바텀시트 클릭 이벤트에서 시간을 받아온 후 레이아웃을 세팅해준다.*/
    override fun onTimeSelected(h: String, m: String, flag: Int) {
        val displayTime = GetTimeDiffUtil().getDisplayTime(h, m)

        if (flag == 0) { // 오픈시간 설정
            binding.tvOpenHour.text = displayTime
            binding.tvOpenHour.isEnabled = true
            openFlag = true

            if (closeFlag) {
                GetTimeDiffUtil().getTimeDiff(
                    binding.tvOpenHour,
                    binding.tvCloseHour,
                    binding.tvTotalTime
                )
            }

            // 마감시간이 활성화 되어 있을 때 -> 24시간 여부 체크
            if (binding.tvCloseHour.isEnabled) {
                areYou24Hour(0)
            }


        } else { // 마감시간 설정
            binding.tvCloseHour.text = displayTime
            binding.tvCloseHour.isEnabled = true
            closeFlag = true

            if (openFlag) {
                GetTimeDiffUtil().getTimeDiff(
                    binding.tvOpenHour,
                    binding.tvCloseHour,
                    binding.tvTotalTime
                )
            }

            // 오픈시간이 활성화 되어 있을 때 -> 24시간 여부 체크
            if (binding.tvOpenHour.isEnabled) {
                areYou24Hour(1)
            }
        }

        checkBtnState()
    }

    // 시간이 같을 때 24시간 설정 여부 묻기
    private fun areYou24Hour(flags: Int) {
        // 시간이 같을 때 24시간 여부 묻기
        if (binding.tvTotalTime.text == "0시간") {
            if (flags == 0) { // 출근 다시 받기
                val openHour = binding.tvOpenHour.text.split(":")[0].toInt()
                val openMin = binding.tvOpenHour.text.split(":")[1].toInt()
                SetAllWorkNextTimePickerBottomSheetDialog(0,openHour,openMin).show(
                    childFragmentManager,
                    "SET_START_HOUR"
                )
                Toast.makeText(requireContext(), "퇴근 시간과 같아요. 시간을 다시 설정해주세요.", Toast.LENGTH_SHORT).show()
            } else { // 퇴근 다시 받기
                val closeHour = binding.tvCloseHour.text.split(":")[0].toInt()
                val closeMin = binding.tvCloseHour.text.split(":")[1].toInt()
                SetAllWorkNextTimePickerBottomSheetDialog(1,closeHour,closeMin).show(
                    childFragmentManager,
                    "SET_END_HOUR"
                )
                Toast.makeText(requireContext(), "출근 시간과 같아요. 시간을 다시 설정해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    //  확인 버튼 이벤트
    private fun initConfirmEvent() {
        binding.btnConfirm.setOnClickListener {
            val allOpenHour = binding.tvOpenHour.text.toString()
            val allCloseHour = binding.tvCloseHour.text.toString()
            val allTotalTime = binding.tvTotalTime.text.toString()
            // 데이터를 액티비티에 넘기기
            bottomSheetClickListener.onTimeAllTimeSelected(
                allOpenHour,
                allCloseHour,
                allTotalTime,
            )
            // 종료
            dismiss()
        }
    }

    private fun checkBtnState() {
        binding.btnConfirm.isEnabled = binding.tvTotalTime.text != "0시간"
    }

    // activity 에 전달된 변수들
    interface BottomSheetClickListener {
        fun onTimeAllTimeSelected(allOpenHour: String, allCloseHour: String, allTotalTime: String)
    }
}