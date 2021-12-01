package com.playground.albazip.src.mypage.common.setting.editinfo.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.src.mypage.common.setting.editinfo.data.LoadInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class LoadInfoService(val view: LoadInfoFragmentView) {

    fun tryLoadInfo(){
        val loadInfoRetrofitInterface = ApplicationClass.sRetrofit.create(LoadInfoRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        loadInfoRetrofitInterface.getMyInfo(token).enqueue(object :
            Callback<LoadInfoResponse> {
            override fun onResponse(call: Call<LoadInfoResponse>, response: Response<LoadInfoResponse>) {
                view.onLoadInfoGetSuccess(response.body() as LoadInfoResponse)
            }

            override fun onFailure(call: Call<LoadInfoResponse>, t: Throwable) {
                view.onLoadInfoGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface LoadInfoRetrofitInterface {
    @GET("/mypage/setting/myinfo")
    fun getMyInfo(@Header("token")token:String): Call<LoadInfoResponse>
}

interface LoadInfoFragmentView {

    fun onLoadInfoGetSuccess(response: LoadInfoResponse)

    fun onLoadInfoGetFailure(message: String)
}