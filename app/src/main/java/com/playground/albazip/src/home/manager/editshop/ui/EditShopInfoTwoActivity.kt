package com.playground.albazip.src.home.manager.editshop.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.playground.albazip.R
import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityEditShopInfoTwoBinding
import com.playground.albazip.src.home.manager.service.MHomeService
import com.playground.albazip.src.register.manager.custom.PayDayBottomSheetDialog
import com.playground.albazip.src.update.runtime.UpdateRunningTimeActivity
import com.playground.albazip.src.update.runtime.data.RunningTimeData
import com.playground.albazip.util.enqueueUtil

class EditShopInfoTwoActivity :
    BaseActivity<ActivityEditShopInfoTwoBinding>(ActivityEditShopInfoTwoBinding::inflate),
    PayDayBottomSheetDialog.BottomSheetClickListener {

    private var payDayFlag: Boolean = false // 급여
    private var restDayInfoFlag: Boolean = false // 공휴일

    // 영업시간 입력완료 플래그
    private var runningTimeFlag: Boolean = true

    // 스케쥴 리스트
    var openScheduleList = arrayListOf<RunningTimeData>()

    // rv 리스트
    var rvList = arrayListOf<RunningTimeData>()

    // 휴일 배열(for.서버통신)
    var holidayList: MutableList<String> = arrayListOf()

    private val startActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                runningTimeFlag = it.data!!.getBooleanExtra("runningTimeFlag", false)

                rvList = it.data!!.getSerializableExtra("adapterList") as ArrayList<RunningTimeData>

                openScheduleList =
                    it.data!!.getSerializableExtra("openScheduleList") as ArrayList<RunningTimeData>
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tryGetEditShopInfo(63)

        initBackEvent()
        getPrevInfo()
        initDoneBtnEvent()

        initRunningTimeBtn()
        checkRestDayEvent()
        setPayBtn()
    }

    // 기존 정보 받아오기
    private fun getPrevInfo() {
        val registerDataList = intent.getSerializableExtra("registerDataList") as ArrayList<String>
        val positionId = intent.getIntExtra("positionId", 0)
        //  가게이름 registerDataList[0]
        // 타입 registerDataList[1]
        // 주소 registerDataList[2]
    }

    private fun initBackEvent() {
        binding.ibtnBack.setOnClickListener {
            finish()
        }
    }

    private fun initDoneBtnEvent() {
        binding.tvNext.setOnClickListener {
            Log.d("kite", openScheduleList.toString())
        }
    }

    private fun initRunningTimeBtn() {
        binding.tvInputPlaceSetRunTimeBtn.setOnClickListener {
            val nextIntent = Intent(this, UpdateRunningTimeActivity::class.java)
            nextIntent.putExtra("openScheduleList", openScheduleList)
            nextIntent.putExtra("adapterList", rvList)
            nextIntent.putExtra("runningTimeFlag", runningTimeFlag)
            startActivityForResult.launch(nextIntent)
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

    private fun setPayBtn() {
        binding.rlInputPlaceMorePayDay.setOnClickListener {
            PayDayBottomSheetDialog().show(supportFragmentManager, "dayPicker")

            // 포커스 넣기
            binding.rlInputPlaceMorePayDay.background = ContextCompat.getDrawable(
                this,
                R.drawable.rectagnle_yellow_radius
            )
        }
    }

    // 포커스 제거 함수
    private fun removeFocus() {
        binding.rlInputPlaceMorePayDay.background = ContextCompat.getDrawable(
            this,
            R.drawable.rectangle_custom_white_radius
        ) // 급여일
    }

    override fun onItemSelected(text: String) {
        // 급여 입력 flag on
        payDayFlag = true
        // 포커스 제거
        removeFocus()
    }

    private fun tryGetEditShopInfo(positionId: Int) {
        val mHomeService: MHomeService =
            ApplicationClass.sRetrofit.create(MHomeService::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        val call = mHomeService.getEditShopInfo(token, positionId)
        call.enqueueUtil(
            getResultCode = { it.code },
            onSuccess200 = {
                holidayList = it.data.holiday // 공휴일 설정

                for (index in it.data.openSchedule.indices) {
                    val data = it.data.openSchedule[index]

                    openScheduleList.add(
                        RunningTimeData(
                            openTime = data.startTime,
                            closeTime = data.endTime,
                            day = data.day,

                            openFlag = true,
                            closeFlag = true
                        )
                    )

                }

                for (data in holidayList) { // 24시간 버튼 설정
                    for (index in openScheduleList.indices) {
                        if (openScheduleList[index].day != data
                            && openScheduleList[index].openTime == "0000"
                            && openScheduleList[index].closeTime == "0000"
                        ) {
                            openScheduleList[index].time24State = true
                        } else {
                            openScheduleList[index].restState = true
                        }
                    }
                }

                binding.tvSelectDay.text = it.data.payday  // 급여일

                binding.cbInputPlaceRestDay.isSelected = it.data.holiday.contains("공휴일") // 공휴일 버튼 세팅


                // runningTimeRv 세팅
                setRunningTimeRv()
            },

            onError400 = {
                showCustomToast(it.message.toString())
            }
        )
    }

    private fun setRunningTimeRv() {
        rvList = openScheduleList

        rvList.forEach {
            it.openTime = it.openTime?.substring(0,2) + ":" + it.openTime?.substring(2,4)
            it.closeTime = it.closeTime?.substring(0,2) + ":" + it.closeTime?.substring(2,4)
        }

    }
}