package com.playground.albazip.src.mypage.manager.init.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.src.mypage.manager.init.data.GetMMyPageInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class MMyPageService(val view: MMyPageFragmentView) {

    fun tryGetMMyPage(){
        val mMyPageRetrofitInterface = ApplicationClass.sRetrofit.create(MMyPageRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        mMyPageRetrofitInterface.getMMyPage(token).enqueue(object :
            Callback<GetMMyPageInfoResponse> {
            override fun onResponse(call: Call<GetMMyPageInfoResponse>, response: Response<GetMMyPageInfoResponse>) {
                view.onMMyPageGetSuccess(response.body() as GetMMyPageInfoResponse)
            }

            override fun onFailure(call: Call<GetMMyPageInfoResponse>, t: Throwable) {
                view.onMMyPageGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface MMyPageRetrofitInterface {
    @GET("/mypage/manager")
    // fun getMMyPage(): Call<GetMMyPageInfoResponse>
    fun getMMyPage(@Header("token")token:String): Call<GetMMyPageInfoResponse>
}

interface MMyPageFragmentView {

    fun onMMyPageGetSuccess(response: GetMMyPageInfoResponse)

    fun onMMyPageGetFailure(message: String)
}