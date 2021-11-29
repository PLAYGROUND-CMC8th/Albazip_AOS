package com.example.albazip.src.community.worker.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

class PutConfirmNoticeService(val view: PutConfirmNoticeFragmentView) {

    fun tryPutNoticeRead(noticeId: Int){
        val putConfirmNoticeRetrofitInterface = ApplicationClass.sRetrofit.create(
            PutConfirmNoticeRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        putConfirmNoticeRetrofitInterface.putNoticeRead(token,noticeId).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onPutConfirmNoticeSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPutConfirmNoticeFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface PutConfirmNoticeRetrofitInterface {
    @PUT("/board/notice/{noticeId}/confirm")
    fun putNoticeRead(@Header("token")token:String, @Path(value = "noticeId", encoded = false) noticeId: Int
    ): Call<BaseResponse>
}

interface PutConfirmNoticeFragmentView {

    fun onPutConfirmNoticeSuccess(response: BaseResponse)

    fun onPutConfirmNoticeFailure(message: String)
}