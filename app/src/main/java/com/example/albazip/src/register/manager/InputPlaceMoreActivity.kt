package com.example.albazip.src.register.manager

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.set
import com.example.albazip.R
import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.ApplicationClass.Companion.prefs
import com.example.albazip.config.BaseActivity
import com.example.albazip.config.BaseResponse
import com.example.albazip.databinding.ActivityInputPlaceMoreBinding
import com.example.albazip.src.main.ManagerMainActivity
import com.example.albazip.src.register.common.custom.AgeBottomSheetDialog
import com.example.albazip.src.register.common.data.remote.PositionRegisterResponse
import com.example.albazip.src.register.common.data.remote.PostSignUpRequest
import com.example.albazip.src.register.common.network.SignUpService
import com.example.albazip.src.register.manager.custom.PayDayBottomSheetDialog
import com.example.albazip.src.register.manager.custom.TimePickerBottomSheetDialog
import com.example.albazip.src.register.manager.data.remote.PostMSignUpRequest
import com.example.albazip.src.register.manager.network.MSignUpFragmentView
import com.example.albazip.src.register.manager.network.MSignUpService
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class InputPlaceMoreActivity :
    BaseActivity<ActivityInputPlaceMoreBinding>(ActivityInputPlaceMoreBinding::inflate),
    View.OnClickListener, PayDayBottomSheetDialog.BottomSheetClickListener,
    TimePickerBottomSheetDialog.BottomSheetClickListener, MSignUpFragmentView {

    // 시간 차 계산을 위한 데이터 포맷 선언
    val f: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREA)

    // 버튼 활성화 flag
    var restDayFlag = false // 매장 휴무일
    var openTimeFlag = false // 오픈 시간
    var endTimeFlag = false // 마감시간
    var payDayFlag = false // 급여일

    // 휴일 배열(for.서버통신)
    var holidayList: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // 관리자 화면 or 온보딩으로 이동
        binding.btnNext.setOnClickListener {

            // 휴일
            whichHolidayIsSelected()
            val holiday = holidayList
            // 오픈시간
            val startTime = binding.tvInputStartTime.text.toString().replace(":", "")
            // 마감시간
            val endTime = binding.tvInputEndTime.text.toString().replace(":", "")
            // 급여일
            val payday = binding.tvSelectDay.text.toString()


            val registerDataList: ArrayList<String> =
                intent.getSerializableExtra("registerDataList") as ArrayList<String>


            val postRequest = PostMSignUpRequest(
                name = registerDataList[0],
                type = registerDataList[1],
                address = registerDataList[2],
                registerNumber = registerDataList[3],
                ownerName = registerDataList[4],
                holiday = holiday,
                startTime = startTime,
                endTime = endTime,
                payday = payday
            )

            showLoadingDialog(this)

            MSignUpService(this).tryPostMSignUp(postRequest)

        }

        // 휴일 선택 버튼
        binding.btnNoClosed.setOnClickListener(this) // 연중무휴
        binding.btnMon.setOnClickListener(this) // 월
        binding.btnTue.setOnClickListener(this) // 화
        binding.btnWen.setOnClickListener(this) // 수
        binding.btnThur.setOnClickListener(this) // 목
        binding.btnFri.setOnClickListener(this) // 금
        binding.btnSat.setOnClickListener(this) // 토
        binding.btnSun.setOnClickListener(this) // 일
        binding.btnHoliday.setOnClickListener(this) // 공휴일


        // 오픈시간 선택
        binding.tvInputStartTime.setOnClickListener {
            TimePickerBottomSheetDialog("매장 오픈시간", 0).show(supportFragmentManager, "openpicker")

            removeFocus()

            // 포커스 설정
            binding.rlStartTime.background = ContextCompat.getDrawable(
                this,
                R.drawable.rectangle_fill_white_radius_yellow_15
            )
        }

        // 마감시간 선택
        binding.tvInputEndTime.setOnClickListener {
            TimePickerBottomSheetDialog("매장 마감시간", 1).show(supportFragmentManager, "closepicker")

            removeFocus()

            // 포커스 설정
            binding.rlEndTime.background = ContextCompat.getDrawable(
                this,
                R.drawable.rectangle_fill_white_radius_yellow_15
            )
        }


        // 급여일 선택
        binding.rlPayDay.setOnClickListener {
            PayDayBottomSheetDialog().show(supportFragmentManager, "dayPicker")

            removeFocus()

            // 포커스 넣기
            binding.rlPayDay.background = ContextCompat.getDrawable(
                this,
                R.drawable.rectagnle_yellow_radius
            )
        }

    }

    fun whichHolidayIsSelected() {
        val btnList: ArrayList<AppCompatButton> = arrayListOf(
            binding.btnNoClosed, binding.btnMon, binding.btnTue, binding.btnWen,
            binding.btnThur, binding.btnFri, binding.btnSat, binding.btnSun, binding.btnHoliday
        )

        for (i in 0..8)
            if (btnList[i].isSelected == true) {
                holidayList.add(btnList[i].text.toString())
            }
    }

    fun isAllDeActive(): Boolean {
        binding.apply {
            if (btnMon.isSelected == false && btnTue.isSelected == false && btnWen.isSelected == false && btnThur.isSelected == false && btnFri.isSelected == false
                && btnSat.isSelected == false && btnSun.isSelected == false && btnHoliday.isSelected == false
            ) {
                return true
            }
            return false
        }
    }

    fun weekSelectEvent(v: AppCompatButton) {

        val btnList: ArrayList<AppCompatButton> = arrayListOf(
            binding.btnNoClosed, binding.btnMon, binding.btnTue, binding.btnWen,
            binding.btnThur, binding.btnFri, binding.btnSat, binding.btnSun, binding.btnHoliday
        )

        // 요일이 선택되면
        if (v.isSelected == false) {
            v.isSelected = true
            v.setTextColor(Color.parseColor("#343434"))
            v.background =
                ContextCompat.getDrawable(this@InputPlaceMoreActivity, R.drawable.oval_fill_yellow)


            binding.btnNoClosed.isSelected = false  // 연중무휴 선택해제
            binding.btnNoClosed.setTextColor(Color.parseColor("#6f6f6f"))
            binding.btnNoClosed.background = ContextCompat.getDrawable(
                this@InputPlaceMoreActivity,
                R.drawable.rectangle_fill_gray_radius_16
            )   // 연중무휴 회색으로

        } else {
            v.isSelected = false
            v.setTextColor(Color.parseColor("#6f6f6f"))
            v.background = ContextCompat.getDrawable(
                this@InputPlaceMoreActivity,
                R.drawable.oval_fill_white_stroke_gray
            )
            // 만약 월~공휴일이 전부 선택 안됐을 때 -> 전부 선택해제
            if (isAllDeActive() == true) {
                for (i in 1..7) {   // 연중무휴가 해제되면
                    // 월~ 공휴일 다 흰색으로
                    btnList[i].background = ContextCompat.getDrawable(
                        this@InputPlaceMoreActivity,
                        R.drawable.oval_fill_white_stroke_gray
                    ) // 배경 비활성화
                    btnList[i].setTextColor(Color.parseColor("#6f6f6f"))
                }
                btnList[8].background = ContextCompat.getDrawable(
                    this@InputPlaceMoreActivity,
                    R.drawable.rectangle_fill_white_radius_gray_16
                )
                btnList[8].setTextColor(Color.parseColor("#6f6f6f"))
            } else {     // 아니면? -> 해당 요일만 선택해제
                v.background = ContextCompat.getDrawable(
                    this@InputPlaceMoreActivity,
                    R.drawable.oval_fill_white_stroke_gray
                )
                v.setTextColor(Color.parseColor("#6f6f6f"))
            }
        }
    }


    override fun onClick(v: View?) {

        val btnList: ArrayList<AppCompatButton> = arrayListOf(
            binding.btnNoClosed, binding.btnMon, binding.btnTue, binding.btnWen,
            binding.btnThur, binding.btnFri, binding.btnSat, binding.btnSun, binding.btnHoliday
        )


        binding.apply {
            when (v) {
                btnNoClosed -> {
                    // 연중무휴가 선택되면
                    if (v.isSelected == false) {
                        v.isSelected = true
                        (v as AppCompatButton).setTextColor(Color.parseColor("#343434"))
                        v.background = ContextCompat.getDrawable(
                            this@InputPlaceMoreActivity,
                            R.drawable.rectangle_fill_yellow_radius_16
                        )

                        for (i in 1..7) {
                            btnList[i].isSelected = false   // 월~공휴일 선택 전부 해제
                            btnList[i].background = ContextCompat.getDrawable(
                                this@InputPlaceMoreActivity,
                                R.drawable.oval_fill_custom_white
                            ) // 배경 비활성화
                            btnList[i].setTextColor(Color.parseColor("#6f6f6f"))
                        }
                        btnList[8].isSelected = false
                        btnList[8].setTextColor(Color.parseColor("#6f6f6f"))
                        btnList[8].background = ContextCompat.getDrawable(
                            this@InputPlaceMoreActivity,
                            R.drawable.rectangle_fill_gray_radius_16
                        )
                    } else {
                        v.isSelected = false
                        (v as AppCompatButton).setTextColor(Color.parseColor("#6f6f6f"))
                        v.background = ContextCompat.getDrawable(
                            this@InputPlaceMoreActivity,
                            R.drawable.rectangle_fill_white_radius_gray_16
                        )

                        // 만약 모든 버튼이 비활성화 되어 있다면
                        if (isAllDeActive() == true) {
                            for (i in 1..7) {   // 연중무휴가 해제되면
                                // 월~ 공휴일 다 흰색으로
                                btnList[i].background = ContextCompat.getDrawable(
                                    this@InputPlaceMoreActivity,
                                    R.drawable.oval_fill_white_stroke_gray
                                ) // 배경 비활성화
                                btnList[i].setTextColor(Color.parseColor("#6f6f6f"))
                            }
                            btnList[8].background = ContextCompat.getDrawable(
                                this@InputPlaceMoreActivity,
                                R.drawable.rectangle_fill_white_radius_gray_16
                            )
                            btnList[8].setTextColor(Color.parseColor("#6f6f6f"))
                        }
                    }

                }

                btnMon -> {
                    weekSelectEvent(v as AppCompatButton)
                }
                btnTue -> {
                    weekSelectEvent(v as AppCompatButton)
                }
                btnWen -> {
                    weekSelectEvent(v as AppCompatButton)
                }
                btnThur -> {
                    weekSelectEvent(v as AppCompatButton)
                }
                btnFri -> {
                    weekSelectEvent(v as AppCompatButton)
                }
                btnSat -> {
                    weekSelectEvent(v as AppCompatButton)
                }
                btnSun -> {
                    weekSelectEvent(v as AppCompatButton)
                }
                btnHoliday -> {
                    // 요일이 선택되면
                    if (v.isSelected == false) {
                        v.isSelected = true
                        (v as AppCompatButton).setTextColor(Color.parseColor("#343434"))
                        v.background = ContextCompat.getDrawable(
                            this@InputPlaceMoreActivity,
                            R.drawable.rectangle_fill_yellow_radius_16
                        )
                        btnNoClosed.isSelected = false  // 연중무휴 선택해제
                        btnNoClosed.setTextColor(Color.parseColor("#6f6f6f"))
                        btnNoClosed.background = ContextCompat.getDrawable(
                            this@InputPlaceMoreActivity,
                            R.drawable.rectangle_fill_gray_radius_16
                        )   // 연중무휴 회색으로
                    } else {
                        v.isSelected = false
                        (v as AppCompatButton).setTextColor(Color.parseColor("#6f6f6f"))
                        v.background = ContextCompat.getDrawable(
                            this@InputPlaceMoreActivity,
                            R.drawable.rectangle_fill_white_radius_gray_16
                        )
                        // 만약 월~공휴일이 전부 선택 안됐을 때 -> 전부 선택해제
                        if (isAllDeActive() == true) {
                            for (i in 1..7) {   // 연중무휴가 해제되면
                                // 월~ 공휴일 다 흰색으로
                                btnList[i].background = ContextCompat.getDrawable(
                                    this@InputPlaceMoreActivity,
                                    R.drawable.oval_fill_white_stroke_gray
                                ) // 배경 비활성화
                                btnList[i].setTextColor(Color.parseColor("#6f6f6f"))
                            }
                            btnList[8].background = ContextCompat.getDrawable(
                                this@InputPlaceMoreActivity,
                                R.drawable.rectangle_fill_white_radius_gray_16
                            )
                            btnList[8].setTextColor(Color.parseColor("#6f6f6f"))
                        } else {     // 아니면? -> 해당 요일만 선택해제
                            v.background = ContextCompat.getDrawable(
                                this@InputPlaceMoreActivity,
                                R.drawable.oval_fill_white_stroke_gray
                            )
                            (v as AppCompatButton).setTextColor(Color.parseColor("#6f6f6f"))

                        }
                    }
                }

            }
        }
    }


    override fun onItemSelected(text: String) {
        binding.tvSelectDay.text = text
        binding.tvSelectDay.textSize = 18F
        binding.tvSelectDay.setTextColor(Color.parseColor("#343434"))
        binding.tvSelectDay.setTypeface(null, Typeface.BOLD)

        // 급여 입력 flag on
        payDayFlag = true
        activateCheck()

        // 포커스 제거
        removeFocus()
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
            removeFocus()
            binding.tvInputStartTime.setTextColor(Color.parseColor("#343434"))

            // 오픈 플래그 on
            openTimeFlag = true
            activateCheck()

            // 운영시간 계산
            if (binding.tvInputEndTime.currentTextColor == Color.parseColor("#343434")) { // 마감 시간이 활성화 되어 있으면 시간차를 계산해준다.
                getTimeLag()
            }


        } else { // 마감 값 넣기
            binding.tvInputEndTime.setText(displayHour + ":" + displayMinute)
            // 포커스 해제
            removeFocus()
            binding.tvInputEndTime.setTextColor(Color.parseColor("#343434"))

            // 마감 플래그 on
            endTimeFlag = true
            activateCheck()

            // 운영시간 계산
            if (binding.tvInputStartTime.currentTextColor == Color.parseColor("#343434")) { // 오픈 시간이 활성화 되어 있으면 시간차를 계산해준다.
                getTimeLag()
            }

        }

    }


    // 시간차 설정해주는 함수
    private fun getTimeLag() {
        var sDate = f.parse(binding.tvInputStartTime.text.toString() + ":00")
        var eDate = f.parse(binding.tvInputEndTime.text.toString() + ":00")
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

    // 포커스 제거 함수
    private fun removeFocus() {
        // 오픈 시간
        binding.rlStartTime.background = ContextCompat.getDrawable(
            this,
            R.drawable.rectangle_fill_white_radius_gray_15
        )
        // 마감 시간
        binding.rlEndTime.background = ContextCompat.getDrawable(
            this,
            R.drawable.rectangle_fill_white_radius_gray_15
        )
        // 급여일
        binding.rlPayDay.background = ContextCompat.getDrawable(
            this,
            R.drawable.rectangle_custom_white_radius
        )
    }

    // 활성화 여부 체크
    fun activateCheck() {
        // 추후에 restDayFlag 체크도 넣어주기
        if (payDayFlag == true && openTimeFlag == true && endTimeFlag == true) { // 활성화
            binding.btnNext.isEnabled = true
            binding.btnNext.background = ContextCompat.getDrawable(
                this,
                R.drawable.btn_main_yellow_fill_rounded
            )
            binding.btnNext.setTextColor(Color.parseColor("#343434"))
        } else { // 비활성화
            binding.btnNext.isEnabled = false
            binding.btnNext.background = ContextCompat.getDrawable(
                this,
                R.drawable.btn_disable_yellow_fill_rounded
            )
            binding.btnNext.setTextColor(Color.parseColor("#ADADAD"))
        }
    }

    // 서버통신 성공
    override fun onPostMSignUpSuccess(response: PositionRegisterResponse) {
        dismissLoadingDialog()

        if (response.code == 200) {
            showCustomToast("관리자 가입 완료")

            val mBoardingFlags = prefs.getInt("mBoardingFlags",0)
            ApplicationClass.prefs.setString(ApplicationClass.X_ACCESS_TOKEN, response.tokenData.token)

            // 저장된 Flag값이 0이면 온보딩
            if(mBoardingFlags == 0){
                prefs.setInt("jobFlags",1)
                val nextIntent = Intent(this,ManagerOnBoardingActivity::class.java)
                startActivity(nextIntent)
                finishAffinity()

            }else{ // 저장된 Flag 1이면 관리자 홈으로 바로 이동
                prefs.setInt("jobFlags",1)
                val nextIntent = Intent(this,ManagerMainActivity::class.java)
                startActivity(nextIntent)
                finishAffinity()
            }

        }else if(response.code == 202){
            if(response.message ==  "필수 정보가 부족합니다."){
                showCustomToast("필수 정보가 부족합니다.")
            }
            else if (response.message == "이미 존재하는 매장입니다.") {
                showCustomToast("이미 존재하는 매장입니다.")
            }
        }else{
            showCustomToast(response.message.toString())
        }

    }

    // 서버통신 실패
    override fun onPostMSignUpFailure(message: String) {
        dismissLoadingDialog()
        Log.d("ohnoerror", message)
    }

}