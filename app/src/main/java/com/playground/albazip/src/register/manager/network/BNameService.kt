package com.playground.albazip.src.register.manager.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.src.register.manager.data.remote.BNameResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BNameService(val view: BNameFragmentView) {

    fun tryGetBName(registerNumber:String,ownerName:String){
        val bnameInterface = ApplicationClass.sRetrofit.create(BNameRetrofitInterface::class.java)
        bnameInterface.getBName(registerNumber,ownerName).enqueue(object :
            Callback<BNameResponse> {
            override fun onResponse(call: Call<BNameResponse>, response: Response<BNameResponse>) {
                view.onGetBNameSuccess(response.body() as BNameResponse)
            }

            override fun onFailure(call: Call<BNameResponse>, t: Throwable) {
                view.onGetBNameFailure(t.message ?: "통신 오류")
            }

        })
    }
}

interface BNameFragmentView {

    fun onGetBNameSuccess(response: BNameResponse)

    fun onGetBNameFailure(message: String)
}