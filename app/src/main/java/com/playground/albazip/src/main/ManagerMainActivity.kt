package com.playground.albazip.src.main

import android.os.Bundle
import com.playground.albazip.R
import com.playground.albazip.config.ApplicationClass.Companion.prefs
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityManagerMainBinding
import com.playground.albazip.src.community.manager.MCommunityFragment
import com.playground.albazip.src.home.manager.MHomeFragment
import com.playground.albazip.src.mypage.manager.MMyPageFragment
import com.playground.albazip.util.BackPressCloseHandler

// 관리자 시작화면
class ManagerMainActivity :
    BaseActivity<ActivityManagerMainBinding>(ActivityManagerMainBinding::inflate) {

    // 뒤로가기 핸들러
    private lateinit var backPressCloseHandler: BackPressCloseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        backPressCloseHandler = BackPressCloseHandler(this)

        supportFragmentManager.beginTransaction().replace(R.id.manager_main_frm, MHomeFragment())
            .commitAllowingStateLoss()


        binding.managerMainBtmNav.run {
            setOnItemSelectedListener {
                when (it.itemId) {

                    // 홈
                    R.id.menu_main_btm_nav_home -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.manager_main_frm, MHomeFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener true
                    }
                    // 소통창
                    R.id.menu_main_btm_nav_community -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.manager_main_frm, MCommunityFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener true
                    }

                    // 마이페이지
                    R.id.menu_main_btm_nav_mypage -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.manager_main_frm, MMyPageFragment())
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

        if (binding.managerMainBtmNav.selectedItemId == R.id.menu_main_btm_nav_home) {
            //super.onBackPressed()
            backPressCloseHandler.onBackPressed()
        } else {
            if (prefs.getInt("backStackState", 0) == 1){
                super.onBackPressed()
                prefs.setInt("backStackState", 0)
            }else{
                binding.managerMainBtmNav.selectedItemId = R.id.menu_main_btm_nav_home
            }
        }
    }
}