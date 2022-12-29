package com.playground.albazip.src.update.runtime

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityRunningTimeBinding
import com.playground.albazip.src.update.runtime.adater.RunningTimeAdapter
import com.playground.albazip.src.update.runtime.custom.AllTimeBottomSheetDialog
import com.playground.albazip.src.update.runtime.custom.Confirm24HourBottomSheetDialog
import com.playground.albazip.src.update.runtime.custom.RunningTimeCancelBottomSheetDialog
import com.playground.albazip.src.update.runtime.custom.RunningTimePickerBottomSheetDialog
import com.playground.albazip.src.update.runtime.data.RunningTimeData
import com.playground.albazip.util.GetTimeDiffUtil

class UpdateRunningTimeActivity :
    BaseActivity<ActivityRunningTimeBinding>(ActivityRunningTimeBinding::inflate),
    RunningTimePickerBottomSheetDialog.BottomSheetClickListener,
    AllTimeBottomSheetDialog.BottomSheetClickListener {

    private lateinit var runningTimeAdapter: RunningTimeAdapter

    var selectedPosition = -1
    var hasDataChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAdapter()
        initBlockView()
        // initAllCheckBtnEvent()

        initBackBtn()
        initDoneBtnEvent()

        getIntentRv()
        isAllDataSame()

        checkingDataChanged()
    }

    // 모든 시간 동일한지 UI 체크해주기
    private fun isAllDataSame() {
        binding.cbRunningTimeCheckbox.isSelected = runningTimeAdapter.isAllDataSame()
    }

    // 이미 들어간 데이터가 있다면 다음과 같이 설정
    private fun getIntentRv() {
        if (intent.getBooleanExtra("runningTimeFlag", false)) {
            runningTimeAdapter = RunningTimeAdapter(
                { setAllSameBtnOff() },
                { setDoneBtnVisibilityOn() },
                { setDoneBtnVisibilityOff() }, true
            )

            runningTimeAdapter.runningTimeItemList =
                intent.getSerializableExtra("adapterList") as ArrayList<RunningTimeData>

            binding.rvRunningTimeDays.itemAnimator = null
            binding.rvRunningTimeDays.adapter = runningTimeAdapter
            setRvItemClickEvent()
        }
    }

    private fun checkingDataChanged() {
        runningTimeAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                hasDataChanged = true
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                hasDataChanged = true
            }
        })
    }

    private fun initBackBtn() {
        binding.ivRunningTimeBackBtn.setOnClickListener {
            if (!hasDataChanged) { // 이전 데이터와 바뀐 데이터가 없으면
                finish() // 그냥 종료
            } else {
                RunningTimeCancelBottomSheetDialog().show(supportFragmentManager, "BACK_BTN")
            }
        }
    }

    private fun initAdapter() {
        runningTimeAdapter = RunningTimeAdapter(
            { setAllSameBtnOff() },
            { setDoneBtnVisibilityOn() },
            { setDoneBtnVisibilityOff() }, false
        )
        runningTimeAdapter.runningTimeItemList.addAll(
            listOf(
                RunningTimeData("월요일"),
                RunningTimeData("화요일"),
                RunningTimeData("수요일"),
                RunningTimeData("목요일"),
                RunningTimeData("금요일"),
                RunningTimeData("토요일"),
                RunningTimeData("일요일"),
            )
        )
        binding.rvRunningTimeDays.itemAnimator = null
        binding.rvRunningTimeDays.adapter = runningTimeAdapter

        setRvItemClickEvent()
    }

    private fun setRvItemClickEvent() {
        runningTimeAdapter.setItemClickListener(object : RunningTimeAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int, tag: String) {

                selectedPosition = position // 어떤 아이템이 선택되었는지 포시션 받기

                val openTime = runningTimeAdapter.runningTimeItemList[position].openTime?.split(":")
                val openHour = openTime?.get(0)!!.toInt()
                val openMin = openTime[1].toInt()

                val closeTime =
                    runningTimeAdapter.runningTimeItemList[position].closeTime?.split(":")
                val closeHour = closeTime?.get(0)!!.toInt()
                val closeMin = closeTime[1].toInt()

                when (tag) {
                    "SET_OPEN_HOUR" -> RunningTimePickerBottomSheetDialog(
                        0,
                        openHour,
                        openMin
                    ).show(
                        supportFragmentManager,
                        "SET_OPEN_HOUR"
                    )
                    "SET_CLOSE_HOUR" -> RunningTimePickerBottomSheetDialog(
                        1,
                        closeHour,
                        closeMin
                    ).show(
                        supportFragmentManager,
                        "SET_CLOSE_HOUR"
                    )
                }

            }
        })
    }

    // 오픈. 마감 시간 설정
    override fun onTimeSelected(h: String, m: String, flag: Int) {

        val displayTime = GetTimeDiffUtil().getDisplayTime(h, m)

        if (flag == 0) { // 오픈시간 설정
            runningTimeAdapter.setItemTimeUI(0, selectedPosition, displayTime)

            // 마감시간이 활성화 되어 있을 때 -> 24시간 여부 체크
            if (runningTimeAdapter.runningTimeItemList[selectedPosition].closeFlag) {
                areYou24Hour(0)
            }

        } else { // 마감시간 설정
            runningTimeAdapter.setItemTimeUI(1, selectedPosition, displayTime)

            // 오픈시간이 활성화 되어 있을 때 -> 24시간 여부 체크
            if (runningTimeAdapter.runningTimeItemList[selectedPosition].openFlag) {
                areYou24Hour(1)
            }
        }
    }

    // 시간이 같을 때 24시간 설정 여부 묻기
    private fun areYou24Hour(flags: Int) {
        // 시간이 같을 때 24시간 여부 묻기
        if (runningTimeAdapter.runningTimeItemList[selectedPosition].totalTime == "0시간") {
            if (flags == 0) {
                val openTime =
                    runningTimeAdapter.runningTimeItemList[selectedPosition].openTime?.split(":")
                Confirm24HourBottomSheetDialog(
                    { set24Hour() },
                    { showOpenDialogAgain(openTime!!) }).show(
                    supportFragmentManager,
                    "ARE_YOU_24"
                )
            } else {
                val closeTime =
                    runningTimeAdapter.runningTimeItemList[selectedPosition].closeTime?.split(":")
                Confirm24HourBottomSheetDialog(
                    { set24Hour() },
                    { showCloseDialogAgain(closeTime!!) }).show(
                    supportFragmentManager,
                    "ARE_YOU_24"
                )
            }
        } else {
            runningTimeAdapter.runningTimeItemList[selectedPosition].time24State = false
            runningTimeAdapter.runningTimeItemList[selectedPosition].restState = false
        }
    }

    // 24시간 확인 여부를 묻는 다이얼로그 대응 함수
    private fun showOpenDialogAgain(openTime: List<String>) {
        val openHour = openTime[0].toInt()
        val openMin = openTime[1].toInt()
        RunningTimePickerBottomSheetDialog(0, openHour, openMin).show(
            supportFragmentManager,
            "SET_OPEN_HOUR"
        )
    }

    private fun showCloseDialogAgain(closeTime: List<String>) {
        val closeHour = closeTime[0].toInt()
        val closeMin = closeTime[1].toInt()
        RunningTimePickerBottomSheetDialog(1, closeHour, closeMin).show(
            supportFragmentManager,
            "SET_CLOSE_HOUR"
        )
    }

    // 총시간 24 시간으로 설정하기
    private fun set24Hour() {
        runningTimeAdapter.init24HourUI(selectedPosition)
    }


    // 일괄 적용 클릭 이벤트
    private fun initBlockView() {
        binding.clRunningTimeSame.setOnClickListener {
            AllTimeBottomSheetDialog().show(supportFragmentManager, "SET_ALL_TIME")
        }
    }

    // 일괄 적용 바텀시트 이벤트
    override fun onTimeAllTimeSelected(oTime: String, eTime: String, totalTime: String) {
        // binding.viewRunningCheck.visibility = View.GONE
        binding.cbRunningTimeCheckbox.isSelected = true

        initAdapter()

        if (totalTime == "24시간") {
            runningTimeAdapter.setAll24Hour(oTime, eTime, totalTime)
        } else {
            runningTimeAdapter.setAllTIme(oTime, eTime, totalTime)
        }
    }


    // 모든 영업시간 버튼 이벤트
    /*private fun initAllCheckBtnEvent() {
        binding.cbRunningTimeCheckbox.setOnClickListener {
            if (binding.viewRunningCheck.visibility == View.GONE) { // 없어진 상태라면
                binding.viewRunningCheck.visibility = View.VISIBLE
            } else {
                binding.viewRunningCheck.visibility = View.GONE
            }
        }
    }*/

    // 동일 시간 명시 버튼 끄기
    private fun setAllSameBtnOff() {
        binding.cbRunningTimeCheckbox.isSelected = false
        //binding.viewRunningCheck.visibility = View.VISIBLE
    }

    // 완료 버튼 활성화
    private fun setDoneBtnVisibilityOn() {
        binding.tvRunningTimeDone.isEnabled = true
    }

    // 완료 버튼 비활성화
    private fun setDoneBtnVisibilityOff() {
        binding.tvRunningTimeDone.isEnabled = false
    }

    // 완료 버튼 이벤트
    private fun initDoneBtnEvent() {
        binding.tvRunningTimeDone.setOnClickListener {
            val returnIntent = Intent()

            val openScheduleList =
                runningTimeAdapter.runningTimeItemList.filter { it.restState == false }
            val adapterList = runningTimeAdapter.runningTimeItemList

            returnIntent.putExtra(
                "openScheduleList",
                openScheduleList as ArrayList<RunningTimeData>
            )
            returnIntent.putExtra(
                "adapterList",
                adapterList as ArrayList<RunningTimeData>
            )
            returnIntent.putExtra("runningTimeFlag", true)
            setResult(RESULT_OK, returnIntent)
            finish()
        }
    }

}