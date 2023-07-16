package com.playground.albazip.src.mypage.manager.workerlist.cardevent.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.playground.albazip.R
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.ChildFragmentCardCodeBinding


class CardCodeChildFragment(val cardCode: String, val rank: String) :
    BaseFragment<ChildFragmentCardCodeBinding>(
        ChildFragmentCardCodeBinding::bind,
        R.layout.child_fragment_card_code
    ) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 근무자 코드 받아오기
        binding.tvCardCode.text = cardCode

        // 근무자 포지션 받아오기
        binding.tvInfoTitle.text = getString(R.string.info_title).format(rank)

        // 복사-붙여넣기 기능
        binding.btnCopy.setOnClickListener {
            val clipboardManager = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clipData = ClipData.newPlainText("label", binding.tvCardCode.text)
            clipboardManager!!.setPrimaryClip(clipData)
            Toast.makeText(requireContext(), "코드를 복사했어요. 알바생에게 코드를 공유해주세요.", Toast.LENGTH_LONG)
                .show()
        }
    }
}