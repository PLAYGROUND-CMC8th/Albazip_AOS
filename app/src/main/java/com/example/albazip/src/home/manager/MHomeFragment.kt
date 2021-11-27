package com.example.albazip.src.home.manager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.FragmentHomeBinding
import com.example.albazip.src.home.common.data.HomeCommuData
import com.example.albazip.src.home.manager.adapter.HomeVPAdapter
import com.example.albazip.src.home.manager.closed.HomeClosedChildFragment
import com.example.albazip.src.home.manager.closed.HomePreParingChildFragment
import com.example.albazip.src.home.manager.closed.HomeRestChildFragment
import com.example.albazip.src.home.manager.data.GetAllMHomeResponse
import com.example.albazip.src.home.manager.opened.HomeOpenedChildFragment
import com.example.albazip.src.home.manager.opened.network.GetAllMFragmentView
import com.example.albazip.src.home.manager.opened.network.GetMAllHomeInfoService
import com.example.albazip.src.splash.CheckRightTokenService
import com.example.albazip.src.splash.GetCheckRightTokenFragmentView
import com.example.albazip.src.splash.GetRightTokenResponse
import com.example.albazip.src.splash.SplashActivity


class MHomeFragment :BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::bind, R.layout.fragment_home),GetCheckRightTokenFragmentView,GetAllMFragmentView {

    // 홈의 데이터들을 받아서 뿌려준다.
    //private lateinit var noticeList:ArrayList<HomeCommuData>
    //private lateinit var boardList:ArrayList<HomeCommuData>

    private lateinit var homeVPAdapter: HomeVPAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context) // 토큰 유효성 검사

        CheckRightTokenService(this).tryGetCheckToken()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 홈 전체 데이터 불러오기
        GetMAllHomeInfoService(this).tryGetAllMHomeInfo()
        showLoadingDialog(requireContext())

    }

    override fun onGetTokenSuccess(response: GetRightTokenResponse) {

        if(response.status == 0){ // 토큰 만료 -> 로그아웃
            // 로그인 flag 변경(로그아웃)
            ApplicationClass.prefs.setInt("loginFlags",0)
            // token 비우기
            ApplicationClass.prefs.setString("X-ACCESS-TOKEN","")

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
        dismissLoadingDialog()

        // 영업상태
        //var status = response.data.shopInfo.status
        var status = 1
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
                noticeList.add(HomeCommuData(0,response.data.boardInfo[i].title))
                if (noticeList.size > 3) break // 홈화면에서는 4개까지만 보여주기
            }
        }

        // 작성된 공지글이 없을 때
        if(noticeList.size == 0){
            binding.rlNoWriteList.visibility =View.VISIBLE
        }else{
            binding.rlNoWriteList.visibility = View.GONE
        }

        // viewpager 데이터 받기
        //noticeList.add(HomeCommuData(0,"코로나 관련 매장 관리 공지"))

        // indicator 연결
        homeVPAdapter = HomeVPAdapter(requireContext(),noticeList)

        binding.vpHomeCommunicate.adapter = homeVPAdapter

        binding.dotsIndicator.setViewPager2(binding.vpHomeCommunicate)
    }

    override fun onGetAllMHomeFailure(message: String) {
        dismissLoadingDialog()
    }
}