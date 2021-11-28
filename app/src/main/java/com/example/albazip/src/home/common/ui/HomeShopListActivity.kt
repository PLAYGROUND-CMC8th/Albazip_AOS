package com.example.albazip.src.home.common.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityHomeShopListBinding
import com.example.albazip.src.home.common.adapter.ShopListAdapter
import com.example.albazip.src.home.common.data.ShopListData
import com.example.albazip.src.home.network.GetShopListService
import com.example.albazip.src.home.network.ShopListFragmentView
import com.example.albazip.src.home.network.ShopListResponse
import com.google.android.material.snackbar.Snackbar

class HomeShopListActivity:BaseActivity<ActivityHomeShopListBinding>(ActivityHomeShopListBinding::inflate),ShopListFragmentView {

    private lateinit var shopListAdapter:ShopListAdapter
    private var shopList = ArrayList<ShopListData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 매장목록 조회 api 연결
        GetShopListService(this).tryGetShopList()
        showLoadingDialog(this)

        // 화면 닫기
        binding.btnOut.setOnClickListener {
            finish()
        }

        // 매장 추가 기능
        binding.btnAdd.setOnClickListener {
            Snackbar.make(binding.root, "다음 업데이트에서 추가될 기능입니다:)", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(Color.parseColor("#5b5b5b"))
                .show()
        }
    }

    // 매장목록 조회 성공
    override fun onShopListGetSuccess(response: ShopListResponse) {
        dismissLoadingDialog()

        for(i in 0 until response.data.size){
            shopList.add(ShopListData(response.data[i].managerId,response.data[i].workerId,response.data[i].shop_name,response.data[i].status))
        }

        Log.d("testing",shopList.toString())

        shopListAdapter = ShopListAdapter(shopList,this@HomeShopListActivity)
        binding.rvShopList.adapter = shopListAdapter
    }

    // 매장목록 조회 실패
    override fun onShopListGetFailure(message: String) {
        dismissLoadingDialog()
    }
}