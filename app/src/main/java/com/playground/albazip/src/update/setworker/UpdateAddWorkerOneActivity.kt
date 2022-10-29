package com.playground.albazip.src.update.setworker

import android.os.Bundle
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityUpdateAddWorkerOneBinding
import com.playground.albazip.src.mypage.manager.custom.PayUnitBottomSheetDialog
import com.playground.albazip.src.update.setworker.dialog.RestTimeInfoBottomSheetDialog

class UpdateAddWorkerOneActivity :
    BaseActivity<ActivityUpdateAddWorkerOneBinding>(ActivityUpdateAddWorkerOneBinding::inflate), PayUnitBottomSheetDialog.BottomSheetClickListener {

    private var positionState = false
    private var partState = false
    private var workTimeState = false
    private var restTimeState = false
    private var payState = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPositionSelectEvent()
        initPartTimeSelectEvent()
        initRestTimeSelectEvent()

        initRestInfoBottomSheet()

        initPayPicker()
    }

    // 포지션 선택 이벤트
    private fun initPositionSelectEvent() {
        binding.apply {
            btnMonToFri.setOnClickListener {
                if (btnMonToFri.isSelected) {
                    btnMonToFri.isSelected = false
                } else {
                    btnMonToFri.isSelected = true
                    btnSatToSun.isSelected = false
                }
            }

            btnSatToSun.setOnClickListener {
                if (btnSatToSun.isSelected) {
                    btnSatToSun.isSelected = false
                } else {
                    positionState = true
                    btnSatToSun.isSelected = true
                    btnMonToFri.isSelected = false
                }
            }
        }
    }

    // 파트타임 선택 이벤트
    private fun initPartTimeSelectEvent() {
        binding.apply {
            btnOpen.setOnClickListener {
                if (btnOpen.isSelected) {
                    btnOpen.isSelected = false
                } else {
                    btnOpen.isSelected = true

                    btnMiddle.isSelected = false
                    btnClose.isSelected = false
                }
            }

            btnMiddle.setOnClickListener {
                if (btnMiddle.isSelected) {
                    btnMiddle.isSelected = false
                } else {
                    btnMiddle.isSelected = true

                    btnOpen.isSelected = false
                    btnClose.isSelected = false
                }
            }

            btnClose.setOnClickListener {
                if (btnClose.isSelected) {
                    btnClose.isSelected = false
                } else {
                    btnClose.isSelected = true

                    btnOpen.isSelected = false
                    btnMiddle.isSelected = false
                }
            }
        }
    }

    // 쉬는 시간 선택 이벤트
    private fun initRestTimeSelectEvent() {
        binding.apply {
            // 없음
            btnNoRest.setOnClickListener {
                if (btnNoRest.isSelected) {
                    btnNoRest.isSelected = false
                } else {
                    btnNoRest.isSelected = true

                    btn30Min.isSelected = false
                    btn60Min.isSelected = false
                    btn90Min.isSelected = false
                }
            }

            // 30분
            btn30Min.setOnClickListener {
                if (btn30Min.isSelected) {
                    btn30Min.isSelected = false
                } else {
                    btn30Min.isSelected = true

                    btnNoRest.isSelected = false
                    btn60Min.isSelected = false
                    btn90Min.isSelected = false
                }
            }

            // 60 분
            btn60Min.setOnClickListener {
                if (btn60Min.isSelected) {
                    btn60Min.isSelected = false
                } else {
                    btn60Min.isSelected = true

                    btnNoRest.isSelected = false
                    btn30Min.isSelected = false
                    btn90Min.isSelected = false
                }
            }

            // 90 분
            btn90Min.setOnClickListener {
                if (btn90Min.isSelected) {
                    btn90Min.isSelected = false
                } else {
                    btn90Min.isSelected = true

                    btnNoRest.isSelected = false
                    btn30Min.isSelected = false
                    btn60Min.isSelected = false
                }
            }
        }
    }

    // 쉬는 시간 안내 바텀 시트
    private fun initRestInfoBottomSheet() {
        binding.ivRestTimeInfo.setOnClickListener {
            RestTimeInfoBottomSheetDialog().show(supportFragmentManager, "REST_TIME_INFO")
        }
    }

    // 페이 선택 바텀 시트 이벤트
    private fun initPayPicker() {
        // 급여 종류 picker
        binding.rlPayOne.setOnClickListener {
            PayUnitBottomSheetDialog(binding.tvSelectedPayUnit.text.toString()).show(supportFragmentManager, "unitPicker")
        }
    }

    // 페이 선택 텍스트 받기
    override fun onItemSelected(text: String) {
        binding.tvSelectedPayUnit.text = text
    }

}