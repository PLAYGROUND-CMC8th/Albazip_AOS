package com.playground.albazip.src.update.runtime

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.playground.albazip.R
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityInputPlaceMoreBetaBinding
import com.playground.albazip.src.register.manager.custom.PayDayBottomSheetDialog

class InputPlaceMoreBetaActivity :
    BaseActivity<ActivityInputPlaceMoreBetaBinding>(ActivityInputPlaceMoreBetaBinding::inflate),
    PayDayBottomSheetDialog.BottomSheetClickListener {

    // 급여 플래그
    private var payDayFlag: Boolean = false

    // 공휴일 정보 플래그
    private var restDayInfoFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initRunningTimeBtn()
        moveToOnBoardingPage()

        initPayEvent()

        checkRestDayEvent()
    }

    // 온보딩 화면으로 이동
    private fun moveToOnBoardingPage() {
        binding.btnNext.setOnClickListener {


        }
    }

    // 공휴일 여부 체크 - 이것은 버튼 활성화 여부에 영향을 미치지 않는다.
    private fun checkRestDayEvent() {
        binding.cbInputPlaceRestDay.setOnClickListener {
            if (it.isSelected) {
                it.isSelected = false
                restDayInfoFlag = false
            } else {
                it.isSelected = true
                restDayInfoFlag = true
            }
        }
    }

    // 매장영업시간 설정 화면으로 이동
    private fun initRunningTimeBtn() {
        binding.tvInputPlaceSetRunTimeBtn.setOnClickListener {
            startActivity(Intent(this, UpdateRunningTimeActivity::class.java))
        }
    }

    // 급여일 선택 이벤트
    private fun initPayEvent() {
        binding.rlInputPlaceMorePayDay.setOnClickListener {
            PayDayBottomSheetDialog().show(supportFragmentManager, "dayPicker")

            removeFocus()

            // 포커스 넣기
            binding.rlInputPlaceMorePayDay.background = ContextCompat.getDrawable(
                this,
                R.drawable.rectagnle_yellow_radius
            )
        }
    }

    // 포커스 제거 함수
    private fun removeFocus() {
        // 급여일
        binding.rlInputPlaceMorePayDay.background = ContextCompat.getDrawable(
            this,
            R.drawable.rectangle_custom_white_radius
        )
    }

    // 급여 바텀 시트 이벤트
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


    // 버튼 활성화 여부 체크
    private fun activateCheck() {

    }

}