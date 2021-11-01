package com.example.albazip.src.home.manager

import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentWroteBinding

class WroteChildFragment : BaseFragment<ChildFragmentWroteBinding>(
    ChildFragmentWroteBinding::bind,
    R.layout.child_fragment_wrote
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}