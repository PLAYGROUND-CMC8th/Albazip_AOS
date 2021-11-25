package com.example.albazip.src.splash

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.albazip.R
import com.example.albazip.config.ApplicationClass.Companion.prefs
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivitySplashBinding
import com.example.albazip.src.home.common.HomeActivity
import com.example.albazip.src.main.MainActivity
import com.example.albazip.src.main.ManagerMainActivity
import com.example.albazip.src.main.WorkerMainActivity
import com.example.albazip.src.register.common.RegisterActivity
import com.example.albazip.src.register.manager.ManagerOnBoardingActivity
import com.example.albazip.src.register.worker.ui.WorkerOnBoardingActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        // 시스템바 색상 변경
        window.statusBarColor = Color.parseColor("#ffffff")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       /* FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("GiveMeToken", token.toString())
            Toast.makeText(baseContext, token.toString(), Toast.LENGTH_SHORT).show()
        }) */

        //로그인 상태 불러오기
        val loginFlags = prefs.getInt("loginFlags", 0)

        // login 상태면 -> job 상태 불어오기
        val jobFlags = prefs.getInt("jobFlags", 0)

        Handler(Looper.getMainLooper()).postDelayed({
            // 자동로그인
            if (loginFlags == 1) {

                when (jobFlags) {
                    // 기본 login -> 포지션 가입 화면으로 이동
                    0 -> {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }

                    // 관리자 login -> 관리자 home
                    1 -> {
                        if(prefs.getInt("mBoardingFlags",0) == 0){
                            startActivity(Intent(this, ManagerOnBoardingActivity::class.java)) // 온보딩 화면
                        }else{
                            startActivity(Intent(this, ManagerMainActivity::class.java)) // 메인화면
                        }
                        finish()
                    }

                    // 근무자 login -> 근무자 home
                    2 -> {
                        if(prefs.getInt("wBoardingFlags",0) == 0){
                            startActivity(Intent(this, WorkerOnBoardingActivity::class.java)) // 온보딩 화면
                        }else{
                            startActivity(Intent(this, WorkerMainActivity::class.java)) // 메인화면
                        }
                        finish()
                    }
                }

            } else { // 로그아웃 상태면 -> 초기화면으로 이동
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, 1500)
    }
}