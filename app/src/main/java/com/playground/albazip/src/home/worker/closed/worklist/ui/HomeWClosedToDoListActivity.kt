package com.playground.albazip.src.home.worker.closed.worklist.ui

import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.children
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityHomeTodayTodoListBinding
import com.playground.albazip.src.home.manager.adapter.PagerFragmentStateAdapter
import com.playground.albazip.src.home.worker.opened.worklist.network.GetWTodayTaskFragmentView
import com.playground.albazip.src.home.worker.opened.worklist.network.GetWTodayTaskResponse
import com.playground.albazip.src.home.worker.opened.worklist.network.GetWTodayTaskService
import com.playground.albazip.src.home.worker.opened.worklist.ui.ChildFragmentWPersonal
import com.playground.albazip.src.home.worker.opened.worklist.ui.ChildFragmentWTogether

class HomeWClosedToDoListActivity:
    BaseActivity<ActivityHomeTodayTodoListBinding>(ActivityHomeTodayTodoListBinding::inflate),
    GetWTodayTaskFragmentView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뒤로가기 버튼
        binding.ibtnBack.setOnClickListener {
            finish()
        }

        // 근무자 오늘의 할 일 전체조회
        GetWTodayTaskService(this).tryGetWTodayTask()
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

    // 근무자 오늘의 할 일 조회 성공
    override fun onGetWTaskSuccess(response: GetWTodayTaskResponse) {
        dismissLoadingDialog()

        // ViewPager 와 fragment 연결
        val pagerAdapter =  PagerFragmentStateAdapter(this)
        pagerAdapter.addFragment(ChildFragmentWClosedTogether(response.data))
        pagerAdapter.addFragment(ChildFragmentWClosedPersonal(response.data))

        binding.vpTodayTodo.adapter = pagerAdapter

        // 탭 타이틀 재설정
        val tabTextList = arrayListOf("공동업무", response.data.perTask.positionTitle+" 업무")
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
        // 초기화 시 position 1 의 텍스트 Bold 로 만들기
        binding.tabLayout.getTabAt(1)?.view?.children?.find { it is TextView }?.let { tv ->
            (tv as TextView).post {
                val tabFlags = intent.getIntExtra("tabFlags",0)
                if(tabFlags ==1 ) {
                    if (tv.text.toString() == response.data.perTask.positionTitle + " 업무") {
                        tv.setTypeface(null, Typeface.BOLD)
                    }
                }
            }
        }

        binding.tabLayout.getTabAt(0)?.view?.children?.find { it is TextView }?.let { tv ->
            (tv as TextView).post {
                val tabFlags = intent.getIntExtra("tabFlags",0)
                if(tabFlags == 0) {
                    if (tv.text.toString() == "공동업무") {
                        tv.setTypeface(null, Typeface.BOLD)
                    }
                }
            }
        }

        tabTextStyle()
    }

    override fun onGetWTaskFailure(message: String) {
        dismissLoadingDialog()
    }
}