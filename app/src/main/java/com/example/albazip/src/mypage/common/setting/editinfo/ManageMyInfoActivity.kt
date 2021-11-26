package com.example.albazip.src.mypage.common.setting.editinfo

import android.os.Bundle
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityManageMyInfoBinding
import com.example.albazip.src.mypage.common.setting.editinfo.data.LoadInfoResponse
import com.example.albazip.src.mypage.common.setting.editinfo.network.LoadInfoFragmentView
import com.example.albazip.src.mypage.common.setting.editinfo.network.LoadInfoService

class ManageMyInfoActivity:BaseActivity<ActivityManageMyInfoBinding>(ActivityManageMyInfoBinding::inflate),LoadInfoFragmentView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 기본 정보 데이터 불러오기
        LoadInfoService(this).tryLoadInfo()
        showLoadingDialog(this)
    }

    override fun onLoadInfoGetSuccess(response: LoadInfoResponse) {
       dismissLoadingDialog()

       binding.tvName.text = response.data.lastName+response.data.firstName // 이름
       binding.tvAge.text = response.data.birthyear // 생년월일

        // 성별
       if(response.data.gender == 0){
           binding.tvSex.text = "남자"
       }else{
           binding.tvSex.text = "여자"
       }

        val phoneNum = response.data.phone

        // 전화번호
        binding.tvNum.text = phoneNum.substring(0,3) + "-" + phoneNum.substring(3,7)+"-"+phoneNum.substring(7,11)
    }

    override fun onLoadInfoGetFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }
}