package com.example.albazip.src.home.worker.opened.worklist.ui

import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.children
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityHomeTodayTodoListBinding
import com.example.albazip.src.home.manager.adapter.PagerFragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeWTodayToDoListActivity:
    BaseActivity<ActivityHomeTodayTodoListBinding>(ActivityHomeTodayTodoListBinding::inflate) {

    private val tabTextList = arrayListOf("공동업무", "평일마감 업무")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewPager 와 fragment 연결
        val pagerAdapter =  PagerFragmentStateAdapter(this)
        pagerAdapter.addFragment(ChildFragmentWTogether())
        pagerAdapter.addFragment(ChildFragmentWPersonal())

        binding.vpTodayTodo.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.vpTodayTodo) { tab, position ->
            tab.text = tabTextList[position]
        }.attach()

        // 공동업무 or 개인업무인지 받아오기
        if(intent.hasExtra("tabFlags")) {
            val tabFlags = intent.getIntExtra("tabFlags",0)
            if(tabFlags == 1) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
            }
        }
        // 탭 레이아웃 커스튬
        // 초기화 시 position 0 의 텍스트 Bold 로 만들기
        binding.tabLayout.getTabAt(1)?.view?.children?.find { it is TextView }?.let { tv ->
            (tv as TextView).post {
                if(tv.text.toString() == "평일마감 업무") {
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