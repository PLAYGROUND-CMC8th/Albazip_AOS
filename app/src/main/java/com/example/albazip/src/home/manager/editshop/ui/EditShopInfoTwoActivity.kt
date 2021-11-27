package com.example.albazip.src.home.manager.editshop.ui

import android.os.Bundle
import android.util.Log
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityEditShopInfoTwoBinding

class EditShopInfoTwoActivity:BaseActivity<ActivityEditShopInfoTwoBinding>(ActivityEditShopInfoTwoBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val registerDataList  = intent.getSerializableExtra("registerDataList") as ArrayList<String>
        val holiday = intent.getSerializableExtra("holiday")



    }
}