package com.playground.albazip.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.playground.albazip.src.splash.SplashActivity


class PermissionSupport(val context: Context, val activity: Activity) {

    //요청할 권한 배열 저장

    private val permissions = arrayListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permissionsFor33 = arrayListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_MEDIA_IMAGES
    )

    private var permissionList: ArrayList<String>? = null

    //권한 요청시 발생하는 창에 대한 결과값을 받기 위해 지정해주는 int 형
    //원하는 임의의 숫자 지정
    private val MULTIPLE_PERMISSIONS = 1023 //요청에 대한 결과값 확인을 위해 RequestCode를 final로 정의

    //배열로 선언한 권한 중 허용되지 않은 권한 있는지 체크
    fun checkPermission(): Boolean {
        var result: Int
        permissionList = arrayListOf()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            for (pm in permissionsFor33) {
                result = ContextCompat.checkSelfPermission(context, pm)
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionList!!.add(pm)
                }
            }
        } else {
            for (pm in permissions) {
                result = ContextCompat.checkSelfPermission(context, pm)
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionList!!.add(pm)
                }
            }
        }

        return permissionList!!.isEmpty()
    }

    //배열로 선언한 권한에 대해 사용자에게 허용 요청
    fun requestPermission() {
        ActivityCompat.requestPermissions(
            activity,
            permissionList!!.toTypedArray<String?>(), MULTIPLE_PERMISSIONS
        )
    }

    //요청한 권한에 대한 결과값 판단 및 처리
    fun permissionResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ): Boolean {
        //우선 requestCode가 아까 위에 final로 선언하였던 숫자와 맞는지, 결과값의 길이가 0보다는 큰지 먼저 체크
        if (requestCode == MULTIPLE_PERMISSIONS && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                //grantResults 가 0이면 사용자가 허용한 것 / -1이면 거부한 것
                //-1이 있는지 체크하여 하나라도 -1이 나온다면 false를 리턴
                if (grantResults[i] == -1) {
                    return false
                }
            }
        }
        return true
    }

}