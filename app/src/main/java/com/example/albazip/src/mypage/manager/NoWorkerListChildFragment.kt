package com.example.albazip.src.mypage.manager

import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentNoWorkerListBinding
import com.example.albazip.databinding.ChildFragmentWorkerListBinding

class NoWorkerListChildFragment : BaseFragment<ChildFragmentNoWorkerListBinding>(
    ChildFragmentNoWorkerListBinding::bind,
    R.layout.child_fragment_no_worker_list
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}