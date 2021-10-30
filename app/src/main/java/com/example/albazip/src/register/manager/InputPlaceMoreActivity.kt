package com.example.albazip.src.register.manager

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.set
import com.example.albazip.R
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityInputPlaceMoreBinding
import com.example.albazip.src.register.common.custom.AgeBottomSheetDialog
import com.example.albazip.src.register.manager.custom.PayDayBottomSheetDialog

class InputPlaceMoreActivity :
    BaseActivity<ActivityInputPlaceMoreBinding>(ActivityInputPlaceMoreBinding::inflate),
    View.OnClickListener, PayDayBottomSheetDialog.BottomSheetClickListener {
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


        // 영업시간 입력(시작)
        var _beforeLength: Int = 0
        var _afterLength: Int = 0

        // 영업 시간(시작) 입력 (자동 띄어씌기)
        binding.etStartTime.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                _beforeLength = s!!.length
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                // 올바른 시간 입력 위한 함수(00시간~24시간)
                if (s!!.isNotEmpty()) {

                    if (!(binding.etStartTime.text[0].toString() == "2" || binding.etStartTime.text[0].toString() == "1" || binding.etStartTime.text[0].toString() == "0")) {
                        binding.etStartTime.setText("")
                    }
                }

                if(s!!.length > 1){
                    when(binding.etStartTime.text[1].toString()){
                        "5" -> { binding.etStartTime.setText(binding.etStartTime.text.toString().substring(0,binding.etStartTime.text.length-1))}
                        "6" -> { binding.etStartTime.setText(binding.etStartTime.text.toString().substring(0,binding.etStartTime.text.length-1))}
                        "7" -> { binding.etStartTime.setText(binding.etStartTime.text.toString().substring(0,binding.etStartTime.text.length-1))}
                        "8" -> { binding.etStartTime.setText(binding.etStartTime.text.toString().substring(0,binding.etStartTime.text.length-1))}
                        "9" -> { binding.etStartTime.setText(binding.etStartTime.text.toString().substring(0,binding.etStartTime.text.length-1))}
                    }
                }

                if(s!!.length >2){
                    when(binding.etStartTime.text[2].toString()){
                        "6" -> { binding.etStartTime.setText(binding.etStartTime.text.toString().substring(0,binding.etStartTime.text.length-1))}
                        "7" -> { binding.etStartTime.setText(binding.etStartTime.text.toString().substring(0,binding.etStartTime.text.length-1))}
                        "8" -> { binding.etStartTime.setText(binding.etStartTime.text.toString().substring(0,binding.etStartTime.text.length-1))}
                        "9" -> { binding.etStartTime.setText(binding.etStartTime.text.toString().substring(0,binding.etStartTime.text.length-1))}
                    }
                }

                if(s!!.length > 3 ){
                    when(binding.etStartTime.text[3].toString()){
                        "6" -> { binding.etStartTime.setText(binding.etStartTime.text.toString().substring(0,binding.etStartTime.text.length-1))}
                        "7" -> { binding.etStartTime.setText(binding.etStartTime.text.toString().substring(0,binding.etStartTime.text.length-1))}
                        "8" -> { binding.etStartTime.setText(binding.etStartTime.text.toString().substring(0,binding.etStartTime.text.length-1))}
                        "9" -> { binding.etStartTime.setText(binding.etStartTime.text.toString().substring(0,binding.etStartTime.text.length-1))}
                    }
                }


                // 텍스트 색상 동적 변경
                if (s!!.isEmpty()) {
                    binding.etStartTime.setTextColor(Color.parseColor("#6f6f6f"))
                } else {
                    binding.etStartTime.setTextColor(Color.parseColor("#343434"))
                }

                _afterLength = s.length

                // 삭제중
                if (_beforeLength > _afterLength) {
                    // 삭제 중에 마지막에 -는 자동으로 지우기
                    if (s.toString().endsWith(":")) {
                        binding.etStartTime.setText(s.toString().substring(0, s.length - 1))
                    }
                }

                // 입력중
                else if (_beforeLength < _afterLength) {
                    if (_afterLength == 4 && s.toString().indexOf(" ") < 0) {
                        binding.etStartTime.setText(
                            s.toString().substring(0, 2) + ":" + s.toString().substring(2, s.length)
                        )
                    }
                }
                binding.etStartTime.setSelection(binding.etStartTime.length())

                // 영업시간 입력완료시 전송 버튼 활성화
                if (s.length == 5) { // 활성화
                    //binding.rlClickableCertify.isEnabled = true
                    //binding.tvCertify.setTextColor(Color.parseColor("#343434"))
                } else { // 비활성화
                    //binding.rlClickableCertify.isEnabled = false
                    //binding.tvCertify.setTextColor(Color.parseColor("#cecece"))
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // 영업시간 입력(끝)
        var e_beforeLength: Int = 0
        var e_afterLength: Int = 0


        binding.etEndTime.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                e_beforeLength = s!!.length
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                // 올바른 시간 입력 위한 함수(00시간~24시간)
                if (s!!.isNotEmpty()) {

                    if (!(binding.etEndTime.text[0].toString() == "2" || binding.etEndTime.text[0].toString() == "1" || binding.etEndTime.text[0].toString() == "0")) {
                        binding.etEndTime.setText("")
                    }
                }

                if(s!!.length > 1){
                    when(binding.etEndTime.text[1].toString()){
                        "5" -> { binding.etEndTime.setText(binding.etEndTime.text.toString().substring(0,binding.etEndTime.text.length-1))}
                        "6" -> { binding.etEndTime.setText(binding.etEndTime.text.toString().substring(0,binding.etEndTime.text.length-1))}
                        "7" -> { binding.etEndTime.setText(binding.etEndTime.text.toString().substring(0,binding.etEndTime.text.length-1))}
                        "8" -> { binding.etEndTime.setText(binding.etEndTime.text.toString().substring(0,binding.etEndTime.text.length-1))}
                        "9" -> { binding.etEndTime.setText(binding.etEndTime.text.toString().substring(0,binding.etEndTime.text.length-1))}
                    }
                }

                if(s!!.length >2){
                    when(binding.etEndTime.text[2].toString()){
                        "6" -> { binding.etEndTime.setText(binding.etEndTime.text.toString().substring(0,binding.etEndTime.text.length-1))}
                        "7" -> { binding.etEndTime.setText(binding.etEndTime.text.toString().substring(0,binding.etEndTime.text.length-1))}
                        "8" -> { binding.etEndTime.setText(binding.etEndTime.text.toString().substring(0,binding.etEndTime.text.length-1))}
                        "9" -> { binding.etEndTime.setText(binding.etEndTime.text.toString().substring(0,binding.etEndTime.text.length-1))}
                    }
                }

                if(s!!.length > 3 ){
                    when(binding.etEndTime.text[3].toString()){
                        "6" -> { binding.etEndTime.setText(binding.etEndTime.text.toString().substring(0,binding.etEndTime.text.length-1))}
                        "7" -> { binding.etEndTime.setText(binding.etEndTime.text.toString().substring(0,binding.etEndTime.text.length-1))}
                        "8" -> { binding.etEndTime.setText(binding.etEndTime.text.toString().substring(0,binding.etEndTime.text.length-1))}
                        "9" -> { binding.etEndTime.setText(binding.etEndTime.text.toString().substring(0,binding.etEndTime.text.length-1))}
                    }
                }


                // 텍스트 색상 동적 변경
                if (s!!.isEmpty()) {
                    binding.etEndTime.setTextColor(Color.parseColor("#6f6f6f"))
                } else {
                    binding.etEndTime.setTextColor(Color.parseColor("#343434"))
                }

                e_afterLength = s.length

                // 삭제중
                if (e_beforeLength > e_afterLength) {
                    // 삭제 중에 마지막에 -는 자동으로 지우기
                    if (s.toString().endsWith(":")) {
                        binding.etEndTime.setText(s.toString().substring(0, s.length - 1))
                    }
                }

                // 입력중
                else if (e_beforeLength < e_afterLength) {
                    if (e_afterLength == 4 && s.toString().indexOf(" ") < 0) {
                        binding.etEndTime.setText(
                            s.toString().substring(0, 2) + ":" + s.toString().substring(2, s.length)
                        )
                    }
                }
                binding.etEndTime.setSelection(binding.etEndTime.length())

                // 영업시간 입력완료시 전송 버튼 활성화
                if (s.length == 5) { // 활성화
                    //binding.rlClickableCertify.isEnabled = true
                    //binding.tvCertify.setTextColor(Color.parseColor("#343434"))
                } else { // 비활성화
                    //binding.rlClickableCertify.isEnabled = false
                    //binding.tvCertify.setTextColor(Color.parseColor("#cecece"))
                }

            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // focus 감지
        binding.etStartTime.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.rlStartTime.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectagnle_yellow_radius
                ) else {
                binding.rlStartTime.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectangle_custom_white_radius
                )
            }
        }
        binding.etEndTime.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.rlEndTime.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectagnle_yellow_radius
                ) else {
                binding.rlEndTime.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectangle_custom_white_radius
                )
            }
        }


        // 급여일 선택
        binding.rlPayDay.setOnClickListener {
            PayDayBottomSheetDialog().show(supportFragmentManager, "dayPicker")
            this.currentFocus?.clearFocus() // 이전 작업 포커스 없애기
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
    }
}