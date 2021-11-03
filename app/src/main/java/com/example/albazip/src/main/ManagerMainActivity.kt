package com.example.albazip.src.main

import android.os.Bundle
import com.example.albazip.R
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityManagerMainBinding
import com.example.albazip.src.community.manager.MCommunityFragment
import com.example.albazip.src.home.manager.MHomeFragment
import com.example.albazip.src.mypage.manager.MMyPageFragment
import com.example.albazip.src.schedule.manager.MScheduleFragment
import com.example.albazip.util.BackPressCloseHandler

// 관리자 시작화면
class ManagerMainActivity :
    BaseActivity<ActivityManagerMainBinding>(ActivityManagerMainBinding::inflate) {

    // 뒤로가기 핸들러
    private lateinit var backPressCloseHandler: BackPressCloseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        backPressCloseHandler = BackPressCloseHandler(this)

        supportFragmentManager.beginTransaction().replace(R.id.manager_main_frm, MHomeFragment()).commitAllowingStateLoss()


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

                    // 스케줄
                    R.id.menu_main_btm_nav_schedule -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.manager_main_frm, MScheduleFragment())
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

        if(binding.managerMainBtmNav.selectedItemId == R.id.menu_main_btm_nav_home){
            //super.onBackPressed()
            backPressCloseHandler.onBackPressed()
        }else{
            binding.managerMainBtmNav.selectedItemId = R.id.menu_main_btm_nav_home
        }
    }
}