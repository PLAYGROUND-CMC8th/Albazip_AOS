package com.example.albazip.src.home.manager.editshop.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.albazip.R
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityEditShopInfoOneBinding
import com.example.albazip.src.home.manager.editshop.network.GetEditShopInfoFragmentView
import com.example.albazip.src.home.manager.editshop.network.GetEditShopInfoResponse
import com.example.albazip.src.home.manager.editshop.network.GetEditShopInfoService
import com.example.albazip.src.register.manager.InputBNumActivity
import com.example.albazip.src.register.manager.custom.TypeBottomSheetDialog

class EditShopInfoOneActivity :
    BaseActivity<ActivityEditShopInfoOneBinding>(ActivityEditShopInfoOneBinding::inflate),
    TypeBottomSheetDialog.BottomSheetClickListener,GetEditShopInfoFragmentView {

    var name_flags: Boolean = true
    var place_type_flags: Boolean = true
    var main_place_flags: Boolean = true

    /// 서버 통신에서 받아올 데이터
    // 휴무일
    var holiday = ArrayList<String>()
    // 시작시간
    var startTime =""
    // 종료시간
    var endTime = ""
    // 월급날
    var payDay = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 포커스 감지 함수
        onFocus()

        val positionId = intent.getIntExtra("positionId",0)
        GetEditShopInfoService(this).tryGetEditShopInfo(positionId)
        showLoadingDialog(this)

        binding.ibtnBack.setOnClickListener {
            finish()
        }

        binding.rlPlaceType.setOnClickListener {
            TypeBottomSheetDialog(binding.tvType.text.toString()).show(
                supportFragmentManager,
                "typePicker"
            )

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
        binding.tvDone.setOnClickListener {

            // 매장명
            val placeName = binding.etName.text.toString()
            // 업종명
            val placeType = binding.tvType.text.toString()
            // 주소
            val placeAdress =
                binding.etLocationMain.text.toString() + " " + binding.etLocationSub.text.toString()

            // 매장 정보 넘기기
            val registerDataList: ArrayList<String> = arrayListOf(placeName, placeType, placeAdress,startTime,endTime,payDay)

            val nextIntent = Intent(this, EditShopInfoTwoActivity::class.java)

            nextIntent.putExtra("registerDataList", registerDataList)
            nextIntent.putExtra("holiday",holiday)
            nextIntent.putExtra("positionId",positionId)

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
        binding.tvDone.isEnabled = true
        binding.tvDone.setTextColor(Color.parseColor("#ffc400"))
    }

    // 버튼 비활성화 함수
    private fun deActivateBtn() {
        binding.tvDone.isEnabled = false
        binding.tvDone.setTextColor(Color.parseColor("#adadad"))
    }

    // focus 감지
    fun onFocus() {

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

    // 매장 정보 조회 성공
    override fun onGetEditShopInfoSuccess(response: GetEditShopInfoResponse) {
        dismissLoadingDialog()

        // 매장명
        binding.etName.setText(response.data.name)

        // 업종선택
        binding.tvType.text = response.data.type

        // 주소
        binding.etLocationMain.setText(response.data.address)

        // 휴무일
        holiday = response.data.holiday
        // 시작시간
        startTime = response.data.startTime
        // 마감시간
        endTime = response.data.endTime
        // 월급날
        payDay = response.data.payday
    }

    // 매장 정보 조회 실패
    override fun onGetEditShopInfoFailure(message: String) {
        dismissLoadingDialog()
    }
}