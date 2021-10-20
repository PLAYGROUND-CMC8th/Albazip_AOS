package com.example.albazip.ui.register.common.presenter

import android.view.View
import com.example.albazip.config.BasePresenter
import com.example.albazip.config.BaseView

interface AgreeContract {

    interface View:BaseView<Presenter> {

        // 버튼 상태
        var btnEnabled:Boolean

        // 버튼 모습 나타내기
        fun showActivateBtn()

        // 버튼 모습 숨기기
        fun showDeActivateBtn()
    }

    interface Presenter:BasePresenter{

        // 버튼 상태 저장
        fun setBtnState(btnEnabled: Boolean)

        // 버튼 상태 반환
        fun getBtnState():Boolean
    }
}