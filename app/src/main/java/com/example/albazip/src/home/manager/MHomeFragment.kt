package com.example.albazip.src.home.manager

import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.FragmentMHomeBinding
import com.example.albazip.src.home.manager.closed.HomePreParingChildFragment
import com.example.albazip.src.home.manager.opened.HomeOpenedChildFragment

class MHomeFragment :
    BaseFragment<FragmentMHomeBinding>(FragmentMHomeBinding::bind, R.layout.fragment_m_home) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction().replace(R.id.home_child_frame_layout, HomeOpenedChildFragment())
            .commitAllowingStateLoss()
    }
}