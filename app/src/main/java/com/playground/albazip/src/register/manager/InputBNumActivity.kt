package com.playground.albazip.src.register.manager

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import com.playground.albazip.R
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityBnumBinding
import com.playground.albazip.src.register.manager.data.remote.BNameResponse
import com.playground.albazip.src.register.manager.data.remote.BNumResponse
import com.playground.albazip.src.register.manager.network.BNameFragmentView
import com.playground.albazip.src.register.manager.network.BNameService
import com.playground.albazip.src.register.manager.network.BNumFragmentView
import com.playground.albazip.src.register.manager.network.BNumService

class InputBNumActivity : BaseActivity<ActivityBnumBinding>(ActivityBnumBinding::inflate),BNumFragmentView,BNameFragmentView {

    // 사업자 정보 인증 단계
    var certifyState = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 텍스트 굵기 변경
        changeTxtType(binding.etInputBnum)
        changeTxtType(binding.etInputName)

        // 포커스 여부 반환(배경 색 변경)
        onFocus()

        //  사업자 번호 자동으로 "-" 붙이기
        var _beforeLength: Int = 0
        var _afterLength: Int = 0

        // 휴대폰 번호 입력 (자동 띄어씌기)
        binding.etInputBnum.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                _beforeLength = s!!.length
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                _afterLength = s!!.length

                // 삭제중
                if (_beforeLength > _afterLength) {
                    // 삭제 중에 마지막에 -는 자동으로 지우기
                    if (s.toString().endsWith(" ")) {
                        binding.etInputBnum.setText(s.toString().substring(0, s.length - 1))
                    }
                }

                // 입력중
                else if (_beforeLength < _afterLength) {
                    if (_afterLength == 5 && s.toString().indexOf("-") < 0) {
                        binding.etInputBnum.setText(
                            s.toString().substring(0, 3) + "-" + s.toString().substring(3, s.length)
                        )
                    } else if (_afterLength == 8) {
                        binding.etInputBnum.setText(
                            s.toString().substring(0, 6) + "-" + s.toString().substring(6, s.length)
                        )
                    } else if (_afterLength == 14) {
                        binding.etInputBnum.setText(
                            s.toString().substring(0, 12) + "-" + s.toString()
                                .substring(12, s.length)
                        )
                    }
                }
                binding.etInputBnum.setSelection(binding.etInputBnum.length())

