package com.playground.albazip.src.update.setworker.card

import android.os.Bundle
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityUpdateEditWorkerOneBinding

class UpdateEditWorkerOneActivity :
    BaseActivity<ActivityUpdateEditWorkerOneBinding>(ActivityUpdateEditWorkerOneBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBackBtn()
    }

    private fun initBackBtn() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}