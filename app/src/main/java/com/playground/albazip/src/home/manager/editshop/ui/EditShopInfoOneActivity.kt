package com.playground.albazip.src.home.manager.editshop.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.playground.albazip.R
import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityEditShopInfoOneBinding
import com.playground.albazip.src.home.manager.editshop.network.GetEditShopInfoFragmentView
import com.playground.albazip.src.home.manager.editshop.network.GetEditShopInfoResponse
import com.playground.albazip.src.home.manager.editshop.network.GetEditShopInfoService
import com.playground.albazip.src.home.manager.service.MHomeService
import com.playground.albazip.src.register.manager.custom.TypeBottomSheetDialog
import com.playground.albazip.src.update.runtime.network.RegisterService
import com.playground.albazip.util.enqueueUtil

class EditShopInfoOneActivity :
    BaseActivity<ActivityEditShopInfoOneBinding>(ActivityEditShopInfoOneBinding::inflate),
    TypeBottomSheetDialog.BottomSheetClickListener {

    var name_flags: Boolean = true
    var place_type_flags: Boolean = true
    var main_place_flags: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 포커스 감지 함수
        onFocus()

        val positionId = intent.getIntExtra("positionId", 0)
        tryGetEditShopInfo(positionId)

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
            val registerDataList: ArrayList<String> = arrayListOf(placeName, placeType, placeAdress)

            val nextIntent = Intent(this, EditShopInfoTwoActivity::class.java)

            nextIntent.putExtra("registerDataList", registerDataList)
            nextIntent.putExtra("positionId", positionId)

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

    private fun tryGetEditShopInfo(positionId: Int) {
        val mHomeService: MHomeService =
            ApplicationClass.sRetrofit.create(MHomeService::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        val call = mHomeService.getEditShopInfo(token, 63)
        call.enqueueUtil(
            getResultCode = { it.code },
            onSuccess200 = {
                binding.etName.setText(it.data.name) // 매장명
                binding.tvType.text = it.data.type // 업종선택
                binding.etLocationMain.setText(it.data.address) // 주소
            },

            onError400 = {
                showCustomToast(it.message.toString())
            }
        )
    }
}