package com.playground.albazip.util

import android.app.Activity
import android.content.Context
import android.widget.Toast

// 뒤로가기 버튼 핸들러(종료 알림)
class BackPressCloseHandler(context: Context) {

    var context = context
    private var backKeyPressedTime: Long = 0

    var toast: Toast = Toast.makeText(context, "\'뒤로\'버튼을 한번 더 누르면,앱이 종료됩니다.", Toast.LENGTH_SHORT)

    fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            showGuideToast()
            return
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            (context as Activity).finish()
            toast.cancel()
        }
    }

    fun showGuideToast() {
        toast.show()
    }
}