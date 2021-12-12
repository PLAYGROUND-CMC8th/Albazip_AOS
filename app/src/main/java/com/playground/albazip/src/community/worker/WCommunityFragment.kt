package com.playground.albazip.src.community.worker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.playground.albazip.R
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.FragmentWCommunityBinding
import com.playground.albazip.src.community.manager.ui.NoticeMChildFragment
import com.playground.albazip.src.community.worker.ui.NoticeWChildFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.playground.albazip.src.community.manager.ui.SearchCoMActivity
import com.playground.albazip.src.community.worker.ui.SearchCoWActivity
import com.playground.albazip.src.home.common.ui.HomeAlarmActivity

class WCommunityFragment :
    BaseFragment<FragmentWCommunityBinding>(FragmentWCommunityBinding::bind, R.layout.fragment_w_community) {

    private val tabTextList = arrayListOf("공지사항")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 탭 레이아웃 초기화
        init()

        // 검색화면으로 이동
        binding.tbrIbtnSearch.setOnClickListener {
            val nextIntent = Intent(requireContext(), SearchCoWActivity::class.java)
            startActivity(nextIntent)
        }

        // 알림 화면으로 이동(준비중)
        binding.tbrIbtnNotice.setOnClickListener {
            val nextIntent = Intent(requireContext(), HomeAlarmActivity::class.java)
            startActivity(nextIntent)
        }
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
            return 1
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> NoticeWChildFragment()
                else -> NoticeMChildFragment()
            }
        }
    }
}