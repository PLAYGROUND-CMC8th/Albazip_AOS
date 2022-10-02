package com.playground.albazip.src.register.manager.place

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.playground.albazip.R
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityRegisterPlaceBinding
import com.playground.albazip.src.register.manager.InputBNumActivity
import com.playground.albazip.src.register.manager.custom.TypeBottomSheetDialog

class RegisterPlaceActivity :
    BaseActivity<ActivityRegisterPlaceBinding>(ActivityRegisterPlaceBinding::inflate),
    TypeBottomSheetDialog.BottomSheetClickListener {

    var name_flags: Boolean = false
    var place_type_flags: Boolean = false
    var main_place_flags: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 포커스 감지 함수
        onFocus()

        // 받아오는 데이터 값이 존재하면 -> 값 미리 넣어주기
        if (intent.hasExtra("name")) {
            binding.etName.setText(intent.getStringExtra("name")) // 매장명
            binding.etLocationMain.setText(intent.getStringExtra("adress")) // 매장위치

            //글씨 굵기 설정
            binding.etName.setTypeface(null, Typeface.BOLD)
            binding.etLocationMain.setTypeface(null, Typeface.BOLD)

            // 입력 flags 참 설정
            name_flags = true
            main_place_flags =true

        }

        binding.rlPlaceType.setOnClickListener {
            TypeBottomSheetDialog(binding.tvType.text.toString()).show(supportFragmentManager, "typePicker")

            // 모든 포커스 제거
            window.decorView.clearFocus()

            // 포커스 감지
            binding.rlPlaceType.background = ContextCompat.getDrawable(
                this,
                R.drawable.rectagnle_yellow_radius
            )
        }

        // 텍스트 굵기 변화
        chageToBold(binding.etName)
        chageToBold(binding.etLocationMain)
        chageToBold(binding.etLocationSub)

        // 시업자 정보 입력 화면으로 화면전환
        binding.btnNext.setOnClickListener {

            // 매장명
            val placeName = binding.etName.text.toString()
            // 업종명
            val placeType = binding.tvType.text.toString()
            // 주소
            val placeAdress = binding.etLocationMain.text.toString() + " " + binding.etLocationSub.text.toString()

            // 매장 장소 정보 넘기기
            val registerDataList :ArrayList<String> = arrayListOf(placeName,placeType,placeAdress)

            val nextIntent = Intent(this, InputBNumActivity::class.java)

            nextIntent.putExtra("registerDataList",registerDataList)

            startActivity(nextIntent)
        }
    }

    override fun onItemSelected(text: String) {
        binding.tvType.text = text
        binding.tvType.setTextColor(Color.parseColor("#343434"))
        binding.tvType.setTypeface(null, Typeface.BOLD)
        place_type_flags = true
        checkIntentState()

        // 배경 변경
        binding.rlPlaceType.background = ContextCompat.getDrawable(
            this,
            R.drawable.rectangle_custom_white_radius
        )
    }

    // 글씨 굵기 설정
    fun chageToBold(v: EditText) {
        v.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! >= 1) {
                    v.setTypeface(null, Typeface.BOLD)

                    when (v) {
                        binding.etName -> {
                            name_flags = true
                        }

                        binding.etLocationMain -> {
                            main_place_flags = true
                        }

                    }

                    checkIntentState()

                } else {
                    v.setTypeface(null, Typeface.NORMAL)

                    when (v) {
                        binding.etName -> {
                            name_flags = false
                        }

                        binding.etLocationMain -> {
                            main_place_flags = false
                        }

                    }

                    checkIntentState()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    private fun checkIntentState() {
        if ((name_flags && main_place_flags && place_type_flags)) {
            activateBtn()
        } else {
            deActivateBtn()
        }
    }

    // 버튼 활성화 함수
    private fun activateBtn() {
        binding.btnNext.isEnabled = true
        binding.btnNext.background =
            ContextCompat.getDrawable(this, R.drawable.btn_main_yellow_fill_rounded)
        binding.btnNext.setTextColor(Color.parseColor("#343434"))
    }

    // 버튼 비활성화 함수
    private fun deActivateBtn() {
        binding.btnNext.isEnabled = false
        binding.btnNext.background =
            ContextCompat.getDrawable(this, R.drawable.btn_disable_yellow_fill_rounded)
        binding.btnNext.setTextColor(Color.parseColor("#adadad"))
    }

    // focus 감지
    fun onFocus(){

        // 매장명 입력감지
        binding.etName.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.rlPlaceName.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectagnle_yellow_radius
                ) else {
                binding.rlPlaceName.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectangle_custom_white_radius
                )
            }
        }

        // 매장명 (main) 입력감지
        binding.etLocationMain.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.rlLocationMain.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectagnle_yellow_radius
                ) else {
                binding.rlLocationMain.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectangle_custom_white_radius
                )
            }
        }

        // 매장명 (sub) 입력감지
        binding.etLocationSub.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.rlLocationSub.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectagnle_yellow_radius
                ) else {
                binding.rlLocationSub.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectangle_custom_white_radius
                )
            }
        }
    }

}