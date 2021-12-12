package com.playground.albazip.src.home.worker.opened

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.playground.albazip.R
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.databinding.ChildFragmentHomeWOpenedBinding
import com.playground.albazip.src.home.common.ui.HomeAlarmActivity
import com.playground.albazip.src.home.common.ui.HomeShopListActivity
import com.playground.albazip.src.home.worker.data.AllHomeWResult
import com.playground.albazip.src.home.worker.data.GetAllWHomeResponse
import com.playground.albazip.src.home.worker.network.GetAllWFragmentView
import com.playground.albazip.src.home.worker.network.GetAllWHomeInfoService
import com.playground.albazip.src.home.worker.network.PutQRScanFragmentView
import com.playground.albazip.src.home.worker.network.PutQRScanService
import com.playground.albazip.src.home.worker.opened.worklist.ui.HomeWTodayToDoListActivity
import com.playground.albazip.src.main.WorkerMainActivity
import com.playground.albazip.util.GetCurrentTime
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.ApplicationClass.Companion.prefs
import com.playground.albazip.src.home.worker.closed.worklist.ui.HomeWClosedToDoListActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer

class HomeWOpenedFragment(data: AllHomeWResult) : BaseFragment<ChildFragmentHomeWOpenedBinding>(
    ChildFragmentHomeWOpenedBinding::bind,
    R.layout.child_fragment_home_w_opened
),PutQRScanFragmentView, GetAllWFragmentView {

    var resultData = data
    // 시간 차 계산을 위한 데이터 포맷 선언
    val f: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREA)

    var threadTime = "    "

    val mTimer = timer(initialDelay = 100, period = 100) { // 0.1초후에 0.1초단위로 진행
        (requireContext() as Activity).runOnUiThread {

            GetAllWHomeInfoService(this@HomeWOpenedFragment).tryGetAllWHomeInfo()
            // 남은시간 (from 서버)
            /*var restTime = resultData.scheduleInfo.remainTime

            // 퇴근시간 (from 서버)
            var offTime = resultData.scheduleInfo.endTime?.substring(0,2) + ":" + resultData.scheduleInfo.endTime?.substring(2,4)

            // 현재시간 (from 로컬)
            var currentTime = GetCurrentTime().getTime

            // 시간 단위 변경
            var castOffTime = f.parse(offTime+":00") // 퇴근시간
            var castCurrentTime = f.parse(currentTime+":00") // 현재시간

            // 시간차 구하기
            var diffTime = castOffTime.time - castCurrentTime.time

            var sec = diffTime / 1000 // 총 시간(초) 받아오기

            var showHour = sec / (60 * 60)
            var showMin = sec / 60 - (showHour * 60)

            // 구한시간차 단위 변경
            var getDiffTime = showHour.toString() + ":" + showMin.toString()
            var castGetDiffTime = f.parse(getDiffTime+":00")

            // UI에 출력할 시간차
            var showingTime = castGetDiffTime.toString().substring(11,16)*/

            // 퇴근시간 초과시
           // if(threadTime == "0000"){ // 화면 갱신시간을 대비하여 로드하는 동안 UI 공백처리
           //     binding.tvGoOff.text = ""
           // }

            if(threadTime.contains("-")){
                binding.tvGoOff.setTextColor(Color.parseColor("#f90100")) // 색상변경 - 빨강색

                // 기본 상태는 0, 알림을 확인하면 1로 변경
                var goOffPopFlags = prefs.getInt("goOffPopFlags",0)
                if(goOffPopFlags == 0) {
                    binding.rlAlarm.visibility = View.VISIBLE // 알림 띄우기
                }
                    binding.tvGoOff.text =
                        "+" + threadTime.substring(1, 3) + ":" + threadTime.substring(3, 5)
                    // qr 빨간점 보이기
                    binding.ivQrBedge.visibility = View.VISIBLE

            }else{ // 기본상태
                binding.tvGoOff.text = threadTime.substring(0,2)+":"+threadTime.substring(2,4)
                binding.tvGoOff.setTextColor(Color.parseColor("#343434"))
                // qr 빨간점 숨기기
                binding.ivQrBedge.visibility = View.GONE
                prefs.setInt("goOffPopFlags",0) // 확인 상태 "0"로 변경하기

            }
        }
    }

    override fun onStop() {
        super.onStop()
        mTimer.cancel()
    }

