package com.playground.albazip.src.community.worker.ui

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivitySearchBinding
import com.playground.albazip.src.community.common.network.GetNoticeSearchFragmentView
import com.playground.albazip.src.community.common.network.GetNoticeSearchService
import com.playground.albazip.src.community.common.network.SearchResponse
import com.playground.albazip.src.community.manager.ui.NoticeMChildFragment
import com.playground.albazip.src.community.manager.ui.NoticeMSearchChildFragment

class SearchCoWActivity:BaseActivity<ActivitySearchBinding>(ActivitySearchBinding::inflate),
    GetNoticeSearchFragmentView {

    private val tabTextList = arrayListOf("공지사항")
    private lateinit var searchResponse: SearchResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뒤로가기 버튼
        binding.ibtnBack.setOnClickListener {
            finish()
        }

        // 시작시 공지사항 탭 숨기기
        binding.clTab.visibility = View.INVISIBLE

        // 시작시 키보드 자동 포커스
        binding.etvSearchArea.requestFocus()
        //키보드 보이게 하는 부분
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        // 검색하기
        binding.etvSearchArea.setOnKeyListener { _, keyCode, event ->

            if ((event.action== KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // 검색 시작
                // 공백을 입력했을 때 검색 결과 없음 띄우기
                if(binding.etvSearchArea.text.toString().replace(" ", "").equals("")){
                    showBlankResult()
                }else{
                    // 공백 없이 정상입력했을 때
                    GetNoticeSearchService(this).tryGetNoticeRead(binding.etvSearchArea.text.toString().replace(" ", ""))
                    showLoadingDialog(this@SearchCoWActivity)
                }

                true
            } else {
                false
            }
        }
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
            return 1
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> NoticeWSearchChildFragment(searchResponse)
                else -> NoticeMChildFragment()
            }
        }
    }

    // 미확인 공지 -> 확인 (게시글 확인이후)
    override fun onResume() {
        super.onResume()
        GetNoticeSearchService(this).tryGetNoticeRead(binding.etvSearchArea.text.toString().replace(" ", ""))
        showLoadingDialog(this@SearchCoWActivity)
    }

    override fun onGetSearchNoticeSuccess(response: SearchResponse) {
        dismissLoadingDialog()
        searchResponse = response

        if(response.data.size == 0){ // 검색결과가 없다면
            binding.llNoSearchResult.visibility = View.VISIBLE // 검색결과 없음 창 보여주기
            binding.clTab.visibility = View.GONE // 탭 숨기기
        }else{
            binding.llNoSearchResult.visibility = View.GONE // 검색결과 없음 창 숨기기
            binding.clTab.visibility = View.VISIBLE
        }

        init()
    }

    // 검색 실패
    override fun onGetSearchNoticeFailure(message: String) {
        dismissLoadingDialog()
    }

    // 검색 결과 없음 화면 띄우기(공백으로 검색했을 때)
    private fun showBlankResult(){
        binding.llNoSearchResult.visibility = View.VISIBLE // 검색결과 없음 창 보여주기
        binding.clTab.visibility = View.GONE // 탭 숨기기
    }
}