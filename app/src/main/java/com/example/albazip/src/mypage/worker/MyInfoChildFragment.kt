package com.example.albazip.src.mypage.worker

import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentMyInfoBinding

class MyInfoChildFragment : BaseFragment<ChildFragmentMyInfoBinding>(
    ChildFragmentMyInfoBinding::bind,
    R.layout.child_fragment_my_info
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}