//    override fun onPause() {// fragment 교체시 타이머 정지
//        super.onPause()
//        mTimer.cancel()
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 출근 시간
        binding.tvGoTime.text = resultData.scheduleInfo.realStartTime?.substring(0,2) + ":" + resultData.scheduleInfo.realStartTime?.substring(2,4)

        // 남은 시간 초기화 (Thread 의 딜레이를 막기 위함)
        binding.tvGoOff.text = resultData.scheduleInfo.remainTime?.substring(0,2) + ":" + resultData.scheduleInfo.remainTime?.substring(2,4)

        // 화면 정보 받아오기
        binding.tvShopName.text = resultData.shopInfo.name // 매장명
        binding.tvDay.text =
            resultData.todayInfo.month.toString() + "/" + resultData.todayInfo.date.toString() + " " + resultData.todayInfo.day + "요일" // 오늘 날짜

        // 공동업무 완료
        binding.tvDoneCntTogether.text = resultData.taskInfo.coTask.completeCount.toString()
        binding.tvTotalCntTogehter.text = "/ " + resultData.taskInfo.coTask.totalCount.toString()
        binding.progressBarTogether.progress =
            (((resultData.taskInfo.coTask.completeCount).toDouble() / (resultData.taskInfo.coTask.totalCount).toDouble()) * 100).toInt()

        // 개인업무 완료
        binding.tvDoneCntAlone.text = resultData.taskInfo.perTask.completeCount.toString()
        binding.tvTotalCntAlone.text = "/ " + resultData.taskInfo.perTask.totalCount.toString()
        binding.progressBarAlone.progress =
            (((resultData.taskInfo.perTask.completeCount).toDouble() / (resultData.taskInfo.perTask.totalCount).toDouble()) * 100).toInt()


        // 포지션
        binding.tvWorkerPosition.text = resultData.scheduleInfo.positionTitle
        // 개인업무 포지션 정보 받아오기
        binding.tvAlone.text = resultData.scheduleInfo.positionTitle.replace(" ","")+" 업무"
        // 개인업무 꿀단지 표시여부
        var personalDoneRate = (((resultData.taskInfo.perTask.completeCount).toDouble() / (resultData.taskInfo.perTask.totalCount).toDouble()) * 100).toInt()
        // 꿀단지 상태(이미지뷰)
        if(personalDoneRate==0){
            Glide.with(requireContext()).load(R.drawable.img_honey_0).into(binding.ivHoneyProgress)
        }else if(personalDoneRate in 0..29){
            Glide.with(requireContext()).load(R.drawable.img_honey_0_to_30).into(binding.ivHoneyProgress)
        }else if(personalDoneRate in 30..59){
            Glide.with(requireContext()).load(R.drawable.img_honey_30_to_60).into(binding.ivHoneyProgress)
        }else if(personalDoneRate >=90){
            Glide.with(requireContext()).load(R.drawable.img_honey_90).into(binding.ivHoneyProgress)
        }

        val positionTime = resultData.scheduleInfo.positionTitle

        if(positionTime.contains("오픈")){
            Glide.with(requireContext()).load(R.drawable.ic_dot_open_position).into(binding.ivPositionIcon)
        }else if(positionTime.contains("미들")){
            Glide.with(requireContext()).load(R.drawable.ic_dot_middle_position).into(binding.ivPositionIcon)
        }else{
            Glide.with(requireContext()).load(R.drawable.ic_dot_end_position).into(binding.ivPositionIcon)
        }

        //val positionTime = resultData.scheduleInfo.positionTitle.substring(2, 5).replace(" ","")
       /* when (positionTime) {
            "오픈" -> {  Glide.with(requireContext()).load(R.drawable.ic_dot_open_position).into(binding.ivPositionIcon) }
            "미들" -> { Glide.with(requireContext()).load(R.drawable.ic_dot_middle_position).into(binding.ivPositionIcon)}
            "마감" -> { Glide.with(requireContext()).load(R.drawable.ic_dot_end_position).into(binding.ivPositionIcon)}
        }*/


        // 알림 팝업창 닫기
        binding.btnDelPopUp.setOnClickListener {
            binding.rlAlarm.visibility = View.GONE
            prefs.setInt("goOffPopFlags",1) // 확인 상태 "1"로 변경하기
        }

        // 알림 화면으로 이동
        binding.ibtnBell.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeAlarmActivity::class.java)
            startActivity(nextIntent)
        }

        // qr 스캔 화면으로 이동 - 아이콘
        binding.ibtnQrScan.setOnClickListener {
            callQRActivity()
        }

        // 매장 선택화면으로 이동
        binding.rlChooseShop.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeShopListActivity::class.java)
            startActivity(nextIntent)
        }

        // 공동업무 확인 화면으로 이동
        binding.rlTogetherBg.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeWTodayToDoListActivity::class.java)
            nextIntent.putExtra("tabFlags", 0)
            startActivity(nextIntent)
        }

        // 개인(포지션)업무 확인 화면으로 이동
        binding.rlPersonalWork.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeWTodayToDoListActivity::class.java)
            nextIntent.putExtra("tabFlags", 1)
            startActivity(nextIntent)
        }
    }

    // qr 코드 스캔 시작
    private fun callQRActivity() {
        val options = ScanOptions()
        // qr 스캔을 위한 코드 (= code for QR Scanning)
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)

        options.setPrompt("Scan a barcode")
        options.setCameraId(0) // Use a specific camera of the device
        options.captureActivity = QRScanningActivity::class.java
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        barcodeLauncher.launch(options)
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) { // qr 스캔 취소

        } else { // qr 스캔 성공
            PutQRScanService(this).tryPutQRScan()
            showLoadingDialog(requireContext())
        }
    }

    // QR 스캔 성공
    override fun onPutQRSuccess(response: BaseResponse) {
        dismissLoadingDialog()

        if (response.message.toString().contains("출근")){
            showCustomToast(GetCurrentTime().getTime+"에 출근이 기록되었습니다.")
        }else{
            showCustomToast(GetCurrentTime().getTime+"에 퇴근이 기록되었습니다.")
            // qr 체크 완료(화면 갱신을 위한 prefs설정)
            ApplicationClass.prefs.setInt("qrCheckState",-1)
        }

        // 다시 홈화면으로 이동
        val nextIntent = Intent(requireContext(),WorkerMainActivity::class.java)
        startActivity(nextIntent)
        (requireContext() as Activity).finishAffinity()
        //activity?.finishAffinity()
    }
    // QR 스캔 실패
    override fun onPutQRFailure(message: String) {
        dismissLoadingDialog()
    }

    // 남은 시간만 살짝 빼오기 (thread 를 위함)
    override fun onGetAllWHomeSuccess(response: GetAllWHomeResponse) {
        threadTime = response.data.scheduleInfo.remainTime
    }

    override fun onGetAllWHomeFailure(message: String) {

    }

}