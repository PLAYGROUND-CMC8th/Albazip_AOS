package com.example.albazip.src.home.manager.closed

import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentHomePrepareBinding

class HomePreParingChildFragment:BaseFragment<ChildFragmentHomePrepareBinding>(ChildFragmentHomePrepareBinding::bind,
    R.layout.child_fragment_home_prepare) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}