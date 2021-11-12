package com.example.albazip.src.mypage.worker.init.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.src.mypage.worker.init.data.GetWMyPageInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class WMyPageService(val view: WMyPageFragmentView) {

    fun tryGetWMyPage(){
        val wMyPageRetrofitInterface = ApplicationClass.sRetrofit.create(WMyPageRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        wMyPageRetrofitInterface.getWMyPage(token).enqueue(object :
            Callback<GetWMyPageInfoResponse> {
            override fun onResponse(call: Call<GetWMyPageInfoResponse>, response: Response<GetWMyPageInfoResponse>) {
                view.onWMyPageGetSuccess(response.body() as GetWMyPageInfoResponse)
            }

            override fun onFailure(call: Call<GetWMyPageInfoResponse>, t: Throwable) {
                view.onWMyPageGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface WMyPageRetrofitInterface {
    @GET("/mypage/worker")
    // fun getMMyPage(): Call<GetMMyPageInfoResponse>
    fun getWMyPage(@Header("token")token:String): Call<GetWMyPageInfoResponse>
}

interface WMyPageFragmentView {

    fun onWMyPageGetSuccess(response: GetWMyPageInfoResponse)

    fun onWMyPageGetFailure(message: String)
}