package com.playground.albazip.src.withdraw.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Header


class WithDrawService(val view: WithDrawFragmentView) {

    fun tryDeletePersonInfo() {
        val withDrawRetrofitInterface =
            ApplicationClass.sRetrofit.create(WithDrawRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        withDrawRetrofitInterface.deletePersonInfo(token).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                view.onDeleteSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onDeleteFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface WithDrawRetrofitInterface {
    @DELETE("/mypage/setting")
    fun deletePersonInfo(
        @Header("token") token: String,
    ): Call<BaseResponse>
}

interface WithDrawFragmentView {

    fun onDeleteSuccess(response: BaseResponse)

    fun onDeleteFailure(message: String)
}