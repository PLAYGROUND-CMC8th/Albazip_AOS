package com.example.albazip.src.mypage.manager.workerlist.cardevent.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentCardCodeBinding


class CardCodeChildFragment(val cardCode:String):BaseFragment<ChildFragmentCardCodeBinding>(ChildFragmentCardCodeBinding::bind, R.layout.child_fragment_card_code) {

    val getCardCode = cardCode

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 근무자 코드 받아오기
        binding.tvCardCode.text = getCardCode

        // 복사-붙여넣기 기능
        binding.rlCopyBtn.setOnClickListener {
            val clipboardManager = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clipData = ClipData.newPlainText("label", binding.tvCardCode.text)
            clipboardManager!!.setPrimaryClip(clipData)
            Toast.makeText(requireContext(), "코드가 복사되었습니다.", Toast.LENGTH_LONG).show()
        }
    }
}