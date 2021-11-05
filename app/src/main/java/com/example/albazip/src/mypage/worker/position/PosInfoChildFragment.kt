package com.example.albazip.src.mypage.worker.position

import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentPosInfoBinding

// 근무자 > 포지션 탭
class PosInfoChildFragment : BaseFragment<ChildFragmentPosInfoBinding>(
    ChildFragmentPosInfoBinding::bind,
    R.layout.child_fragment_pos_info
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}