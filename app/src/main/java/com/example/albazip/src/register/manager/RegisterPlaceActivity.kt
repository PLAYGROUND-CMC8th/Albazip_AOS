package com.example.albazip.src.register.manager

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.albazip.R
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityRegisterPlaceBinding
import com.example.albazip.src.register.manager.custom.TypeBottomSheetDialog

class RegisterPlaceActivity :
    BaseActivity<ActivityRegisterPlaceBinding>(ActivityRegisterPlaceBinding::inflate),
    TypeBottomSheetDialog.BottomSheetClickListener {

    var name_flags: Boolean = false
    var place_type_flags: Boolean = false
    var main_place_flags: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            TypeBottomSheetDialog().show(supportFragmentManager, "typePicker")
        }

        // 텍스트 굵기 변화
        chageToBold(binding.etName)
        chageToBold(binding.etLocationMain)
        chageToBold(binding.etLocationSub)

        // 시업자 정보 입력 화면으로 화면전환
        binding.btnNext.setOnClickListener {
            val nextIntent = Intent(this,InputBNumActivity::class.java)
            startActivity(nextIntent)
        }
    }

    override fun onItemSelected(text: String) {
        binding.tvType.text = text
        binding.tvType.setTextColor(Color.parseColor("#343434"))
        binding.tvType.setTypeface(null, Typeface.BOLD)
        place_type_flags = true
        checkIntentState()
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

    // 입력 감지
    // 추후에 테두리 색 변경해주세용 ^^ ~


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

}