                // 휴대폰 번호 입력완료시 전송 버튼 활성화
                if (s.length == 12) { // 활성화
                    binding.btnNext.isEnabled = true
                    binding.btnNext.background =
                        ContextCompat.getDrawable(this@InputBNumActivity, R.drawable.btn_main_yellow_fill_rounded)
                    binding.btnNext.setTextColor(Color.parseColor("#343434"))
                    binding.ivInputCheck.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@InputBNumActivity,
                            R.drawable.ic_checked_correct
                        )
                    )
                } else { // 비활성화
                    binding.btnNext.isEnabled = false
                    binding.btnNext.background =
                        ContextCompat.getDrawable(this@InputBNumActivity, R.drawable.btn_disable_yellow_fill_rounded)
                    binding.btnNext.setTextColor(Color.parseColor("#adadad"))
                    binding.ivInputCheck.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@InputBNumActivity,
                            R.drawable.ic_checked_normal
                        )
                    )
                }

            }

            override fun afterTextChanged(s: Editable?) {}

        })

        // 텍스트 감지
        binding.etInputName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s?.length!! >= 1){
                    binding.ivInputCheckName.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@InputBNumActivity,
                            R.drawable.ic_checked_correct
                        )
                    )


                    // 버튼 활성화
                    binding.btnNext.isEnabled = true
                    binding.btnNext.background =
                        ContextCompat.getDrawable(this@InputBNumActivity, R.drawable.btn_main_yellow_fill_rounded)

                }else{
                    binding.ivInputCheckName.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@InputBNumActivity,
                            R.drawable.ic_checked_normal
                        )
                    )

                    // 버튼 비활성화
                    binding.btnNext.isEnabled = false
                    binding.btnNext.background =
                        ContextCompat.getDrawable(this@InputBNumActivity, R.drawable.btn_disable_yellow_fill_rounded)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        // 다음 버튼 클릭
        binding.btnNext.setOnClickListener {

            // 인증 번호 존재하는 지 여부 먼저 판단 (1단계)
            if(certifyState == 1) {

                showLoadingDialog(this)

                BNumService(this).tryGetBNum(binding.etInputBnum.text.toString())
            }

            // 인증 번호와 성함이 일치하는지 여부 판단 (2단계)
            if(certifyState == 2){

                showLoadingDialog(this)

                //showCustomToast(binding.etInputBnum.text.toString() + binding.etInputName.text.toString())
                BNameService(this).tryGetBName(binding.etInputBnum.text.toString(),binding.etInputName.text.toString())
            }
        }
    }

    override fun onGetBNumSuccess(response: BNumResponse) {
        dismissLoadingDialog()
        if(response.message == "사업자등번호가 존재합니다.") {

            // 일치여부 영역 보이기
            binding.clNameInput.visibility = View.VISIBLE

            // 경고 텍스트 지우기
            binding.warningTv.visibility = View.INVISIBLE

            // 배경 기본으로 재변경(오류 났을 때 없애기 위함)
            binding.rlBnumInput.background = ContextCompat.getDrawable(
                this@InputBNumActivity,
                R.drawable.rectangle_custom_white_radius
            )

            // 클릭 못하게 막기
            binding.etInputBnum.isEnabled = false

            // 인증 단계 설정
            certifyState = 2

            // 버튼 클릭 막기
            binding.btnNext.isEnabled = false

        }else{

            // 경고 배경으로 바꾸기
            binding.rlBnumInput.background = ContextCompat.getDrawable(
                this@InputBNumActivity,
                R.drawable.rectagnle_red_radius
            )

            // 경고 텍스트 띄우기
            binding.warningTv.visibility = View.VISIBLE

            // 사업자 이름 체크박스 해제
            binding.ivInputCheckName.setImageDrawable(
                ContextCompat.getDrawable(
                    this@InputBNumActivity,
                    R.drawable.ic_checked_normal
                )
            )
        }

    }

    override fun onGetBNumFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast("실패")
    }

    // focus 감지
    fun onFocus(){
        binding.etInputBnum.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.rlBnumInput.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectagnle_yellow_radius
                ) else {
                binding.rlBnumInput.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectangle_custom_white_radius
                )
            }
        }

        binding.etInputName.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.rlNameInput.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectagnle_yellow_radius
                ) else {
                binding.rlNameInput.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.rectangle_custom_white_radius
                )
            }
        }
    }

    override fun onGetBNameSuccess(response: BNameResponse) {
        dismissLoadingDialog()
        if(response.message =="사업자등번호가 인증되었습니다."){

            val registerDataList:ArrayList<String> = intent.getSerializableExtra("registerDataList") as ArrayList<String>

            //  사업자 번호
            registerDataList.add(binding.etInputBnum.text.toString())

            // 사업자 성함
            registerDataList.add(binding.etInputName.text.toString())

            // 체크 성공
            binding.ivInputCheckName.setImageDrawable(
                ContextCompat.getDrawable(
                    this@InputBNumActivity,
                    R.drawable.ic_checked_correct
                )
            )

            // 화면이동
            val nextIntent = Intent(this, InputPlaceMoreActivity::class.java)
            nextIntent.putExtra("registerDataList",registerDataList)
            startActivity(nextIntent)
        }else{
            binding.warningNameTv.visibility = View.VISIBLE
            binding.rlNameInput.background = ContextCompat.getDrawable(
                this,
                R.drawable.rectagnle_red_radius
            )
            // 체크 경고
            binding.ivInputCheckName.setImageDrawable(
                ContextCompat.getDrawable(
                    this@InputBNumActivity,
                    R.drawable.ic_checked_normal
                )
            )
        }
    }

    override fun onGetBNameFailure(message: String) {
        showCustomToast("통신오류!")
    }
}