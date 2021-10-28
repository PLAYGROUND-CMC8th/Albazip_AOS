package com.example.albazip.src.register.manager.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.src.register.manager.data.remote.BNumResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BNumService(val view: BNumFragmentView)  {

    fun tryGetBNum(registerNumber: String){
        val bnumInterface = ApplicationClass.sRetrofit.create(BNumRetrofitInterface::class.java)
        bnumInterface.getBNum(registerNumber).enqueue(object :
            Callback<BNumResponse> {
            override fun onResponse(call: Call<BNumResponse>, response: Response<BNumResponse>) {
                view.onGetBNumSuccess(response.body() as BNumResponse)
            }

            override fun onFailure(call: Call<BNumResponse>, t: Throwable) {
                view.onGetBNumFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface BNumFragmentView {

    fun onGetBNumSuccess(response: BNumResponse)

    fun onGetBNumFailure(message: String)
}