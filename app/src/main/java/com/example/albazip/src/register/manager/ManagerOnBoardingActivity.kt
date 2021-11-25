package com.example.albazip.src.register.manager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.albazip.R
import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityOnBoardingBinding
import com.example.albazip.src.main.MainActivity
import com.example.albazip.src.main.ManagerMainActivity
import com.example.albazip.src.register.common.adapter.OnBoardingVPAdapter
import com.example.albazip.src.register.common.data.local.OnBoardData
import kotlin.collections.ArrayList

class ManagerOnBoardingActivity :
    BaseActivity<ActivityOnBoardingBinding>(ActivityOnBoardingBinding::inflate) {

    private lateinit var onBoardingAdpater: OnBoardingVPAdapter
    private lateinit var layoutOnBoardingIndicator: LinearLayout
    var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var boardList = ArrayList<OnBoardData>()

        // 관리자 메인화면으로 이동
        binding.btnStart.setOnClickListener {
            // flag 값 변경후 화면 이동
            ApplicationClass.prefs.setInt("mBoardingFlags",1)
            val nextIntent = Intent(this,ManagerMainActivity::class.java)
            startActivity(nextIntent)
            finishAffinity()
        }

        // viewpager 리스트
        boardList.add(
            OnBoardData(
                getDrawable(R.drawable.img_onboarding_1), "근무자 관리",
                "근무자의 스케줄과 시급을 설정하고, \n업무 리스트를 작성해 편리하게 인수인계 하세요."
            )
        )

        boardList.add(
            OnBoardData(
                getDrawable(R.drawable.img_onboarding_2), "실시간 근태보고",
                "업무 리스트를 작성하면 근무자가 업무를\n체크하고 관리자는 업무 진행현황을\n실시간으로 확인할 수 있어요."
            )
        )

        boardList.add(
            OnBoardData(
                getDrawable(R.drawable.img_onboarding_3), "모두를 위한 소통창",
                "새로운 공지사항이나 변경사항을\n즉시 공유하고 확인 할 수 있어요.\n이제 모두가 연결된 환경에서 근무하세요!"
            )
        )

        onBoardingAdpater = OnBoardingVPAdapter(this, boardList)

        binding.onboardVp2.adapter = onBoardingAdpater

        layoutOnBoardingIndicator = binding.llIndicator

        // 페이지 선택 동작 반환 함수
        binding.onboardVp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                if (position == 0) {
                    currentPosition = 0
                    binding.ivIndicatorOne.isSelected = true
                    binding.ivIndicatorTwo.isSelected = false
                    binding.ivIndicatorThree.isSelected = false
                } else if (position == 1) {
                    currentPosition = 1
                    binding.ivIndicatorOne.isSelected = false
                    binding.ivIndicatorTwo.isSelected = true
                    binding.ivIndicatorThree.isSelected = false
                } else {
                    // 세번째라면
                    // 뷰페이지 이동 막기
                        currentPosition = 2
                    binding.onboardVp2.isUserInputEnabled = false
                    binding.onboardFrame.visibility = View.VISIBLE // 시작 버튼 레이아웃 보이기
                }

                super.onPageSelected(position)
            }
        })

        // 페이지 선택 이동
        binding.tvNext.setOnClickListener {
            var selectPosition = currentPosition
            selectPosition++
            binding.onboardVp2.currentItem = selectPosition
        }

        // 건너뛰기 버튼
        binding.tvSkip.setOnClickListener {
            binding.onboardVp2.currentItem = 2
        }

    }

}