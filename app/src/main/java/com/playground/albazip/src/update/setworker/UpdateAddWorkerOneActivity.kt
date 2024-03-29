package com.playground.albazip.src.update.setworker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.playground.albazip.R
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityUpdateAddWorkerOneBinding
import com.playground.albazip.src.mypage.manager.custom.PayUnitBottomSheetDialog
import com.playground.albazip.src.mypage.manager.workerlist.ui.AddWorkerTwoActivity
import com.playground.albazip.src.update.setworker.data.WorkerTimeData
import com.playground.albazip.src.update.setworker.dialog.RestTimeInfoBottomSheetDialog
import com.playground.albazip.src.update.setworker.network.RequestAddPosition
import java.text.DecimalFormat

class UpdateAddWorkerOneActivity :
    BaseActivity<ActivityUpdateAddWorkerOneBinding>(ActivityUpdateAddWorkerOneBinding::inflate),
    PayUnitBottomSheetDialog.BottomSheetClickListener {

    private var positionState = false
    private var partState = false
    private var restTimeState = false
    private var payState = true

    var workingTimeFlag = false


    var rank = "알바생"
    var title = ""
    var breakTime = ""
    var salary = ""
    var salaryType = ""
    var workSchedule = arrayListOf<RequestAddPosition.WorkSchedule>()

    var rvList = arrayListOf<WorkerTimeData>()

    private val startActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                workingTimeFlag = it.data!!.getBooleanExtra("workingTimeFlag", false)

                rvList = it.data!!.getSerializableExtra("adapterList") as ArrayList<WorkerTimeData>

                if (workingTimeFlag) {
                    binding.llWorkingDone.visibility = View.VISIBLE
                } else {
                    binding.llWorkingDone.visibility = View.GONE
                }

                checkBtnState()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPositionSelectEvent()
        initPartTimeSelectEvent()
        initRestTimeSelectEvent()

        initRestInfoBottomSheet()

        initPayPicker()
        initPayEvent()

        initSelectWorkingTimeBtn()

        initNextBtn()
        initBackBtn()
    }

    // 다음 버튼
    private fun initNextBtn() {
        binding.tvNext.setOnClickListener {
            val workerStringList: ArrayList<String> =
                arrayListOf("알바생", title, breakTime, salary, salaryType)

            // 근무자 추가 두 번째 화면으로 이동
            val nextIntent = Intent(this, AddWorkerTwoActivity::class.java)
            nextIntent.putExtra("workerStringList", workerStringList)
            nextIntent.putExtra("workSchedule",workSchedule as ArrayList<RequestAddPosition.WorkSchedule>)

            // 입력정보 넘겨주기
            startActivity(nextIntent)
            finish()
        }
    }

    // 뒤로가기 버튼
    private fun initBackBtn() {
        binding.btnBack.setOnClickListener {
            finish()
        }
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

                checkBtnState()
            }

            btnSatToSun.setOnClickListener {
                if (btnSatToSun.isSelected) {
                    btnSatToSun.isSelected = false
                } else {
                    btnSatToSun.isSelected = true
                    btnMonToFri.isSelected = false
                }

                checkBtnState()
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

                checkBtnState()
            }

            btnMiddle.setOnClickListener {
                if (btnMiddle.isSelected) {
                    btnMiddle.isSelected = false
                } else {
                    btnMiddle.isSelected = true

                    btnOpen.isSelected = false
                    btnClose.isSelected = false
                }

                checkBtnState()
            }

            btnClose.setOnClickListener {
                if (btnClose.isSelected) {
                    btnClose.isSelected = false
                } else {
                    btnClose.isSelected = true

                    btnOpen.isSelected = false
                    btnMiddle.isSelected = false
                }

                checkBtnState()
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

                checkBtnState()
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

                checkBtnState()
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

                checkBtnState()
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

                checkBtnState()
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
            PayUnitBottomSheetDialog(binding.tvSelectedPayUnit.text.toString()).show(
                supportFragmentManager,
                "unitPicker"
            )

            // 포커스 넣기
            binding.rlPayOne.background = ContextCompat.getDrawable(
                this@UpdateAddWorkerOneActivity,
                R.drawable.rectangle_fill_white_radius_yellow_15
            )
        }
    }

    // 페이 선택 텍스트 받기
    override fun onItemSelected(text: String) {
        binding.tvSelectedPayUnit.text = text

        binding.rlPayOne.background = ContextCompat.getDrawable(
            this,
            R.drawable.rectangle_fill_white_radius_gray_15
        )
    }

    // 근무일 선택하기
    private fun initSelectWorkingTimeBtn() {
        binding.btnSelectWorkDay.setOnClickListener {
            val nextIntent = Intent(this, UpdateSetWorkerTimeActivity::class.java)
            nextIntent.putExtra("adapterList", rvList)
            nextIntent.putExtra("workingTimeFlag", workingTimeFlag)
            startActivityForResult.launch(nextIntent)
        }
    }

    // 급여 (,) 붙이기
    private fun initPayEvent() {
        var result = ""
        val decimalFormat: DecimalFormat = DecimalFormat("#,###")
        binding.etPayment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result)) {
                    result = decimalFormat.format((s.toString().replace(",", "").toDouble()))
                    binding.etPayment.setText(result)
                    binding.etPayment.setSelection(result.length)
                }

                if (s?.toString()?.isNotEmpty() == true) {
                    payState = true
                    checkBtnState()
                } else {
                    payState = false
                    checkBtnState()
                }

                // 포커스 넣기
                binding.rlPayTwo.background = ContextCompat.getDrawable(
                    this@UpdateAddWorkerOneActivity,
                    R.drawable.rectangle_fill_white_radius_yellow_15
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

    }


    private fun checkFlags() {
        val positionBtnList = mutableListOf(binding.btnMonToFri, binding.btnSatToSun)
        val partBtnList = mutableListOf(binding.btnOpen, binding.btnMiddle, binding.btnClose)
        val restTimeBtnList =
            mutableListOf(binding.btnNoRest, binding.btn30Min, binding.btn60Min, binding.btn90Min)

        positionState = positionBtnList.any { it.isSelected }
        partState = partBtnList.any { it.isSelected }
        restTimeState = restTimeBtnList.any { it.isSelected }
    }

    // 다음 액티비티에 넘겨줄 데이터들 받아놓기
    private fun getIntentData() {
        val positionBtnList = mutableListOf(binding.btnMonToFri, binding.btnSatToSun)
        val partBtnList = mutableListOf(binding.btnOpen, binding.btnMiddle, binding.btnClose)
        val restTimeBtnList =
            mutableListOf(binding.btnNoRest, binding.btn30Min, binding.btn60Min, binding.btn90Min)

        title = positionBtnList.filter { it.isSelected }[0].text
            .toString() + partBtnList.filter { it.isSelected }[0].text.toString()
        breakTime = restTimeBtnList.filter { it.isSelected }[0].text.toString()
        salary = binding.etPayment.text.toString()
        salaryType = binding.tvSelectedPayUnit.text.toString()

        val selectedRv = rvList.filter { it.isSelected == true }

        for (i in selectedRv.indices) {
            val data = selectedRv[i]
            workSchedule.add(
                RequestAddPosition.WorkSchedule(
                    data.workDay.substring(0, 1),
                    data.openTime!!.replace(":",""),
                    data.closeTime!!.replace(":","")
                )
            )
        }
    }

    private fun checkBtnState() {

        binding.rlPayTwo.background = ContextCompat.getDrawable(
            this@UpdateAddWorkerOneActivity,
            R.drawable.rectangle_fill_white_radius_gray_15
        )

        checkFlags()

        if (positionState && partState && restTimeState && payState && workingTimeFlag) {
            binding.tvNext.isEnabled = true
            getIntentData()
        } else {
            binding.tvNext.isEnabled = false
        }
    }

}