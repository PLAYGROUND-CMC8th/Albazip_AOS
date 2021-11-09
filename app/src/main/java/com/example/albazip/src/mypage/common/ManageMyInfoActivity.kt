package com.example.albazip.src.mypage.common

import android.content.Intent
import android.os.Bundle
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityManageMyInfoBinding


class ManageMyInfoActivity:BaseActivity<ActivityManageMyInfoBinding>(ActivityManageMyInfoBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 기본 정보 수정
        binding.tvModifyDefaultInfo.setOnClickListener {
            val nextIntent = Intent(this, EditMyInfoActivity::class.java)
            startActivity(nextIntent)
        }

        // 핸드폰 번호 수정
        binding.tvModifyPhoneNum.setOnClickListener {
            val nextIntent = Intent(this, EditPhoneNumActivity::class.java)
            startActivity(nextIntent)
        }
    }
}