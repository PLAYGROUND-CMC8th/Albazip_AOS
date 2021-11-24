package com.example.albazip.src.community.manager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.FragmentMCommunityBinding
import com.example.albazip.src.community.manager.ui.NoticeMChildFragment
import com.google.android.material.tabs.TabLayoutMediator

class MCommunityFragment :
    BaseFragment<FragmentMCommunityBinding>(FragmentMCommunityBinding::bind, R.layout.fragment_m_community) {

    private val tabTextList = arrayListOf("공지사항")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 탭 레이아웃 초기화
        init()
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
                0 -> NoticeMChildFragment()
                else -> NoticeMChildFragment()
            }
        }
    }
}