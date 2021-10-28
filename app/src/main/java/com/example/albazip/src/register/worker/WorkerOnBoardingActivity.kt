package com.example.albazip.src.register.worker

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.albazip.R
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityOnBoardingBinding
import com.example.albazip.src.register.common.adapter.OnBoardingVPAdapter
import com.example.albazip.src.register.common.data.local.OnBoardData

class WorkerOnBoardingActivity :
    BaseActivity<ActivityOnBoardingBinding>(ActivityOnBoardingBinding::inflate) {

    private lateinit var onBoardingAdpater: OnBoardingVPAdapter
    private lateinit var layoutOnBoardingIndicator: LinearLayout
    var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var boardList = ArrayList<OnBoardData>()

        // viewpager 리스트
        boardList.add(
            OnBoardData(
                getDrawable(R.drawable.img_w_onboarding_1), "업무 체크",
                "업무 리스트를 체크하고, 하루에\n 주어진 업무를 빼먹지 않고 수행할 수 있어요."
            )
        )

        boardList.add(
            OnBoardData(
                getDrawable(R.drawable.img_w_onboarding_2), "근무일 및 급여 확인",
                "QR체크를 통한 출퇴근 기록으로\n정확한 근무한 날과 시간이 기록돼요!\n또한 이 달에 받을 급여를 미리 알 수 있어요."
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