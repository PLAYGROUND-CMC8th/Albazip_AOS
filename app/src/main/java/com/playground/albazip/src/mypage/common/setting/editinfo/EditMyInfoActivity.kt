package com.playground.albazip.src.mypage.common.setting.editinfo

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.playground.albazip.R
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.databinding.ActivityEditMyInfoBinding
import com.playground.albazip.src.mypage.common.setting.editinfo.custom.EditAgeBottomSheetDialog
import com.playground.albazip.src.mypage.common.setting.editinfo.data.EditInfoRequest
import com.playground.albazip.src.mypage.common.setting.editinfo.network.EditInfoFragmentView
import com.playground.albazip.src.mypage.common.setting.editinfo.network.EditInfoService
import com.playground.albazip.src.splash.SplashActivity

class EditMyInfoActivity:BaseActivity<ActivityEditMyInfoBinding>(ActivityEditMyInfoBinding::inflate),
    EditAgeBottomSheetDialog.BottomSheetClickListener,EditInfoFragmentView {

    // 성별 받아오기
    var gender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뒤로가기
        binding.ibtnClose.setOnClickListener {
            finish()
        }

        // 이전 클래스에서 정보 받아오기
        val myInfoList = intent.getSerializableExtra("myInfoList") as List<String>
        // 성
        binding.etLastName.setText(myInfoList[0])
        // 이름
        binding.etFirstName.setText(myInfoList[1])
        // 나이
        binding.tvInputAge.text = myInfoList[2]
        // 성별
        if(myInfoList[3] == "남자"){
            binding.rlMan.isSelected = true
        }else{
            binding.rlWoman.isSelected = true
        }

        // 서버통신 시작
        binding.tvDone.setOnClickListener {
             val postLastName = binding.etLastName.text.toString()
            val postFirstName = binding.etFirstName.text.toString()
            val postBirthYear = binding.tvInputAge.text.toString()

            var postSex = "-1"
            if(binding.rlMan.isSelected == true){
                postSex = "0"
            }else{
                postSex = "1"
            }

            val postRequest = EditInfoRequest(
                firstName = postFirstName,
                lastName = postLastName,
                birthyear = postBirthYear,
                gender = postSex
            )

            EditInfoService(this).tryEditInfo(postRequest)
            showLoadingDialog(this)
        }


        // 포커스 여부 감지
        binding.apply {
            etLastName.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus)
                    binding.rlFirstName.background = ContextCompat.getDrawable(
                        this@EditMyInfoActivity,
                        R.drawable.rectagnle_yellow_radius
                    ) else {
                    binding.rlFirstName.background = ContextCompat.getDrawable(
                        this@EditMyInfoActivity,
                        R.drawable.rectangle_custom_white_radius
                    )
                }
            }
            etFirstName.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus)
                    binding.rlName.background = ContextCompat.getDrawable(
                        this@EditMyInfoActivity,
                        R.drawable.rectagnle_yellow_radius
                    ) else {
                    binding.rlName.background = ContextCompat.getDrawable(
                        this@EditMyInfoActivity,
                        R.drawable.rectangle_custom_white_radius
                    )
                }
            }
        }

        // 입력감지(first-name)
        binding.etLastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s?.length!! >= 1){
                    binding.etLastName.setTypeface(null, Typeface.BOLD)
                }else{
                    binding.etLastName.setTypeface(null, Typeface.NORMAL)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 입력감지(last-name)
        binding.etFirstName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //last_name_flags = s?.length!! >= 1
                if(s?.length!! >= 1){
                    binding.etFirstName.setTypeface(null, Typeface.BOLD)
                }else{
                    binding.etFirstName.setTypeface(null, Typeface.NORMAL)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        // 생년월일 picker dialog 생성
        binding.tvInputAge.setOnClickListener {
            EditAgeBottomSheetDialog().show(supportFragmentManager, "agePicker")
        }

        // 성별 선택
        binding.apply {
            // 남자 선택
            rlMan.setOnClickListener {
                rlMan.isSelected = true
                rlWoman.isSelected = false
            }

            // 여자 선택
            rlWoman.setOnClickListener {
                rlWoman.isSelected = true
                rlMan.isSelected = false
            }
        }

    }

    // bottomsheet dialog에서 생년월일 받아오기
    override fun onItemSelected(text: String) {
        binding.tvInputAge.text = text
        binding.tvInputAge.setTextColor(Color.parseColor("#343434"))
        binding.tvInputAge.setTypeface(null,Typeface.BOLD)
    }

    // 개인정보 수정 성공
    override fun onEditInfoPostSuccess(response: BaseResponse) {
       dismissLoadingDialog()

        showCustomToast("기본 정보가 변경되었습니다.")
        val homeIntent = Intent(this,SplashActivity::class.java)
        startActivity(homeIntent)
        finishAffinity()
    }

    override fun onEditInfoPostFailure(message: String) {
       dismissLoadingDialog()
    }
}