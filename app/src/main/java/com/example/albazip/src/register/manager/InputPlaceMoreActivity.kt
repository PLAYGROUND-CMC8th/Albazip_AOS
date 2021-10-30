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
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.set
import com.example.albazip.R
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityInputPlaceMoreBinding
import com.example.albazip.src.register.common.custom.AgeBottomSheetDialog
import com.example.albazip.src.register.manager.custom.PayDayBottomSheetDialog
import com.example.albazip.src.register.manager.custom.TimePickerBottomSheetDialog
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class InputPlaceMoreActivity :
    BaseActivity<ActivityInputPlaceMoreBinding>(ActivityInputPlaceMoreBinding::inflate),
    View.OnClickListener, PayDayBottomSheetDialog.BottomSheetClickListener,
    TimePickerBottomSheetDialog.BottomSheetClickListener {

    // 시간 차 계산을 위한 데이터 포맷 선언
    val f: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 온보딩 화면으로 이동
        binding.btnNext.setOnClickListener {
            val nextIntent = Intent()
            startActivity(nextIntent)
            finish()
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

    override fun onClick(v: View?) {

        binding.apply {
            when (v) {
                btnNoClosed -> {
                    btnNoClosed.isSelected = true
                    btnNoClosed.setTextColor(Color.parseColor("#343434"))
                    checkDayFalse()
                }

                btnMon -> {
                    btnMon.isSelected = true
                    btnMon.setTextColor(Color.parseColor("#343434"))
                    btnNoClosed.setTextColor(Color.parseColor("#6f6f6f"))

                    btnNoClosed.isSelected = false
                }
                btnTue -> {
                    btnTue.isSelected = true
                    btnTue.setTextColor(Color.parseColor("#343434"))
                    btnNoClosed.setTextColor(Color.parseColor("#6f6f6f"))

                    btnNoClosed.isSelected = false
                }
                btnWen -> {
                    btnWen.isSelected = true
                    btnWen.setTextColor(Color.parseColor("#343434"))
                    btnNoClosed.setTextColor(Color.parseColor("#6f6f6f"))

                    btnNoClosed.isSelected = false
                }
                btnThur -> {
                    btnThur.isSelected = true
                    btnThur.setTextColor(Color.parseColor("#343434"))
                    btnNoClosed.setTextColor(Color.parseColor("#6f6f6f"))

                    btnNoClosed.isSelected = false
                }
                btnFri -> {
                    btnFri.isSelected = true
                    btnFri.setTextColor(Color.parseColor("#343434"))
                    btnNoClosed.setTextColor(Color.parseColor("#6f6f6f"))

                    btnNoClosed.isSelected = false
                }
                btnSat -> {
                    btnSat.isSelected = true
                    btnSat.setTextColor(Color.parseColor("#343434"))
                    btnNoClosed.setTextColor(Color.parseColor("#6f6f6f"))

                    btnNoClosed.isSelected = false
                }
                btnSun -> {
                    btnSun.isSelected = true
                    btnSun.setTextColor(Color.parseColor("#343434"))
                    btnNoClosed.setTextColor(Color.parseColor("#6f6f6f"))

                    btnNoClosed.isSelected = false
                }
                btnHoliday -> {
                    btnHoliday.isSelected = true
                    btnHoliday.setTextColor(Color.parseColor("#343434"))
                    btnNoClosed.setTextColor(Color.parseColor("#6f6f6f"))

                    btnNoClosed.isSelected = false
                }

            }
        }
    }

    fun checkDayFalse() {
        binding.apply {
            btnMon.isSelected = false
            btnTue.isSelected = false
            btnWen.isSelected = false
            btnThur.isSelected = false
            btnFri.isSelected = false
            btnSat.isSelected = false
            btnSun.isSelected = false
            btnHoliday.isSelected = false

            btnMon.setTextColor(Color.parseColor("#6f6f6f"))
            btnTue.setTextColor(Color.parseColor("#6f6f6f"))
            btnWen.setTextColor(Color.parseColor("#6f6f6f"))
            btnThur.setTextColor(Color.parseColor("#6f6f6f"))
            btnFri.setTextColor(Color.parseColor("#6f6f6f"))
            btnSat.setTextColor(Color.parseColor("#6f6f6f"))
            btnSun.setTextColor(Color.parseColor("#6f6f6f"))
            btnHoliday.setTextColor(Color.parseColor("#6f6f6f"))
        }
    }

    override fun onItemSelected(text: String) {
        binding.tvSelectDay.text = text
        binding.tvSelectDay.textSize = 18F
        binding.tvSelectDay.setTextColor(Color.parseColor("#343434"))
        binding.tvSelectDay.setTypeface(null, Typeface.BOLD)
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

            // 운영시간 계산
            if (binding.tvInputEndTime.currentTextColor == Color.parseColor("#343434")) { // 마감 시간이 활성화 되어 있으면 시간차를 계산해준다.
                getTimeLag()
            }


        } else { // 마감 값 넣기
            binding.tvInputEndTime.setText(displayHour + ":" + displayMinute)
            // 포커스 해제
            removeFocus()
            binding.tvInputEndTime.setTextColor(Color.parseColor("#343434"))

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

            if(showMin == 0L){
                binding.tvWorkHour.setText(showHour.toString() + "시간")
            }else{
                binding.tvWorkHour.setText(showHour.toString() + "시간 " + showMin.toString() + "분")
            }


        } else { // 24시간을 더해서 빼준다.

            var sec = diff / 1000 + 86400// 총 시간(초) 받아오기(24시간 더해서)

            var showHour = sec / (60 * 60)
            var showMin = sec / 60 - (showHour * 60)

            if(showMin == 0L){
                binding.tvWorkHour.setText(showHour.toString() + "시간")
            }else{
                binding.tvWorkHour.setText(showHour.toString() + "시간 " + showMin.toString() + "분")
            }
        }

    }

    // 포커스 제거 함수
    private fun removeFocus(){
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

}