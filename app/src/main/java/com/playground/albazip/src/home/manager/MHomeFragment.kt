package com.playground.albazip.src.home.manager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.playground.albazip.R
import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.FragmentHomeBinding
import com.playground.albazip.src.community.manager.MCommunityFragment
import com.playground.albazip.src.home.common.data.HomeCommuData
import com.playground.albazip.src.home.manager.adapter.HomeMVPAdapter
import com.playground.albazip.src.home.manager.closed.HomeClosedChildFragment
import com.playground.albazip.src.home.manager.closed.HomePreParingChildFragment
import com.playground.albazip.src.home.manager.closed.HomeRestChildFragment
import com.playground.albazip.src.home.manager.data.GetAllMHomeResponse
import com.playground.albazip.src.home.manager.opened.HomeOpenedChildFragment
import com.playground.albazip.src.home.manager.opened.network.GetAllMFragmentView
import com.playground.albazip.src.home.manager.opened.network.GetMAllHomeInfoService
import com.playground.albazip.src.home.manager.worklist.ui.AddTogetherWorkListActivity
import com.playground.albazip.src.main.MainActivity
import com.playground.albazip.src.splash.CheckRightTokenService
import com.playground.albazip.src.splash.GetCheckRightTokenFragmentView
import com.playground.albazip.src.splash.GetRightTokenResponse
import com.playground.albazip.src.splash.SplashActivity


class MHomeFragment :BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::bind, R.layout.fragment_home),GetCheckRightTokenFragmentView,GetAllMFragmentView {

    // 홈의 데이터들을 받아서 뿌려준다.
    //private lateinit var noticeList:ArrayList<HomeCommuData>
    //private lateinit var boardList:ArrayList<HomeCommuData>

    private lateinit var homeVPAdapter: HomeMVPAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context) // 토큰 유효성 검사

        CheckRightTokenService(this).tryGetCheckToken()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAllCommunityBtn()
        initAddWorkBtn()
    }

    // 전체보기 버튼 (소통창 화면으로 이동)
    private fun initAllCommunityBtn() {
        binding.tvShowCommunity.setOnClickListener {
            requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.manager_main_btm_nav).menu.getItem(1).isChecked=true
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.manager_main_frm,MCommunityFragment()).commit()
        }
    }

    // 할 일 추가 버튼
    private fun initAddWorkBtn() {
        binding.floatAddWorkBtn.setOnClickListener {
            val nextIntent = Intent(requireContext(), AddTogetherWorkListActivity::class.java)
            startActivity(nextIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        // 홈 전체 데이터 불러오기
        GetMAllHomeInfoService(this).tryGetAllMHomeInfo()
        showBeeLoadingDialog(requireContext())
        //showLoadingDialog(requireContext())
    }

    override fun onGetTokenSuccess(response: GetRightTokenResponse) {

        if (response.status == 0 || response.message.equals("보유한 토큰이 만료되었습니다.")) { // 토큰 만료 -> 로그아웃
            // 로그인 flag 변경(로그아웃)
            ApplicationClass.prefs.setInt("loginFlags", 0)
            // token 비우기
            ApplicationClass.prefs.setString("X-ACCESS-TOKEN", "")

            // 메인 화면으로 이동
            val mainIntent = Intent(requireContext(), SplashActivity::class.java)
            startActivity(mainIntent)
            // 이전 엑티비티 모두 종료
            activity?.finishAffinity()
        }
    }

    override fun onGetTokenFailure(message: String) {
    }

    // 관리자 홈 전체 조회 성공
    override fun onGetAllMHomeSuccess(response: GetAllMHomeResponse) {
        dismissBeeLoadingDialog()
        //dismissLoadingDialog()

        if (response.code == 202) { // 토큰 유효성 체크
            // 로그인 flag 변경(로그아웃)
            ApplicationClass.prefs.setInt("loginFlags",0)
            // token 비우기
            ApplicationClass.prefs.setString("X-ACCESS-TOKEN","")

            // 메인 화면으로 이동
            val mainIntent = Intent(requireContext(), MainActivity::class.java)
            startActivity(mainIntent)
            // 이전 엑티비티 모두 종료
            activity?.finishAffinity()
        }

        // 매장명 받아오기
       // if(response.data.shopInfo.name != null) {
       //     ApplicationClass.prefs.setString("login_shop_name", response.data.shopInfo.name)
       // }else{
            ApplicationClass.prefs.setString("login_shop_name", "")
      //  }
        // 영업상태
        // TODO: 응?? 왜 안되지 ??
        val status = response.data.shopInfo.status

        // 영업상태 체크
        if(status == 0){ // 영업전
            childFragmentManager.beginTransaction().replace(R.id.home_child_frame_layout, HomePreParingChildFragment(response.data))
                .commitAllowingStateLoss()
        }else if(status == 1){ // 영업중
            childFragmentManager.beginTransaction().replace(R.id.home_child_frame_layout, HomeOpenedChildFragment(response.data))
                .commitAllowingStateLoss()
        }else if(status == 2){ // 영업후
            childFragmentManager.beginTransaction().replace(R.id.home_child_frame_layout, HomeClosedChildFragment(response.data))
                .commitAllowingStateLoss()
        }else{ // 휴무일
            childFragmentManager.beginTransaction().replace(R.id.home_child_frame_layout, HomeRestChildFragment(response.data))
                .commitAllowingStateLoss()
        }

        var noticeList = ArrayList<HomeCommuData>()


        for (i in 0 until response.data.boardInfo.size){
            if(response.data.boardInfo[i].status == 0){
                noticeList.add(HomeCommuData(0,response.data.boardInfo[i].title,response.data.boardInfo[i].id,0))
                if (noticeList.size > 3) break // 홈화면에서는 4개까지만 보여주기
            }
        }

        // 작성된 공지글이 없을 때
        if(noticeList.size == 0){
            binding.rlNoWriteList.visibility =View.VISIBLE
            binding.tvShowCommunity.visibility = View.GONE
        }else{
            binding.rlNoWriteList.visibility = View.GONE
            binding.tvShowCommunity.visibility = View.VISIBLE
        }

        // viewpager 데이터 받기
        //noticeList.add(HomeCommuData(0,"코로나 관련 매장 관리 공지"))

        // indicator 연결
        homeVPAdapter = HomeMVPAdapter(requireContext(),noticeList,0)

        binding.vpHomeCommunicate.adapter = homeVPAdapter

        binding.dotsIndicator.setViewPager2(binding.vpHomeCommunicate)
    }

    override fun onGetAllMHomeFailure(message: String) {
        dismissBeeLoadingDialog()
        //dismissLoadingDialog()
    }
}