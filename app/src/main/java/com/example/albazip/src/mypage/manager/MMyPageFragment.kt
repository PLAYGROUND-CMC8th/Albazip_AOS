package com.example.albazip.src.mypage.manager

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.FragmentMHomeBinding
import com.example.albazip.databinding.FragmentMMypageBinding
import com.example.albazip.src.home.manager.WorkerListChildFragment
import com.example.albazip.src.home.manager.WroteChildFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MMyPageFragment :
    BaseFragment<FragmentMMypageBinding>(FragmentMMypageBinding::bind, R.layout.fragment_m_mypage) {

    private val tabTextList = arrayListOf("근무자", "작성글")

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

        // 탭 레이아웃 커스튬
        tabTextStyle()

        // 기본 선택값 = 근무자
        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))

        init()

    }

    private fun init() {
        binding.viewpager.adapter = CustomFragmentStateAdapter(requireActivity())
        TabLayoutMediator(binding.tabLayout, binding.viewpager) {
                tab, position ->
            tab.text = tabTextList[position]
        }.attach()
    }

    /* 2개의 프래그먼트를 달아줄 어댑터 */
    inner class CustomFragmentStateAdapter(fragmentActivity: FragmentActivity):
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> WorkerListChildFragment()
                1 -> WroteChildFragment()
                else -> WorkerListChildFragment()
            }
        }
    }

    private fun tabTextStyle(){
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
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

}