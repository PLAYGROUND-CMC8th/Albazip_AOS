package com.example.albazip.ui.register.common.presenter

import android.view.View

class AgreePresenter(val agreeView: AgreeContract.View):AgreeContract.Presenter {


    override fun setBtnState(btnEnabled: Boolean) {
        agreeView.btnEnabled = btnEnabled
    }

    override fun getBtnState():Boolean {
        return agreeView.btnEnabled
    }


    override fun start() {

    }

}