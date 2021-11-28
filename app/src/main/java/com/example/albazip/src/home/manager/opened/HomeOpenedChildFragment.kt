package com.example.albazip.src.home.manager.opened


import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentHomeOpenedBinding
import com.example.albazip.src.home.common.ui.HomeAlarmActivity
import com.example.albazip.src.home.common.ui.HomeShopListActivity
import com.example.albazip.src.home.manager.custom.AddWorkBottomSheetDialog
import com.example.albazip.src.home.manager.data.AllHomeMResult
import com.example.albazip.src.home.manager.opened.ui.QRShowingActivity
import com.example.albazip.src.home.manager.opened.ui.TodaysWorkerListActivity
import com.example.albazip.src.home.manager.worklist.ui.AddTogetherWorkListActivity
import com.example.albazip.src.home.manager.worklist.ui.HomeMTodayToDoListActivity

class HomeOpenedChildFragment(data: AllHomeMResult) : BaseFragment<ChildFragmentHomeOpenedBinding>(
    ChildFragmentHomeOpenedBinding::bind,
    R.layout.child_fragment_home_opened
) {

    val resultData = data

    // 포지션 인원 카운트
    var openCnt = 0
    var middleCnt = 0
    var closeCnt = 0

    // 오픈 근무자들 배열
    var openWorkerList = ArrayList<String>()

    // 중간 근무자들 배열
    var middleWorkerList = ArrayList<String>()

    // 마감 근무자들 배열
    var closeWorkerList = ArrayList<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 포지션 인원 세기
        countWorkerList()
        showOpenUI(openCnt)
        showMiddleUI(middleCnt)
        showCloseUI(closeCnt)

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


        // qr 조회 화면으로 이동
        binding.ibtnQrScan.setOnClickListener {
            val nextIntent = Intent(requireContext(), QRShowingActivity::class.java)
            nextIntent.putExtra("shop_name", binding.tvShopName.text)
            startActivity(nextIntent)
        }

        // 알림 화면으로 이동
        binding.ibtnBell.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeAlarmActivity::class.java)
            startActivity(nextIntent)
        }

        // 오늘의 근무자 리스트 보기
        binding.tvTodayWorkers.setOnClickListener {
            val nextIntent = Intent(requireContext(), TodaysWorkerListActivity::class.java)
            startActivity(nextIntent)
        }
        binding.llWorkerList.setOnClickListener {
            val nextIntent = Intent(requireContext(), TodaysWorkerListActivity::class.java)
            startActivity(nextIntent)
        }

        // 업무 추가 액티비티
        binding.ibtnAdd.setOnClickListener {
            val nextIntent = Intent(requireContext(), AddTogetherWorkListActivity::class.java)
            startActivity(nextIntent)
            //AddWorkBottomSheetDialog().show(parentFragmentManager, "addwork")
        }

        // 매장 선택화면으로 이동
        binding.rlChooseShop.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeShopListActivity::class.java)
            startActivity(nextIntent)
        }

        // 공동업무 리스트
        binding.rlTogetherBg.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeMTodayToDoListActivity::class.java)
            nextIntent.putExtra("tabFlags", 0)
            startActivity(nextIntent)
        }

        // 개인업무 리스트
        binding.rlPersonalWorkBg.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeMTodayToDoListActivity::class.java)
            nextIntent.putExtra("tabFlags", 1)
            startActivity(nextIntent)
        }
    }

    // 포지션 별 인원수 카운트
    fun countWorkerList() {
        // 오픈 명수 카운트
        for (i in 0 until resultData.workerInfo.size) {
            if (resultData.workerInfo[i].title == "오픈") {
                openCnt++
                openWorkerList.add(resultData.workerInfo[i].firstName)
            }
        }

        // 미들 명수 카운트
        for (i in 0 until resultData.workerInfo.size) {
            if (resultData.workerInfo[i].title == "미들") {
                middleCnt++
                middleWorkerList.add(resultData.workerInfo[i].firstName)
            }
        }

        // 마감 명수 카운트
        for (i in 0 until resultData.workerInfo.size) {
            if (resultData.workerInfo[i].title == "마감") {
                closeCnt++
                closeWorkerList.add(resultData.workerInfo[i].firstName)
            }
        }
    }

    // 오픈 UI 설정
    fun showOpenUI(openCnt: Int) {
        if (openCnt == 0) {
            binding.rlOpenNone.visibility = View.VISIBLE
        } else if (openCnt == 1) {
            binding.rlOpenOne.visibility = View.VISIBLE
            binding.tvOpenOneOne.text = openWorkerList[0]
        } else if (openCnt == 2) {
            binding.rlOpenTwo.visibility = View.VISIBLE
            binding.tvOpenTwoOne.text = openWorkerList[0]
            binding.tvOpenTwoTwo.text = openWorkerList[1]
        } else if (openCnt >= 3) {
            binding.rlOpenThree.visibility = View.VISIBLE
            binding.tvOpenThreeOne.text = openWorkerList[0]
            binding.tvOpenThreeTwo.text = openWorkerList[1]
            binding.tvOpenThreeThree.text = "+" + (openWorkerList.size - 2).toString()
        }
    }

    // 미들 UI 설정
    fun showMiddleUI(middleCnt: Int) {
        if (middleCnt == 0) {
            binding.rlMidNone.visibility = View.VISIBLE
        } else if (middleCnt == 1) {
            binding.rlMidOne.visibility = View.VISIBLE
            binding.tvMidOneOne.text = middleWorkerList[0]
        } else if (middleCnt == 2) {
            binding.rlMidTwo.visibility = View.VISIBLE
            binding.tvMidTwoOne.text = middleWorkerList[0]
            binding.tvMidTwoTwo.text = middleWorkerList[1]
        } else if (middleCnt >= 3) {
            binding.rlMidThree.visibility = View.VISIBLE
            binding.tvMidThreeOne.text = middleWorkerList[0]
            binding.tvMidThreeTwo.text = middleWorkerList[1]
            binding.tvMidThreeThree.text = "+" + (middleWorkerList.size - 2).toString()
        }
    }

    // 마감 UI 설정
    fun showCloseUI(closeCnt: Int) {
        if (closeCnt == 0) {
            binding.rlCloseNone.visibility = View.VISIBLE
        } else if (closeCnt == 1) {
            binding.rlCloseOne.visibility = View.VISIBLE
            binding.tvCloseOneOne.text = closeWorkerList[0]
        } else if (closeCnt == 2) {
            binding.rlCloseTwo.visibility = View.VISIBLE
            binding.tvCloseTwoOne.text = closeWorkerList[0]
            binding.tvCloseTwoTwo.text = closeWorkerList[1]
        } else if (closeCnt >= 3) {
            binding.rlCloseThree.visibility = View.VISIBLE
            binding.tvCloseThreeOne.text = closeWorkerList[0]
            binding.tvCloseThreeTwo.text = closeWorkerList[1]
            binding.tvCloseThreeThree.text = "+" + (closeWorkerList.size - 2).toString()
        }
    }
}