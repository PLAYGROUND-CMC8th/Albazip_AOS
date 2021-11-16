package com.example.albazip.src.home.manager.opened

import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentHomeOpenedBinding

class HomeOpenedChildFragment: BaseFragment<ChildFragmentHomeOpenedBinding>(
    ChildFragmentHomeOpenedBinding::bind,
    R.layout.child_fragment_home_opened) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}