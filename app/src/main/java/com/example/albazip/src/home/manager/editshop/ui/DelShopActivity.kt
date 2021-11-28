package com.example.albazip.src.home.manager.editshop.ui

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.albazip.R
import com.example.albazip.config.BaseActivity
import com.example.albazip.config.BaseResponse
import com.example.albazip.databinding.ActivityHomeDelShopBinding
import com.example.albazip.src.home.manager.editshop.network.DelShopFragmentView
import com.example.albazip.src.home.manager.editshop.network.DelShopService
import com.example.albazip.src.splash.SplashActivity

class DelShopActivity:BaseActivity<ActivityHomeDelShopBinding>(ActivityHomeDelShopBinding::inflate),DelShopFragmentView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val positionId = intent.getIntExtra("positionId",0)

        binding.tvBottomTitle.text = "정말 " + intent.getStringExtra("shop_name") + "를 삭제하시겠습니까?"

        // 뒤로가기 버튼
        binding.ibtnBack.setOnClickListener {
            finish()
        }

        // 취소 버튼
        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.cbAgree.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked == true){ // 삭제 버튼 활성화
                binding.btnDelShop.isEnabled = true
                binding.btnDelShop.background =  ContextCompat.getDrawable(this, R.drawable.rectangle_fill_custom_black_15)
            }else{ // 삭제 버튼 비활성화
                binding.btnCancel.isEnabled = false
                binding.btnDelShop.background =  ContextCompat.getDrawable(this, R.drawable.rectagnle_fill_del_deactive_15)
            }
        }

        // 삭제하기 서버통신
        binding.btnDelShop.setOnClickListener {
            DelShopService(this).tryDelShopInfo(positionId)
            showLoadingDialog(this)
        }

    }

    // 매장 삭제 성공
    override fun onDelShopInfoSuccess(response: BaseResponse) {
        dismissLoadingDialog()

        // 스플래시 화면으로 이동
        val spalshIntent = Intent(this,SplashActivity::class.java)
        startActivity(spalshIntent)
        finishAffinity()

    }

    // 매장 삭제 실패
    override fun onDelShopInfoFailure(message: String) {
        dismissLoadingDialog()
    }
}