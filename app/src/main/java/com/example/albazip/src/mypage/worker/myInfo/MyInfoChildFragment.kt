package com.example.albazip.src.mypage.worker.myInfo

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentMyInfoBinding
import com.example.albazip.src.mypage.worker.init.data.MyInfo

class MyInfoChildFragment(val myInfoList:ArrayList<MyInfo>) : BaseFragment<ChildFragmentMyInfoBinding>(
    ChildFragmentMyInfoBinding::bind,
    R.layout.child_fragment_my_info
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 지각 횟수 확인
        binding.rlLate.setOnClickListener {
            val nextIntent = Intent(requireContext(), LateCheckActivity::class.java)
            startActivity(nextIntent)
        }

        // 공동업무 참여 횟수 확인
        binding.rlTogether.setOnClickListener {
            val nextIntent = Intent(requireContext(), TogetherWorkActivity::class.java)
            startActivity(nextIntent)
        }

        // 업무 완수율 확인
        binding.rlSuccess.setOnClickListener {
            val nextIntent = Intent(requireContext(), DoneWorkActivity::class.java)
            startActivity(nextIntent)
        }

    }
}