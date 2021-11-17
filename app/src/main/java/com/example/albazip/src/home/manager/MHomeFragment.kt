package com.example.albazip.src.home.manager

import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.FragmentMHomeBinding
import com.example.albazip.src.home.common.data.HomeCommuData
import com.example.albazip.src.home.manager.adapter.HomeVPAdapter
import com.example.albazip.src.home.manager.closed.HomePreParingChildFragment


class MHomeFragment :
    BaseFragment<FragmentMHomeBinding>(FragmentMHomeBinding::bind, R.layout.fragment_m_home) {

    //private lateinit var noticeList:ArrayList<HomeCommuData>
    //private lateinit var boardList:ArrayList<HomeCommuData>

    private lateinit var homeVPAdapter: HomeVPAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction().replace(R.id.home_child_frame_layout, HomePreParingChildFragment())
            .commitAllowingStateLoss()

        var noticeList = ArrayList<HomeCommuData>()

        // viewpager 데이터 받기
        noticeList.add(HomeCommuData(0,"코로나 관련 매장 관리 공지"))
        noticeList.add(HomeCommuData(1,"휴가 관련 공지"))
        noticeList.add(HomeCommuData(0,"중요 공지"))


        // indicator 연결
        homeVPAdapter = HomeVPAdapter(requireContext(),noticeList)

        binding.vpHomeCommunicate.adapter = homeVPAdapter

        binding.dotsIndicator.setViewPager2(binding.vpHomeCommunicate)


    }
}