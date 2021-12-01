package com.playground.albazip.src.mypage.common.setting.editinfo

import android.content.Intent
import android.os.Bundle
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityManageMyInfoBinding
import com.playground.albazip.src.mypage.common.setting.editinfo.data.LoadInfoResponse
import com.playground.albazip.src.mypage.common.setting.editinfo.network.LoadInfoFragmentView
import com.playground.albazip.src.mypage.common.setting.editinfo.network.LoadInfoService

class ManageMyInfoActivity :
    BaseActivity<ActivityManageMyInfoBinding>(ActivityManageMyInfoBinding::inflate),
    LoadInfoFragmentView {

    // 성
    var intent_last_name = ""

    // 이름
    var intent_first_name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 기본 정보 데이터 불러오기
        LoadInfoService(this).tryLoadInfo()
        showLoadingDialog(this)

        // 기본정보 수정하기
        binding.tvModifyDefaultInfo.setOnClickListener {

            // 전달할 기본 정보를 담는 배열
            var myInfoList = arrayListOf<String>(
                intent_last_name,
                intent_first_name,
                binding.tvAge.text.toString(),
                binding.tvSex.text.toString(),
            )

            val nextIntent = Intent(this, EditMyInfoActivity::class.java)

            nextIntent.putExtra("myInfoList", myInfoList)

            startActivity(nextIntent)
        }

        // 전화번호 수정하기
        binding.tvModifyPhoneNum.setOnClickListener {

            val nextIntent = Intent(this, EditPhoneNumActivity::class.java)

            startActivity(nextIntent)
        }

        // 뒤로가기
        binding.ibtnClose.setOnClickListener {
            finish()
        }
    }

    override fun onLoadInfoGetSuccess(response: LoadInfoResponse) {
        dismissLoadingDialog()

        intent_last_name = response.data.lastName
        intent_first_name = response.data.firstName

        binding.tvName.text = response.data.lastName + response.data.firstName // 이름
        binding.tvAge.text = response.data.birthyear // 생년월일

        // 성별
        if (response.data.gender == 0) {
            binding.tvSex.text = "남자"
        } else {
            binding.tvSex.text = "여자"
        }

        val phoneNum = response.data.phone

        // 전화번호
        binding.tvNum.text =
            phoneNum.substring(0, 3) + "-" + phoneNum.substring(3, 7) + "-" + phoneNum.substring(7, 11)
    }

    override fun onLoadInfoGetFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }
}