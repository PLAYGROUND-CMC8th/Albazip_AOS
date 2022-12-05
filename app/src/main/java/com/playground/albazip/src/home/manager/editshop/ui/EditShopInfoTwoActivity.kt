package com.playground.albazip.src.home.manager.editshop.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.playground.albazip.R
import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityEditShopInfoTwoBinding
import com.playground.albazip.src.home.manager.editshop.data.RequestEditShop
import com.playground.albazip.src.home.manager.service.MHomeService
import com.playground.albazip.src.main.ManagerMainActivity
import com.playground.albazip.src.register.manager.custom.PayDayBottomSheetDialog
import com.playground.albazip.src.update.runtime.UpdateRunningTimeActivity
import com.playground.albazip.src.update.runtime.data.RunningTimeData
import com.playground.albazip.util.GetTimeDiffUtil
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

        val positionId = intent.getIntExtra("positionId", 0)
        tryGetEditShopInfo(positionId)

        initBackEvent()
        initDoneBtnEvent()

        initRunningTimeBtn()
        checkRestDayEvent()
        setPayBtn()
    }

    private fun initBackEvent() {
        binding.ibtnBack.setOnClickListener {
            finish()
        }
    }

    private fun initDoneBtnEvent() {
        binding.tvNext.setOnClickListener {
            val positionId = intent.getIntExtra("positionId", 0)
            val registerDataList =
                intent.getSerializableExtra("registerDataList") as ArrayList<String>

            // 서버로 넘겨줄 스케줄 리스트
            val openScheduleListToServer = arrayListOf<RequestEditShop.OpenSchedule>()
            for (i in openScheduleList.indices) {
                val data = openScheduleList[i]
                openScheduleListToServer.add(
                    i,
                    RequestEditShop.OpenSchedule(
                        data.day!!.substring(0, 1),
                        data.openTime!!.replace(":", ""),
                        data.closeTime!!.replace(":", "")
                    )
                )
            }

            // 공휴일 버튼 체크되어있으면 -> 추가
            if (binding.cbInputPlaceRestDay.isSelected) {
                holidayList.add("공휴일")
            }

            val body = RequestEditShop(
                name = registerDataList[0],
                type = registerDataList[1],
                address = registerDataList[2],
                holiday = holidayList,
                payday = binding.tvSelectDay.text.toString(),
                openSchedule = openScheduleListToServer,
            )
            tryPostEditShopInfo(positionId, body)
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

                openScheduleList.apply {
                    add(RunningTimeData(day = "월요일", openTime = "0000", closeTime = "0000", openFlag = true, closeFlag = true))
                    add(RunningTimeData(day = "화요일", openTime = "0000", closeTime = "0000", openFlag = true, closeFlag = true))
                    add(RunningTimeData(day = "수요일", openTime = "0000", closeTime = "0000", openFlag = true, closeFlag = true))
                    add(RunningTimeData(day = "목요일", openTime = "0000", closeTime = "0000", openFlag = true, closeFlag = true))
                    add(RunningTimeData(day = "금요일", openTime = "0000", closeTime = "0000", openFlag = true, closeFlag = true))
                    add(RunningTimeData(day = "토요일", openTime = "0000", closeTime = "0000", openFlag = true, closeFlag = true))
                    add(RunningTimeData(day = "일요일", openTime = "0000", closeTime = "0000", openFlag = true, closeFlag = true))
                }

                for (index in it.data.openSchedule.indices) {
                    val data = it.data.openSchedule[index]

                    if (openScheduleList[index].day!!.contains(data.day)) {
                        openScheduleList[index].apply {
                            openTime = data.startTime
                            closeTime = data.endTime
                        }
                    }

                    // 24시 설정
                    if (data.startTime == "0000" && data.endTime == "0000") {
                        openScheduleList[index].time24State = true
                        openScheduleList[index].totalTime = "24시간"
                    }
                }

                for (index in openScheduleList.indices) {
                    // 공휴일 설정
                    for (i in holidayList.indices) {
                        if (openScheduleList[index].day!! == holidayList[i] + "요일") {
                            openScheduleList[index].time24State = false
                            openScheduleList[index].restState = true
                        }
                    }
                }

                binding.tvSelectDay.text = it.data.payday  // 급여일

                binding.cbInputPlaceRestDay.isSelected =
                    it.data.holiday.contains("공휴일") // 공휴일 버튼 세팅

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
            it.openTime = it.openTime?.substring(0, 2) + ":" + it.openTime?.substring(2, 4)
            it.closeTime = it.closeTime?.substring(0, 2) + ":" + it.closeTime?.substring(2, 4)
            it.totalTime = GetTimeDiffUtil().getTimeDiffTxt(it.openTime!!, it.closeTime!!)

            if (it.time24State) it.totalTime = "24시간"
        }
    }

    private fun tryPostEditShopInfo(positionId: Int, body: RequestEditShop) {
        val mHomeService: MHomeService =
            ApplicationClass.sRetrofit.create(MHomeService::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        val call = mHomeService.postEditShopInfo(token, positionId, body)
        call.enqueueUtil(
            getResultCode = { it.code },
            onSuccess200 = {
                showCustomToast("매장 정보가 변경되었습니다")
                val nextIntent = Intent(this, ManagerMainActivity::class.java)
                startActivity(nextIntent)
                finishAffinity()
            },
            onError400 = {
                showCustomToast(it.message.toString())
            }
        )
    }
}