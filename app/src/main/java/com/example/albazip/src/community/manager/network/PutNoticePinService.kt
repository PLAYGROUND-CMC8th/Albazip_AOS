package com.example.albazip.src.community.manager.network

import android.widget.CheckBox
import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

class PutNoticePinService(val view: PutNoticePinListFragmentView) {

    fun tryPutNoticePin(noticeId: Int,checkBox: CheckBox){
        val putNoticePinRetrofitInterface = ApplicationClass.sRetrofit.create(PutNoticePinRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        putNoticePinRetrofitInterface.putNoticePin(token,noticeId).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onNoticePinPutSuccess(response.body() as BaseResponse,checkBox)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onNoticePinPutFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface PutNoticePinRetrofitInterface {
    @PUT("/board/notice/pin/{noticeId}")
    fun putNoticePin(@Header("token")token:String, @Path(value = "noticeId", encoded = false) noticeId: Int
    ): Call<BaseResponse>
}

interface PutNoticePinListFragmentView {

    fun onNoticePinPutSuccess(response: BaseResponse,checkBox: CheckBox)

    fun onNoticePinPutFailure(message: String)
}