package com.playground.albazip.src.splash

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import com.kakao.util.maps.helper.Utility
import com.playground.albazip.config.ApplicationClass.Companion.prefs
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivitySplashBinding
import com.playground.albazip.src.home.common.ui.HomeActivity
import com.playground.albazip.src.main.MainActivity
import com.playground.albazip.src.main.ManagerMainActivity
import com.playground.albazip.src.main.WorkerMainActivity
import com.playground.albazip.src.onboard.manager.ManagerOnBoardingActivity
import com.playground.albazip.src.onboard.worker.WorkerOnBoardingActivity
import com.playground.albazip.util.PermissionSupport
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Base64.getEncoder


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        // 시스템바 색상 변경
        window.statusBarColor = Color.parseColor("#ffffff")
    }

    private fun permissionCheck() {
        // PermissionSupport 클래스 객체 생성
        val permission = PermissionSupport(this, this)

        // 권한 체크 후 리턴이 false 로 들어오면
        if (!permission.checkPermission()) {
            //권한 요청
            permission.requestPermission()
        } else {
            moveToPage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        val permission = PermissionSupport(this, this)

        //여기서도 리턴이 false 로 들어온다면 (사용자가 권한 허용 거부)
        if (!permission.permissionResult(requestCode, permissions, grantResults)) {
            // permission 직접 요청
            showCustomToast("필수 권한을 허용해야 서비스 정상 이용이 가능합니다.")
            finish()
        } else {
            moveToPage()
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionCheck()
    }

    private fun moveToPage() {
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
                        if (prefs.getInt("mBoardingFlags", 0) == 0) {
                            startActivity(
                                Intent(
                                    this,
                                    ManagerOnBoardingActivity::class.java
                                )
                            ) // 온보딩 화면
                        } else {
                            startActivity(Intent(this, ManagerMainActivity::class.java)) // 메인화면
                        }
                        finish()
                    }

                    // 근무자 login -> 근무자 home
                    2 -> {
                        if (prefs.getInt("wBoardingFlags", 0) == 0) {
                            startActivity(
                                Intent(
                                    this,
                                    WorkerOnBoardingActivity::class.java
                                )
                            ) // 온보딩 화면
                        } else {
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