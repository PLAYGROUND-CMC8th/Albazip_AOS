package com.playground.albazip.src.home.worker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.playground.albazip.R
import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.ApplicationClass.Companion.prefs
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.FragmentHomeBinding
import com.playground.albazip.src.community.worker.WCommunityFragment
import com.playground.albazip.src.home.common.data.HomeCommuData
import com.playground.albazip.src.home.manager.adapter.HomeVPAdapter
import com.playground.albazip.src.home.worker.closed.HomeWDoneFragment
import com.playground.albazip.src.home.worker.closed.HomeWReadyFragment
import com.playground.albazip.src.home.worker.closed.HomeWRestFragment
import com.playground.albazip.src.home.worker.data.GetAllWHomeResponse
import com.playground.albazip.src.home.worker.network.GetAllWFragmentView
import com.playground.albazip.src.home.worker.network.GetAllWHomeInfoService
import com.playground.albazip.src.home.worker.opened.HomeWOpenedFragment
import com.playground.albazip.src.splash.CheckRightTokenService
import com.playground.albazip.src.splash.GetCheckRightTokenFragmentView
import com.playground.albazip.src.splash.GetRightTokenResponse
import com.playground.albazip.src.splash.SplashActivity

class WHomeFragment :
    BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::bind, R.layout.fragment_home),GetAllWFragmentView,
    GetCheckRightTokenFragmentView {

    //private lateinit var noticeList:ArrayList<HomeCommuData>
    //private lateinit var boardList:ArrayList<HomeCommuData>

    private lateinit var homeVPAdapter: HomeVPAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context) // 토큰 유효성 검사

        CheckRightTokenService(this).tryGetCheckToken()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전체보기 (커뮤니티 화면으로 이동)
        binding.tvShowCommunity.setOnClickListener {
            requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.worker_main_btm_nav).menu.getItem(1).isChecked=true
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.worker_main_frm,
                WCommunityFragment()
            ).commit()
        }

        // 알바생은 업무추가 버튼 끄기
        binding.floatAddWorkBtn.visibility = View.GONE

    }

    override fun onResume() {
        super.onResume()

        // 근무자 홈 전체조회 통신
        GetAllWHomeInfoService(this).tryGetAllWHomeInfo()
        showBeeLoadingDialog(requireContext())
        //showLoadingDialog(requireContext())
    }

    // 관리자 전체 정보 조회 성공
    override fun onGetAllWHomeSuccess(response: GetAllWHomeResponse) {
        dismissBeeLoadingDialog()
        //dismissLoadingDialog()

        // 매장명 받아오기
        ApplicationClass.prefs.setString("login_shop_name",response.data.shopInfo.name)

        // 영업상태
        //var status = 1
        var status = response.data.shopInfo.status

        var qrCheckStatus = prefs.getInt("qrCheckState",0)

        // 다음날로 돌아왔을 때
        if(status == 0 || status == 3){
            prefs.setInt("qrCheckState",0) // qr 체크 상태 초기화
        }

        if (status == 1){ // 영업중일 때
            if (qrCheckStatus == -1){ // qr을 찍고 퇴근한 상태라면
                status = -1 // 오픈 -> 마감 (qr 체크완료 화면으로 이동)
            }
        }

        // 영업상태 체크
        if(status == 0){ // 영업전
            childFragmentManager.beginTransaction().replace(R.id.home_child_frame_layout, HomeWReadyFragment(response.data))
                .commitAllowingStateLoss()
        }else if(status == 1){ // 영업중
                childFragmentManager.beginTransaction().replace(R.id.home_child_frame_layout, HomeWOpenedFragment(response.data))
                    .commitAllowingStateLoss()
        }else if(status == 2){ // 영업후
            childFragmentManager.beginTransaction().replace(R. id.home_child_frame_layout, HomeWDoneFragment(response.data))
                .commitAllowingStateLoss()
        }else if(status == -1){ // 가게는 영업중이나, 퇴근을 찍어버렸을 때
            childFragmentManager.beginTransaction().replace(R.id.home_child_frame_layout, HomeWDoneFragment(response.data))
                .commitAllowingStateLoss()
        } else{ // 휴무일
            childFragmentManager.beginTransaction().replace(R.id.home_child_frame_layout, HomeWRestFragment(response.data))
                .commitAllowingStateLoss()
        }

        var noticeList = ArrayList<HomeCommuData>()

        for (i in 0 until response.data.boardInfo.size){
            if(response.data.boardInfo[i].status == 0){
                noticeList.add(HomeCommuData(response.data.boardInfo[i].confirm,response.data.boardInfo[i].title,response.data.boardInfo[i].id,response.data.boardInfo[i].confirm))
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

        // indicator 연결
        homeVPAdapter = HomeVPAdapter(requireContext(),noticeList,1)

        binding.vpHomeCommunicate.adapter = homeVPAdapter

        binding.dotsIndicator.setViewPager2(binding.vpHomeCommunicate)
    }

    override fun onGetAllWHomeFailure(message: String) {
        dismissBeeLoadingDialog()
        //dismissLoadingDialog()
    }


    // 유효 토큰 검사
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

}