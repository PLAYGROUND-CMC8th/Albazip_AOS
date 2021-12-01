package com.playground.albazip.src.mypage.worker.myInfo.network


import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.PUT

class RequestWorkOutService(val view: RequestWorkOutFragmentView) {

    fun tryPutWorkOut(){
        val requestWorkOutRetrofitInterface = ApplicationClass.sRetrofit.create(RequestWorkOutRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        requestWorkOutRetrofitInterface.putWorkOut(token).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onWorkOutPutSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onWorkOutPutFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface RequestWorkOutRetrofitInterface {
    @PUT("/mypage/myinfo/resign")
    fun putWorkOut(@Header("token")token:String): Call<BaseResponse>
}

interface RequestWorkOutFragmentView {

    fun onWorkOutPutSuccess(response: BaseResponse)

    fun onWorkOutPutFailure(message: String)
}