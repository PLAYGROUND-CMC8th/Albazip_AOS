package com.example.albazip.src.home.manager.worklist.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityHomeTodayTodoListBinding
import com.example.albazip.src.home.manager.adapter.PagerFragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class HomeTodayToDoListActivity:BaseActivity<ActivityHomeTodayTodoListBinding>(ActivityHomeTodayTodoListBinding::inflate) {

    private val tabTextList = arrayListOf("공동업무", "개인업무")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewPager 와 fragment 연결
        val pagerAdapter =  PagerFragmentStateAdapter(this)
        pagerAdapter.addFragment(ChildFragmentTogether())
        pagerAdapter.addFragment(ChildFragmentPersonal())

        binding.vpTodayTodo.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.vpTodayTodo) { tab, position ->
            tab.text = tabTextList[position]
        }.attach()

        // 탭 레이아웃 커스튬
        // 초기화 시 position 0 의 텍스트 Bold 로 만들기
        binding.tabLayout.getTabAt(0)?.view?.children?.find { it is TextView }?.let { tv ->
            (tv as TextView).post {
                if(tv.text.toString() == "공동업무") {
                    tv.setTypeface(null, Typeface.BOLD)
                }
            }
        }

        tabTextStyle()
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