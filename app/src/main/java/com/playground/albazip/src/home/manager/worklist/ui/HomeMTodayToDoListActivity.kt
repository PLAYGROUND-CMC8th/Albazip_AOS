package com.playground.albazip.src.home.manager.worklist.ui

import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.children
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityHomeTodayTodoListBinding
import com.playground.albazip.src.home.manager.adapter.PagerFragmentStateAdapter
import com.playground.albazip.src.home.manager.worklist.network.GetMTodayTaskFragmentView
import com.playground.albazip.src.home.manager.worklist.network.GetMTodayTaskResponse
import com.playground.albazip.src.home.manager.worklist.network.GetMTodayTaskService
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class HomeMTodayToDoListActivity :
    BaseActivity<ActivityHomeTodayTodoListBinding>(ActivityHomeTodayTodoListBinding::inflate),
    GetMTodayTaskFragmentView {

    private val tabTextList = arrayListOf("공동업무", "개인업무")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뒤로가기 버튼
        binding.ibtnBack.setOnClickListener {
            finish()
        }

        // 관리자 오늘의 할 일 전체조회
        GetMTodayTaskService(this).tryGetAllWHomeInfo()
        showLoadingDialog(this)

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

    override fun onGetMTaskSuccess(response: GetMTodayTaskResponse) {
        dismissLoadingDialog()

        val pagerAdapter = PagerFragmentStateAdapter(this)
        pagerAdapter.addFragment(ChildFragmentTogether(response.data))
        pagerAdapter.addFragment(ChildFragmentPersonal(response.data))

        binding.vpTodayTodo.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.vpTodayTodo) { tab, position ->
            tab.text = tabTextList[position]
        }.attach()

        // 공동업무 or 개인업무인지 받아오기
        if (intent.hasExtra("tabFlags")) {
            val tabFlags = intent.getIntExtra("tabFlags", 0)
            if (tabFlags == 1) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
            }
        }

        // 탭 레이아웃 커스튬
        // 초기화 시 position 0 의 텍스트 Bold 로 만들기
        binding.tabLayout.getTabAt(0)?.view?.children?.find { it is TextView }?.let { tv ->
            (tv as TextView).post {
                if (tv.text.toString() == "공동업무") {
                    tv.setTypeface(null, Typeface.BOLD)
                }
            }
        }

        tabTextStyle()
    }

    override fun onGetMTaskFailure(message: String) {
        dismissLoadingDialog()
    }
}