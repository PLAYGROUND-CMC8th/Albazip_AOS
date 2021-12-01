package com.playground.albazip.src.mypage.manager.workerlist.outworker.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Path

class ResponseWorkOutService(val view: ResponseWorkOutFragmentView) {

    fun tryPutWorkOut(positionId: Int){
        val responseWorkOutRetrofitInterface = ApplicationClass.sRetrofit.create(
            ResponseWorkOutRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        responseWorkOutRetrofitInterface.delWorkOut(token,positionId).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onWorkOutDelSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onWorkOutDelFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface ResponseWorkOutRetrofitInterface {
    @DELETE("/mypage/workers/{positionId}")
    fun delWorkOut(@Header("token")token:String,@Path(value = "positionId", encoded = false) positionId: Int): Call<BaseResponse>
}

interface ResponseWorkOutFragmentView {

    fun onWorkOutDelSuccess(response: BaseResponse)

    fun onWorkOutDelFailure(message: String)
}
