package com.playground.albazip.src.register.common.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.src.register.common.data.remote.PhoneCheckResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhoneCheckService(val view:PhoneCheckFragmentView) {

    fun tryGetPhoneCheck(serverCheckPhoneNum:String,inputPhoneNum: String){
        val phoneCheckInterface = ApplicationClass.sRetrofit.create(PhoneCheckRetrofitInterface::class.java)
        phoneCheckInterface.getBNum(serverCheckPhoneNum).enqueue(object :
            Callback<PhoneCheckResponse> {
            override fun onResponse(
                call: Call<PhoneCheckResponse>,
                response: Response<PhoneCheckResponse>
            ) {
                view.onGetCheckSuccess(response.body() as PhoneCheckResponse,serverCheckPhoneNum,inputPhoneNum)
            }

            override fun onFailure(call: Call<PhoneCheckResponse>, t: Throwable) {
                view.onGetCheckfailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface PhoneCheckFragmentView {

    fun onGetCheckSuccess(response: PhoneCheckResponse,serverCheckPhoneNum:String,inputPhoneNum:String)

    fun onGetCheckfailure(message: String)
}