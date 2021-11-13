package com.example.albazip.src.mypage.worker

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.FragmentWMypageBinding
import com.example.albazip.src.mypage.manager.custom.MSelectProfileBottomSheetDialog
import com.example.albazip.src.mypage.manager.init.data.BoardInfo
import com.example.albazip.src.mypage.worker.board.BoardChildListFragment
import com.example.albazip.src.mypage.worker.custom.WSelectProfileBottomSheetDialog
import com.example.albazip.src.mypage.worker.init.data.GetWMyPageInfoResponse
import com.example.albazip.src.mypage.worker.init.data.MyInfo
import com.example.albazip.src.mypage.worker.init.data.PositionInfo
import com.example.albazip.src.mypage.worker.init.data.WBoardInfo
import com.example.albazip.src.mypage.worker.init.network.WMyPageFragmentView
import com.example.albazip.src.mypage.worker.init.network.WMyPageService
import com.example.albazip.src.mypage.worker.myInfo.MyInfoChildFragment
import com.example.albazip.src.mypage.worker.position.PosInfoChildFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class WMyPageFragment :
    BaseFragment<FragmentWMypageBinding>(FragmentWMypageBinding::bind, R.layout.fragment_w_mypage),
    WSelectProfileBottomSheetDialog.BottomSheetClickListener,WMyPageFragmentView {

    private val tabTextList = arrayListOf("내 정보", "포지션", "작성글")

    private lateinit var myInfo:MyInfo // 내정보
    private lateinit var positionInfo:PositionInfo // 포지션
    private lateinit var boardInfo:WBoardInfo  // 작성글

    private lateinit var intentPosition:String

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 근무자 전체 데이터 받아오기
        WMyPageService(this).tryGetWMyPage()
        showLoadingDialog(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 프로필 이미지 변경
        binding.ibtnChangeProfile.setOnClickListener {
            WSelectProfileBottomSheetDialog().show(childFragmentManager, "setProfile")
        }

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
                0 -> MyInfoChildFragment(myInfo)
                1 -> PosInfoChildFragment(positionInfo,intentPosition)
                2 -> BoardChildListFragment(boardInfo)
                else -> MyInfoChildFragment(myInfo)
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

    override fun onItemSelected(uri: Uri?) {
        Glide.with(requireContext()).load(uri).circleCrop().into(binding.profileImg)
    }

    override fun onWMyPageGetSuccess(response: GetWMyPageInfoResponse) {
        dismissLoadingDialog()
        if(response.code == 200){
            ////////////// 기본 정보 받아오기
            // 프로필 사진 설정
            binding.tvShopName.text = response.data.profileInfo.shopName // 가게이름
            binding.tvPosition.text = response.data.profileInfo.jobTitle // 포지션
            binding.tvWorkerName.text = response.data.profileInfo.lastName + response.data.profileInfo.firstName // 유저이름


            ////////////// 내 정보 받아오기  ///////////////////
            myInfo = response.data.myInfo
            //////////////// 포지션 정보 받아오기 ///////////////
            positionInfo = response.data.positionInfo
            //////////////// 작성글 정보 받아오기 ///////////////
            boardInfo = response.data.boardInfo

            intentPosition = response.data.profileInfo.jobTitle

            init()
        }else{
            showCustomToast(response.message.toString())
        }


    }

    override fun onWMyPageGetFailure(message: String) {
        dismissLoadingDialog()
    }
}