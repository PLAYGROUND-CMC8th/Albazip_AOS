package com.example.albazip.src.mypage.manager.workerlist.cardevent.ui

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityCardInfoBinding
import com.example.albazip.src.mypage.manager.board.ui.WroteChildFragment
import com.example.albazip.src.mypage.manager.workerlist.ui.NoWorkerListChildFragment
import com.example.albazip.src.mypage.manager.workerlist.ui.WorkerListChildFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// 근무자 카드 클릭 시 뜨는 activity
class CardInfoActivity:BaseActivity<ActivityCardInfoBinding>(ActivityCardInfoBinding::inflate){

    private val tabTextList = arrayListOf("근무자 정보", "포지션 정보","업무 리스트")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        init()
    }

    private fun init() {
        binding.viewpager.adapter = CustomFragmentStateAdapter(this)
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
                0 -> {  // 근무자 정보 없을 때
                        CardCodeFragment() // 근무자 정보
                    }
                1 -> CardPositionFragment() // 포지션 정보
                2 -> CardToDoFragment()// 업무 리스트
                else -> CardCodeFragment()
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

}