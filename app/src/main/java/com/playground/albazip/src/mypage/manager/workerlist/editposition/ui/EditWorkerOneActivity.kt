package com.playground.albazip.src.mypage.manager.workerlist.editposition.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.playground.albazip.R
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityEditWorkerOneBinding
import com.playground.albazip.src.mypage.manager.custom.PayUnitBottomSheetDialog
import com.playground.albazip.src.mypage.manager.workerlist.editposition.network.GetPositionInfoFragmentView
import com.playground.albazip.src.mypage.manager.workerlist.editposition.network.GetPositionInfoResponse
import com.playground.albazip.src.mypage.manager.workerlist.editposition.network.GetPositionInfoService
import com.playground.albazip.src.register.manager.custom.TimePickerBottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditWorkerOneActivity :
    BaseActivity<ActivityEditWorkerOneBinding>(ActivityEditWorkerOneBinding::inflate),
    View.OnClickListener, TimePickerBottomSheetDialog.BottomSheetClickListener,
    PayUnitBottomSheetDialog.BottomSheetClickListener,GetPositionInfoFragmentView {
    // 시간 차 계산을 위한 데이터 포맷 선언
    val f: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 업데이트 전에 잠시 가리기
        binding.tvWorkHour.visibility = View.INVISIBLE

        // 뒤로가기 버튼
        binding.ibtnBack.setOnClickListener {
            finish()
        }

        // 편집 정보 조회하기
        val intentPositionId = intent.getIntExtra("positionId",0)
        GetPositionInfoService(this).tryGetPositionInfo(intentPositionId)
        showLoadingDialog(this)

        // 알바생
        binding.btnAlba.setOnClickListener {
            selectRank(binding.btnAlba)
            checkingFlags()
        }

        // 직원 -> 스낵 바 띄우기
        binding.btnStaff.setOnClickListener {
            showSnackBar()
        }

        // 다음 버튼
        binding.tvNext.setOnClickListener {

            // 입력 정보 받아오기
            val rank = "알바생"

            var titleOne = ""
            var titleTwo = ""
            val positionOneList =
                arrayListOf<AppCompatButton>(binding.btnWeekday, binding.btnWeekend)
            val positionTwoList =
                arrayListOf<AppCompatButton>(binding.btnOpen, binding.btnMiddle, binding.btnEnd)
            for (i in 0 until positionOneList.size) { // 오픈
                if (positionOneList[i].isSelected == true) {
                    titleOne = positionOneList[i].text.toString()
                }
            }
            for (i in 0 until positionTwoList.size) { // 마감
                if (positionTwoList[i].isSelected == true) {
                    titleTwo = positionTwoList[i].text.toString()
                }
            }

            val title = titleOne + titleTwo
            val startTime = binding.tvInputStartTime.text.toString().replace(":", "")
            val endTime = binding.tvInputOffTime.text.toString().replace(":", "")

            val workDays = arrayListOf<String>()
            binding.apply {
                var workDayList = arrayListOf<AppCompatButton>(
                    btnMon,
                    btnTue,
                    btnWen,
                    btnThur,
                    btnFri,
                    btnSat,
                    btnSun
                )
                for (i in 0 until workDayList.size) { // 오픈
                    if (workDayList[i].isSelected == true) {
                        workDays.add(workDayList[i].text.toString())
                    }
                }
            }

            // 휴식시간
            var breakTime = ""
            val breakList = arrayListOf<AppCompatButton>(
                binding.btnNoRest,
                binding.btn30Min,
                binding.btn60Min,
                binding.btn90Min
            )
            for (i in 0 until breakList.size) { // 오픈
                if (breakList[i].isSelected == true) {
                    breakTime = breakList[i].text.toString()
                }
            }

            val salary = binding.etPayment.text.toString()
            val salaryType = binding.tvSelectedPayUnit.text.toString()


            // 입력 정보 배열에 담기
            val workerDataList: ArrayList<Any> = arrayListOf(
                rank,
                title,
                startTime,
                endTime,
                workDays,
                breakTime,
                salary,
                salaryType,
            )

            // 근무자 추가 두 번째 화면으로 이동
            val nextIntent = Intent(this, EditWorkerTwoActivity::class.java)
            nextIntent.putExtra("workerDataList", workerDataList)
            nextIntent.putExtra("positionId", intentPositionId)
            // 입력정보 넘겨주기
            startActivity(nextIntent)
            finish()
        }

        /// 포지션 선택 버튼(평일/주말)
        binding.btnWeekday.setOnClickListener(this)
        binding.btnWeekend.setOnClickListener(this)
        binding.btnManager.setOnClickListener(this)
        binding.btnStoreManager.setOnClickListener(this)

        /// 포지션 선택 버튼(오픈/미들/마감)
        binding.btnOpen.setOnClickListener(this)
        binding.btnMiddle.setOnClickListener(this)
        binding.btnEnd.setOnClickListener(this)

        /// 요일 선택 버튼
        binding.btnMon.setOnClickListener(this)
        binding.btnTue.setOnClickListener(this)
        binding.btnWen.setOnClickListener(this)
        binding.btnThur.setOnClickListener(this)
        binding.btnFri.setOnClickListener(this)
        binding.btnSat.setOnClickListener(this)
        binding.btnSun.setOnClickListener(this)
        binding.btnRotate.setOnClickListener(this)

        // 휴게시간 선택 버튼
        binding.btnNoRest.setOnClickListener(this)
        binding.btn30Min.setOnClickListener(this)
        binding.btn60Min.setOnClickListener(this)
        binding.btn90Min.setOnClickListener(this)

        // 출근시간 picker
        binding.rlGoWorkTime.setOnClickListener {
            TimePickerBottomSheetDialog("매장 오픈시간", 0).show(supportFragmentManager, "openpicker")
        }

        // 퇴근시간 picker
        binding.rlOffWorkTime.setOnClickListener {
            TimePickerBottomSheetDialog("매장 마감시간", 1).show(supportFragmentManager, "closepicker")
        }

        // 급여 종류 picker
        binding.rlPayOne.setOnClickListener {
            PayUnitBottomSheetDialog(binding.tvSelectedPayUnit.text.toString()).show(
                supportFragmentManager,
                "unitPicker"
            )
        }

        // 급여 (,) 붙이기
        var result = ""
        val decimalFormat: DecimalFormat = DecimalFormat("#,###")
        binding.etPayment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result)) {
                    result = decimalFormat.format((s.toString().replace(",", "").toDouble()))
                    binding.etPayment.setText(result)
                    binding.etPayment.setSelection(result.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }


    override fun onClick(v: View?) {
        binding.apply {
            when (v) {
                /// 포지션 선택 버튼(평일/주말)
                btnWeekday -> {
                    selectPositionDay(v as AppCompatButton)
                    checkingFlags()
                }
                btnWeekend -> {
                    selectPositionDay(v as AppCompatButton)
                    checkingFlags()
                }
                btnManager -> {
                    showSnackBar()
                }
                btnStoreManager -> {
                    showSnackBar()
                }

                /// 포지션 선택 버튼(오픈/미들/마감)
                btnOpen -> {
                    selectPositionTime(v as AppCompatButton)
                    checkingFlags()
                }
                btnMiddle -> {
                    selectPositionTime(v as AppCompatButton)
                    checkingFlags()
                }
                btnEnd -> {
                    selectPositionTime(v as AppCompatButton)
                    checkingFlags()
                }

                /// 요일 선택 버튼
                btnMon -> {
                    selectWorkingDay(v as AppCompatButton)
                    isDayAllUnchecked()
                    checkingFlags()
                }
                btnTue -> {
                    selectWorkingDay(v as AppCompatButton)
                    isDayAllUnchecked()
                    checkingFlags()
                }
                btnWen -> {
                    selectWorkingDay(v)
                    isDayAllUnchecked()
                    checkingFlags()
                }
                btnThur -> {
                    selectWorkingDay(v)
                    isDayAllUnchecked()
                    checkingFlags()
                }
                btnFri -> {
                    selectWorkingDay(v)
                    isDayAllUnchecked()
                    checkingFlags()
                }
                btnSat -> {
                    selectWorkingDay(v)
                    isDayAllUnchecked()
                    checkingFlags()
                }
                btnSun -> {
                    selectWorkingDay(v)
                    isDayAllUnchecked()
                    checkingFlags()
                }
                btnRotate -> {
                    showSnackBar()
                }

                /// 휴게시간 선택 버튼
                btnNoRest -> {
                    selectRestTime(v)
                    checkingFlags()
                }
                btn30Min -> {
                    selectRestTime(v)
                    checkingFlags()
                }
                btn60Min -> {
                    selectRestTime(v)
                    checkingFlags()
                }
                btn90Min -> {
                    selectRestTime(v)
                    checkingFlags()
                }

            }
        }
    }

    // 직급 선택 버튼
    fun selectRank(v: View) {
        if (v.isSelected == true) {
            v.isSelected = false

            // 비활성화 텍스트 색상 변경
            binding.btnAlba.setTextColor(Color.parseColor("#6f6f6f"))

            // 매니저 & 점장 활성화 색상
            binding.btnManager.background = ContextCompat.getDrawable(
                this@EditWorkerOneActivity,
                R.drawable.rectangle_fill_white_radius_gray_16
            )
            binding.btnStoreManager.background = ContextCompat.getDrawable(
                this@EditWorkerOneActivity,
                R.drawable.rectangle_fill_white_radius_gray_16
            )
            // 교대근무 활성화 색상
            binding.btnRotate.background = ContextCompat.getDrawable(
                this@EditWorkerOneActivity,
                R.drawable.rectangle_fill_white_radius_gray_16
            )


        } else {
            v.isSelected = true

            // 활성화 텍스트 색상 변경
            binding.btnAlba.setTextColor(Color.parseColor("#343434"))

            // 매니저 & 점장 비활성화 색상
            binding.btnManager.background =
                ContextCompat.getDrawable(this, R.drawable.rectangle_fill_gray_radius_16)
            binding.btnStoreManager.background =
                ContextCompat.getDrawable(this, R.drawable.rectangle_fill_gray_radius_16)
            // 교대근무 비활성화 색상
            binding.btnRotate.background = ContextCompat.getDrawable(
                this@EditWorkerOneActivity,
                R.drawable.rectangle_fill_gray_radius_16
            )
        }
    }

    // 포지션 선택 버튼(평일/주말)
    fun selectPositionDay(v: AppCompatButton) {
        val dayList: ArrayList<AppCompatButton> =
            arrayListOf(binding.btnWeekday, binding.btnWeekend)

        for (i in 0 until dayList.size) {
            if (dayList[i] == v) {
                if (v.isSelected == true) {
                    v.isSelected = false

                    // 텍스트 색상 비활성화
                    v.setTextColor(Color.parseColor("#6f6f6f"))

                    binding.btnManager.background = ContextCompat.getDrawable(
                        this,
                        R.drawable.rectangle_fill_white_radius_gray_16
                    )
                    binding.btnStoreManager.background = ContextCompat.getDrawable(
                        this,
                        R.drawable.rectangle_fill_white_radius_gray_16
                    )
                } else {
                    v.isSelected = true

                    // 활성화 텍스트 색상 변경
                    v.setTextColor(Color.parseColor("#343434"))

                    binding.btnManager.background =
                        ContextCompat.getDrawable(this, R.drawable.rectangle_fill_gray_radius_16)
                    binding.btnStoreManager.background =
                        ContextCompat.getDrawable(this, R.drawable.rectangle_fill_gray_radius_16)
                }
            } else {
                dayList[i].isSelected = false
                dayList[i].setTextColor(Color.parseColor("#6f6f6f"))
            }
        }
    }

    // 포지션 선택 버튼(오픈/미들/마감)
    fun selectPositionTime(v: AppCompatButton) {
        val timeList: ArrayList<AppCompatButton> =
            arrayListOf(binding.btnOpen, binding.btnMiddle, binding.btnEnd)

        for (i in 0 until timeList.size) {
            if (v == timeList[i]) {
                if (timeList[i].isSelected == true) {
                    timeList[i].isSelected = false
                    // 텍스트 색상 비활성화
                    v.setTextColor(Color.parseColor("#6f6f6f"))
                } else {
                    timeList[i].isSelected = true
                    // 활성화 텍스트 색상 변경
                    v.setTextColor(Color.parseColor("#343434"))
                }
            } else {
                timeList[i].isSelected = false
                // 텍스트 색상 비활성화
                timeList[i].setTextColor(Color.parseColor("#6f6f6f"))
            }
        }
    }

    // 요일 선택 버튼
    fun selectWorkingDay(v: View) {
        // 요일을 선택했을 때
        if (v.isSelected == true) {
            v.isSelected = false
            (v as AppCompatButton).setTextColor(Color.parseColor("#6f6f6f"))
        } else {
            v.isSelected = true
            (v as AppCompatButton).setTextColor(Color.parseColor("#343434"))
        }
    }

    // 모든 버튼 비활성화 여부 체크
    fun isDayAllUnchecked() {
        binding.apply {

            if (!btnMon.isSelected && !btnTue.isSelected && !btnWen.isSelected && !btnThur.isSelected && !btnFri.isSelected && !btnSat.isSelected && !btnSun.isSelected) {
                binding.btnRotate.background = ContextCompat.getDrawable(
                    this@EditWorkerOneActivity,
                    R.drawable.rectangle_fill_white_radius_gray_16
                )
                binding.clVisibleLayout.visibility = View.GONE
            } else {
                binding.btnRotate.background = ContextCompat.getDrawable(
                    this@EditWorkerOneActivity,
                    R.drawable.rectangle_fill_gray_radius_16
                )
                binding.clVisibleLayout.visibility = View.VISIBLE
            }
        }

    }

    fun selectRestTime(v: View) {
        val restTimeList: ArrayList<AppCompatButton> =
            arrayListOf(binding.btnNoRest, binding.btn30Min, binding.btn60Min, binding.btn90Min)

        for (i in 0 until restTimeList.size) {
            if (v == restTimeList[i]) {
                if (restTimeList[i].isSelected == true) {
                    restTimeList[i].isSelected = false
                    (v as AppCompatButton).setTextColor(Color.parseColor("#6f6f6f"))
                } else {
                    restTimeList[i].isSelected = true
                    (v as AppCompatButton).setTextColor(Color.parseColor("#343434"))
                }
            } else {
                restTimeList[i].isSelected = false
                restTimeList[i].setTextColor(Color.parseColor("#6f6f6f"))
            }
        }
    }


    fun showSnackBar() {
        Snackbar.make(binding.root, "해당 직군은 유료서비스로 아직 준비중 입니다 :)", Snackbar.LENGTH_LONG)
            .setBackgroundTint(Color.parseColor("#5b5b5b"))
            .show()
    }

    override fun onTimeSelected(h: String, m: String, flag: Int) {

        var displayHour = ""
        var displayMinute = ""

        // ui에 보여지는 시간과 분
        if (h.length == 1) { // 한자리 숫자일 때는 앞에 "0"을 붙여준다.
            displayHour = "0$h"
        } else {
            displayHour = h
        }

        if (m.length == 1) {
            displayMinute = "0$m"
        } else {
            displayMinute = m
        }


        if (flag == 0) { // 오픈 값 넣기
            binding.tvInputStartTime.setText(displayHour + ":" + displayMinute)
            // 포커스 해제
            //removeFocus()
            binding.tvInputStartTime.setTextColor(Color.parseColor("#343434"))

            // 운영시간 계산
            if (binding.tvInputOffTime.currentTextColor == Color.parseColor("#343434")) { // 마감 시간이 활성화 되어 있으면 시간차를 계산해준다.
                getTimeLag()
            }


        } else { // 마감 값 넣기
            binding.tvInputOffTime.setText(displayHour + ":" + displayMinute)
            // 포커스 해제
            //removeFocus()
            binding.tvInputOffTime.setTextColor(Color.parseColor("#343434"))

            // 운영시간 계산
            if (binding.tvInputStartTime.currentTextColor == Color.parseColor("#343434")) { // 오픈 시간이 활성화 되어 있으면 시간차를 계산해준다.
                getTimeLag()
            }

        }

    }

    // 시간차 설정해주는 함수
    private fun getTimeLag() {
        var sDate = f.parse(binding.tvInputStartTime.text.toString() + ":00")
        var eDate = f.parse(binding.tvInputOffTime.text.toString() + ":00")
        var diff = eDate.time - sDate.time

        var sec = diff / 1000 // 총 시간(초) 받아오기


        if (eDate.time >= sDate.time) {

            var showHour = sec / (60 * 60)
            var showMin = sec / 60 - (showHour * 60)

            if (showMin == 0L) {
                binding.tvWorkHour.setText(showHour.toString() + "시간")
            } else {
                binding.tvWorkHour.setText(showHour.toString() + "시간 " + showMin.toString() + "분")
            }


        } else { // 24시간을 더해서 빼준다.

            var sec = diff / 1000 + 86400// 총 시간(초) 받아오기(24시간 더해서)

            var showHour = sec / (60 * 60)
            var showMin = sec / 60 - (showHour * 60)

            if (showMin == 0L) {
                binding.tvWorkHour.setText(showHour.toString() + "시간")
            } else {
                binding.tvWorkHour.setText(showHour.toString() + "시간 " + showMin.toString() + "분")
            }
        }

    }

    override fun onItemSelected(text: String) {
        binding.tvSelectedPayUnit.text = text
    }

    // 플래그 상태 체크
    fun checkingFlags() {

        // 포지션(1) 체크여부
        val positionOne =
            arrayListOf<Boolean>(binding.btnWeekend.isSelected, binding.btnWeekday.isSelected)
        for (i in 0 until positionOne.size) {
            if (positionOne[i] == true) {
                break
            } else {
            }
        }

        // 포지션(2)
        val positionTwo = arrayListOf<Boolean>(
            binding.btnOpen.isSelected,
            binding.btnMiddle.isSelected,
            binding.btnEnd.isSelected
        )
        for (i in 0 until positionTwo.size) {
            if (positionTwo[i] == true) {
                break
            } else {
            }
        }

        // 근무요일
        val day = arrayListOf<Boolean>(
            binding.btnMon.isSelected,
            binding.btnTue.isSelected,
            binding.btnWen.isSelected,
            binding.btnThur.isSelected,
            binding.btnFri.isSelected,
            binding.btnSat.isSelected,
            binding.btnSun.isSelected
        )
        for (i in 0 until day.size) {
            if (day[i] == true) {
                break
            } else {
            }
        }

        // 휴식시간
        val rest = arrayListOf<Boolean>(
            binding.btn30Min.isSelected,
            binding.btn60Min.isSelected,
            binding.btn90Min.isSelected,
            binding.btnNoRest.isSelected
        )
        for (i in 0 until rest.size) {
            if (rest[i] == true) {
                break
            } else {
            }
        }
    }

    // 편집 정보 가져오기 완료
    override fun onGetPositionInfoSuccess(response: GetPositionInfoResponse) {
        dismissLoadingDialog()

        binding.btnAlba.isSelected = true // 알바생 체크
        binding.btnAlba.setTextColor(Color.parseColor("#343434"))

        //////////////////// 포지션 체크 여부 //////////////////////////////////////////
        // 포지션(1) 체크여부
        val positionOne =
            arrayListOf<AppCompatButton>(binding.btnWeekend, binding.btnWeekday)
        // 포지션(2)
        val positionTwo = arrayListOf<AppCompatButton>(
            binding.btnOpen,
            binding.btnMiddle,
            binding.btnEnd
        )

        val position_one = response.data.title.substring(0,2) // 주말
        val position_two = response.data.title.substring(2,response.data.title.lastIndex+1).replace(" ","") // 미들

        for(i in 0 until positionOne.size){
            if(positionOne[i].text.toString() == position_one){
                positionOne[i].isSelected = true
                positionOne[i].setTextColor(Color.parseColor("#343434"))
            }
        }

        for(i in 0 until positionTwo.size){
            if(positionTwo[i].text.toString() == position_two){
                positionTwo[i].isSelected = true
                positionTwo[i].setTextColor(Color.parseColor("#343434"))
            }
        }

        //////////////////////// 근무 요일 ///////////////////////////////
        val day = arrayListOf<AppCompatButton>(
            binding.btnMon,
            binding.btnTue,
            binding.btnWen,
            binding.btnThur,
            binding.btnFri,
            binding.btnSat,
            binding.btnSun,
        )
        val workDay = response.data.workDay
        for (j in 0 until workDay.size){
            for(i in 0 until day.size){
                if(day[i].text.toString() == workDay[j]){
                    day[i].isSelected = true
                    day[i].setTextColor(Color.parseColor("#343434"))
                }
            }
        }

        // 오픈 시간
        binding.tvInputStartTime.text = response.data.startTime.substring(0,2)+":"+response.data.startTime.substring(2,4)
        // 마감 시간
        binding.tvInputOffTime.text = response.data.endTime.substring(0,2)+":"+response.data.endTime.substring(2,4)

        // ///////////////////////////////  휴식시간
        val rest = arrayListOf<AppCompatButton>(
            binding.btn30Min,
            binding.btn60Min,
            binding.btn90Min,
            binding.btnNoRest
        )
        for (i in 0 until rest.size){
            if(rest[i].text.toString() == response.data.breakTime){
                rest[i].isSelected = true
                rest[i].setTextColor(Color.parseColor("#343434"))
            }
        }

        // 봉급 타입
        when(response.data.salaryType){
            0 -> {binding.tvSelectedPayUnit.text = "시급"}
            1 -> {binding.tvSelectedPayUnit.text = "주급"}
            2 -> {binding.tvSelectedPayUnit.text = "월급"}
        }

        // 급여 (,) 붙이기
        var result = ""
        val decimalFormat: DecimalFormat = DecimalFormat("#,###")
        // 액수
        val salary = decimalFormat.format((response.data.salary.replace(",", "").toDouble()))
        binding.etPayment.setText(salary)

    }

    override fun onGetPositionInfoFailure(message: String) {
        dismissLoadingDialog()
    }
}