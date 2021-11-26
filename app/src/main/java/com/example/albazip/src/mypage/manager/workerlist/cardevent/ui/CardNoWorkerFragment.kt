package com.example.albazip.src.mypage.manager.workerlist.cardevent.ui

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.FragmentCardInfoBinding
import com.example.albazip.src.mypage.manager.workerlist.cardevent.data.EmptyWorkerResponse
import com.example.albazip.src.mypage.manager.workerlist.cardevent.data.PositionProfileInfo
import com.example.albazip.src.mypage.manager.workerlist.cardevent.data.PositionTaskList
import com.example.albazip.src.mypage.manager.workerlist.cardevent.data.WorkerInfo
import com.example.albazip.src.mypage.manager.workerlist.cardevent.network.EmptyWorkerFragmentView
import com.example.albazip.src.mypage.manager.workerlist.cardevent.network.EmptyWorkerService
import com.example.albazip.src.mypage.manager.workerlist.custom.DelPositionBottomSheetDialog
import com.example.albazip.src.mypage.worker.init.data.PositionInfo
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// 근무자 카드 클릭 시 뜨는 activity
class CardNoWorkerFragment(val positionId: Int) : BaseFragment<FragmentCardInfoBinding>(
    FragmentCardInfoBinding::bind,
    R.layout.fragment_card_info
), EmptyWorkerFragmentView {

    private val tabTextList = arrayListOf("근무자 정보", "포지션 정보", "업무 리스트")
    private val getPositionId = positionId // 근무자 고유 id

    // 근무자 부재 데이터
    private var cardCode: String = "" // 부재시 카드코드

    // 근무자 존재 데이터

    // 공통
    private lateinit var positionProfileInfo: PositionProfileInfo // 프로필 정보
    private lateinit var positionInfo: PositionInfo // 포지션 정보
    private lateinit var positionTaskList: ArrayList<PositionTaskList>  // 얘는 나중에 고쳐야 할듯 ㅎㅎ !


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // sticky tab layout
        binding.stickyNestedScrollview.run {
            header = binding.clTab
            stickListener = { _ ->
                Log.d("LOGGER_TAG", "stickListener")
            }
            freeListener = { _ ->
                Log.d("LOGGER_TAG", "freeListener")
            }
        }

        // 뒤로가기
        binding.tbrIbtnBackBtn.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.detach(this)
            transaction.commit()
        }

        // 탭 레이아웃 커스튬
        tabTextStyle()

        // 서버통신 시작
        EmptyWorkerService(this).tryGetEmptyCard(getPositionId)
        showLoadingDialog(requireContext())
    }

    private fun init() {
        binding.viewpager.adapter = CustomFragmentStateAdapter(requireActivity())
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = tabTextList[position]
        }.attach()
    }

    /* 2개의 프래그먼트를 달아줄 어댑터 */
    inner class CustomFragmentStateAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> CardCodeChildFragment(cardCode) // 근무자 부재
                1 -> CardPositionChildFragment(positionInfo, 0, positionId) // 포지션 정보
                2 -> CardToDoChildFragment(positionTaskList)// 업무 리스트
                else -> CardCodeChildFragment(cardCode)
            }
        }
    }

    private fun tabTextStyle() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    setStyleForTab(it, Typeface.BOLD)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    setStyleForTab(it, Typeface.NORMAL)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    fun setStyleForTab(tab: TabLayout.Tab, style: Int) {
        tab.view.children.find { it is TextView }?.let { tv ->
            (tv as TextView).post {
                tv.setTypeface(null, style)
            }
        }
    }

    // 서버 통신 성공
    override fun onGetSuccess(response: EmptyWorkerResponse) {
        dismissLoadingDialog()
        if (response.code == 200) {
            // 프로필 설정
            Glide.with(requireContext()).load(R.drawable.img_profile_48_px_none).circleCrop()
                .into(binding.ivProfile)
            binding.tvPosition.text = response.data.positionProfile.title // 포지션
            binding.tvFirstName.text = response.data.positionProfile.firstName // 이름
            binding.tvRank.text = response.data.positionProfile.rank // 직위

            cardCode = response.data.workerInfo.positionInfo.code

            positionInfo = response.data.positionInfo

            positionTaskList = response.data.positionTaskList
        }

        init()
    }

    override fun onGetFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }
}