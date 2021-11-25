package com.example.albazip.src.mypage.manager

import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import com.example.albazip.databinding.FragmentMMypageBinding
import com.example.albazip.src.mypage.common.setting.MMyPageSettingActivity
import com.example.albazip.src.mypage.manager.custom.MSelectProfileBottomSheetDialog
import com.example.albazip.src.mypage.manager.workerlist.ui.NoWorkerListChildFragment
import com.example.albazip.src.mypage.manager.workerlist.ui.WorkerListChildFragment
import com.example.albazip.src.mypage.manager.board.ui.WroteChildFragment
import com.example.albazip.src.mypage.manager.init.data.GetMMyPageInfoResponse
import com.example.albazip.src.mypage.manager.init.data.NoticeInfo
import com.example.albazip.src.mypage.manager.init.data.PostInfo
import com.example.albazip.src.mypage.manager.init.data.WorkerList
import com.example.albazip.src.mypage.manager.init.network.MMyPageFragmentView
import com.example.albazip.src.mypage.manager.init.network.MMyPageService
import com.example.albazip.src.mypage.manager.workerlist.ui.AddWorkerOneActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MMyPageFragment :
    BaseFragment<FragmentMMypageBinding>(FragmentMMypageBinding::bind, R.layout.fragment_m_mypage),
    MSelectProfileBottomSheetDialog.BottomSheetClickListener, MMyPageFragmentView {

    private val tabTextList = arrayListOf("근무자", "작성글")

    private var isWorkerCardExist = false // 근무자 등록여부 체크 변수

    // fragment에 전달할 배열
    var workList = ArrayList<WorkerList>() // 근무자 리스트
    var noticeList = ArrayList<NoticeInfo>() // 공지 리스트
    var postList = ArrayList<PostInfo>() // 게시글 리스트
    
    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 서버 통신 시작 (관리자 화면 모든 정보 불러오기)
        MMyPageService(this).tryGetMMyPage()
        showLoadingDialog(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 프로필 이미지 변경
        binding.ibtnChangeProfile.setOnClickListener {
            MSelectProfileBottomSheetDialog().show(childFragmentManager, "setProfile")
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

        // 설정 화면으로 이동
        binding.tbrIbtnSetting.setOnClickListener {
            val nextIntent = Intent(requireContext(), MMyPageSettingActivity::class.java)
            startActivity(nextIntent)
        }

        // 탭 레이아웃 커스튬
        tabTextStyle()

        // 근무자 추가버튼 클릭 시
        binding.tbrIbtnPlusWorker.setOnClickListener {
            // 근무자 카드가 세 개 이상일 때
            if(workList.size >= 3){
                // 메세지 띄우기
                Snackbar.make(binding.root, "더 많은 근무자 등록은 구독 시 사용할 수 있습니다 :)", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(Color.parseColor("#5b5b5b"))
                    .show()
            }else{
                val nextIntent = Intent(requireContext(),AddWorkerOneActivity::class.java)
                startActivity(nextIntent)
            }
        }
    }

    // 끄고 다시오면 ㄱㄱ
    override fun onResume() {
        super.onResume()

        // 그쪽에서 오는 거면
        // 데이터 받아와용 ㅇㅋ

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
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    if (isWorkerCardExist == true) { // 근무자 카드가 존재한다면
                        WorkerListChildFragment(workList)// 근무자 리스트 화면 띄우기
                    } else {
                        NoWorkerListChildFragment() // 근무자 추가 화면 뷰 띄우기
                    }
                }
                1 -> WroteChildFragment(noticeList,postList)
                else -> WorkerListChildFragment(workList)
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

    // 프로필 이미지 받아오기
    override fun onItemSelected(uri: Uri?) {
        Glide.with(requireContext()).load(uri).circleCrop().into(binding.profileImg)
    }

    override fun onMMyPageGetSuccess(response: GetMMyPageInfoResponse) {
        dismissLoadingDialog()
        if(response.code == 200){

            // 근무자 정보(ProfileInfo)
            binding.tvShopName.text = response.data.profileInfo.shopName // 매장이름
            binding.tvPosition.text = response.data.profileInfo.jobTitle // 포지션 정보 (ex.사장님)
            binding.tvManagerName.text = response.data.profileInfo.lastName + response.data.profileInfo.firstName // 이름
            if(response.data.profileInfo.imagePath != null) { // 프로필이 존재하지 않는다면
               Glide.with(requireContext()).load(response.data.profileInfo.imagePath).into(binding.profileImg)
            }

            // 근무자 리스트 호출
            if(response.data.workerList?.isEmpty() == true) { // 근무자 리스트가 비어있으면
                isWorkerCardExist = false
                showCustomToast("비어있음")
            }else{
                isWorkerCardExist = true // 근무자 리스트가 존재하면

                // -> fragment 에 데이터 전달
                workList = response.data.workerList!!
            }

            // 작성글 리스트 호출(공지)
            noticeList = response.data.boardInfo.noticeInfo!!
            // 작성글 리스트 호출(게시글)
            postList = response.data.boardInfo.postInfo!!

            // 탭들 불러오기
            init()

        }else{
            showCustomToast(response.message.toString())
        }
    }

    override fun onMMyPageGetFailure(message: String) {
        dismissLoadingDialog()
    }

}