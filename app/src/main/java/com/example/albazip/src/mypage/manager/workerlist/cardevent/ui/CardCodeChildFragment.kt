package com.example.albazip.src.mypage.manager.workerlist.cardevent.ui

import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentCardCodeBinding

class CardCodeChildFragment(val cardCode:String):BaseFragment<ChildFragmentCardCodeBinding>(ChildFragmentCardCodeBinding::bind, R.layout.child_fragment_card_code) {

    val getCardCode = cardCode

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 근무자 코드 받아오기
        binding.tvCardCode.text = getCardCode
    }
}