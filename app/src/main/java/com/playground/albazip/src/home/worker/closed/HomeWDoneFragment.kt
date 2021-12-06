package com.playground.albazip.src.home.worker.closed

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.playground.albazip.R
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.databinding.ChildFragmentHomeWDoneBinding
import com.playground.albazip.src.home.common.ui.HomeAlarmActivity
import com.playground.albazip.src.home.common.ui.HomeShopListActivity
import com.playground.albazip.src.home.worker.data.AllHomeWResult
import com.playground.albazip.src.home.worker.network.PutQRScanFragmentView
import com.playground.albazip.src.home.worker.network.PutQRScanService
import com.playground.albazip.src.home.worker.opened.QRScanningActivity
import com.playground.albazip.src.home.worker.opened.worklist.ui.HomeWTodayToDoListActivity
import com.playground.albazip.src.main.WorkerMainActivity
import com.playground.albazip.util.GetCurrentTime
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class HomeWDoneFragment(data: AllHomeWResult): BaseFragment<ChildFragmentHomeWDoneBinding>(
    ChildFragmentHomeWDoneBinding::bind,
    R.layout.child_fragment_home_w_done),PutQRScanFragmentView {

    var resultData = data

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 매장이름
        binding.tvShopName.text =  resultData.shopInfo.name
        // 요일
        binding.tvCurrentDay.text = resultData.todayInfo.month.toString() + "/" + resultData.todayInfo.date.toString() +" "+ resultData.todayInfo.day+"요일" // 오늘 날짜

        // 완료한 업무 조회
        binding.btnDoneWork.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeWTodayToDoListActivity::class.java)
            nextIntent.putExtra("tabFlags", 0)
            startActivity(nextIntent)
        }

        // 알림 화면으로 이동
        binding.ibtnBell.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeAlarmActivity::class.java)
            startActivity(nextIntent)
        }

        // qr 스캔 화면으로 이동(1) - 이이콘
        binding.ibtnQrScan.setOnClickListener {
            callQRActivity()
        }

        // qr 스캔 화면으로 이동(2) - 버튼
        binding.btnDoneWork.setOnClickListener {
            callQRActivity()
        }

        // 매장 선택화면으로 이동
        binding.rlChooseShop.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeShopListActivity::class.java)
            startActivity(nextIntent)
        }
    }

    // qr 코드 스캔 시작
    private fun callQRActivity(){
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
        }

        // 다시 홈화면으로 이동
        val nextIntent = Intent(requireContext(), WorkerMainActivity::class.java)
        startActivity(nextIntent)
        (requireContext() as Activity).finishAffinity()
    }
    // QR 스캔 실패
    override fun onPutQRFailure(message: String) {
        dismissLoadingDialog()
    }
}