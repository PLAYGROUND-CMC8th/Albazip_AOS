package com.playground.albazip.src.update.runtime

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.playground.albazip.R
import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityInputPlaceMoreBetaBinding
import com.playground.albazip.src.main.ManagerMainActivity
import com.playground.albazip.src.onboard.manager.ManagerOnBoardingActivity
import com.playground.albazip.src.register.manager.custom.PayDayBottomSheetDialog
import com.playground.albazip.src.update.runtime.data.RunningTimeData
import com.playground.albazip.src.update.runtime.network.OpenSchedule
import com.playground.albazip.src.update.runtime.network.RegisterService
import com.playground.albazip.src.update.runtime.network.RequestMSignUp
import com.playground.albazip.util.enqueueUtil

class InputPlaceMoreBetaActivity :
    BaseActivity<ActivityInputPlaceMoreBetaBinding>(ActivityInputPlaceMoreBetaBinding::inflate),
    PayDayBottomSheetDialog.BottomSheetClickListener {

    // 영업시간 입력완료 플래그
    private var runningTimeFlag: Boolean = false

    // 급여 플래그
    private var payDayFlag: Boolean = false

    // 공휴일 정보 플래그
    private var restDayInfoFlag: Boolean = false

    // 스케쥴 리스트
    var openScheduleList = arrayListOf<RunningTimeData>()

    // rv 리스트
    var rvList = arrayListOf<RunningTimeData>()

    private val startActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                runningTimeFlag = it.data!!.getBooleanExtra("runningTimeFlag", false)

                rvList = it.data!!.getSerializableExtra("adapterList") as ArrayList<RunningTimeData>

                openScheduleList =
                    it.data!!.getSerializableExtra("openScheduleList") as ArrayList<RunningTimeData>
                setVisibility()
            }
        }

    private fun setVisibility() {
        if (runningTimeFlag) {
            binding.apply {
                tvInputPlaceInputDone.visibility = View.VISIBLE
                ivInputPlaceCheck.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                tvInputPlaceInputDone.visibility = View.INVISIBLE
                ivInputPlaceCheck.visibility = View.INVISIBLE
            }
        }

        activateCheck()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initRunningTimeBtn()
        moveToOnBoardingPage()

        initPayEvent()

        checkRestDayEvent()

        initBackBtn()
    }

    // 뒤로가기 버튼 이벤트
    private fun initBackBtn() {
        binding.ivInputPlaceMoreBackBtn.setOnClickListener {
            finish()
        }
    }

    // 서버통신 후 온보딩 이벤트로 이동
    private fun moveToOnBoardingPage() {
        binding.btnNext.setOnClickListener {
            //val registerDataList: ArrayList<String> =
            //    intent.getSerializableExtra("registerDataList") as ArrayList<String>
            val registerDataList:ArrayList<String> = intent.getSerializableExtra("registerDataList") as ArrayList<String>

            val holidayList = mutableListOf<String>()
            for (i in rvList.indices) {
                if (rvList[i].restState) {
                    holidayList.add(rvList[i].day.toString().substring(0,1))
                }
            }
            if (restDayInfoFlag){
                holidayList.add("공휴일")
            }

            // 서버로 넘겨줄 스케줄 리스트
            val openScheduleListToServer = arrayListOf<OpenSchedule>()
            for (i in openScheduleList.indices) {
                val data = openScheduleList[i]
                openScheduleListToServer.add(i, OpenSchedule(data.day!!.substring(0,1),data.openTime!!.replace(":",""),data.closeTime!!.replace(":","")))
            }


            tryPostMSignUp(
                RequestMSignUp(
                    name = registerDataList[0],
                    type = registerDataList[1],
                    address = registerDataList[2],
                    registerNumber = registerDataList[3],
                    ownerName = registerDataList[4],
                    holiday = holidayList,
                    openSchedule = openScheduleListToServer,
                    payday = binding.tvSelectDay.text.toString()
                )
            )
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

    // 매장영업시간 설정 화면으로 이동
    private fun initRunningTimeBtn() {
        binding.tvInputPlaceSetRunTimeBtn.setOnClickListener {
            val nextIntent = Intent(this, UpdateRunningTimeActivity::class.java)
            nextIntent.putExtra("openScheduleList", openScheduleList)
            nextIntent.putExtra("adapterList", rvList)
            nextIntent.putExtra("runningTimeFlag", runningTimeFlag)
            startActivityForResult.launch(nextIntent)
        }
    }

    // 급여일 선택 이벤트
    private fun initPayEvent() {
        binding.rlInputPlaceMorePayDay.setOnClickListener {
            PayDayBottomSheetDialog().show(supportFragmentManager, "dayPicker")

            removeFocus()

            // 포커스 넣기
            binding.rlInputPlaceMorePayDay.background = ContextCompat.getDrawable(
                this,
                R.drawable.rectagnle_yellow_radius
            )
        }
    }

    // 포커스 제거 함수
    private fun removeFocus() {
        // 급여일
        binding.rlInputPlaceMorePayDay.background = ContextCompat.getDrawable(
            this,
            R.drawable.rectangle_custom_white_radius
        )
    }

    // 급여 바텀 시트 이벤트
    override fun onItemSelected(text: String) {
        binding.tvSelectDay.text = text
        binding.tvSelectDay.textSize = 18F
        binding.tvSelectDay.setTextColor(Color.parseColor("#343434"))
        binding.tvSelectDay.setTypeface(null, Typeface.BOLD)

        // 급여 입력 flag on
        payDayFlag = true
        activateCheck()

        // 포커스 제거
        removeFocus()
    }


    // 버튼 활성화 여부 체크
    private fun activateCheck() {
        binding.btnNext.isEnabled = runningTimeFlag == true && payDayFlag == true
    }

    // 서버 통신 시도
    private fun tryPostMSignUp(requestMSignUp: RequestMSignUp) {
        val registerService: RegisterService =
            ApplicationClass.sRetrofit.create(RegisterService::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        val call = registerService.postSignUpManager(token, requestMSignUp)
        call.enqueueUtil(
            getResultCode = { it.code },
            onSuccess200 = {
                Log.d("juya",it.message.toString())

                showCustomToast("관리자 가입 완료")

                ApplicationClass.prefs.setInt("mBoardingFlags",0)
                val mBoardingFlags = ApplicationClass.prefs.getInt("mBoardingFlags",0)
                ApplicationClass.prefs.setString(ApplicationClass.X_ACCESS_TOKEN, it.data.token)

                // 저장된 Flag값이 0이면 온보딩
                if(mBoardingFlags == 0){
                    ApplicationClass.prefs.setInt("jobFlags",1)
                    val nextIntent = Intent(this, ManagerOnBoardingActivity::class.java)
                    startActivity(nextIntent)
                    finishAffinity()

                }else{ // 저장된 Flag 1이면 관리자 홈으로 바로 이동
                    ApplicationClass.prefs.setInt("jobFlags",1)
                    val nextIntent = Intent(this, ManagerMainActivity::class.java)
                    startActivity(nextIntent)
                    finishAffinity()
                }

            },

            onError200 = {
                Log.d("juya",it.message.toString())

                if(it.message ==  "필수 정보가 부족합니다."){
                    showCustomToast("필수 정보가 부족합니다.")
                }
                else if (it.message == "이미 존재하는 매장입니다.") {
                    showCustomToast("이미 존재하는 매장입니다.")
                }
            },

            onError400 = {
                Log.d("juya",it.message.toString())
            }
        )
    }

}