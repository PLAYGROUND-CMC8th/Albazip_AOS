package com.playground.albazip.src.main

import android.os.Bundle
import com.playground.albazip.R
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityWorkerMainBinding
import com.playground.albazip.src.community.worker.WCommunityFragment
import com.playground.albazip.src.home.worker.WHomeFragment
import com.playground.albazip.src.mypage.worker.WMyPageFragment
import com.playground.albazip.util.BackPressCloseHandler

// 근무자 시작 화면
class WorkerMainActivity :
    BaseActivity<ActivityWorkerMainBinding>(ActivityWorkerMainBinding::inflate) {
    // 뒤로가기 핸들러
    private lateinit var backPressCloseHandler: BackPressCloseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        backPressCloseHandler = BackPressCloseHandler(this)


        supportFragmentManager.beginTransaction().replace(R.id.worker_main_frm, WHomeFragment())
            .commitAllowingStateLoss()


        binding.workerMainBtmNav.run {
            setOnItemSelectedListener {
                when (it.itemId) {

                    // 홈
                    R.id.menu_main_btm_nav_home -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.worker_main_frm, WHomeFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener true
                    }
                    // 소통창
                    R.id.menu_main_btm_nav_community -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.worker_main_frm, WCommunityFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener true
                    }

                    // 마이페이지
                    R.id.menu_main_btm_nav_mypage -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.worker_main_frm, WMyPageFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener true
                    }
                }
                false
            }
        }


    }

    // 뒤로가기 스택 감지
    override fun onBackPressed() {

        if (binding.workerMainBtmNav.selectedItemId == R.id.menu_main_btm_nav_home) {
            //super.onBackPressed()
            backPressCloseHandler.onBackPressed()
        } else {
            binding.workerMainBtmNav.selectedItemId = R.id.menu_main_btm_nav_home
        }
    }
}