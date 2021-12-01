package com.playground.albazip.src.community.manager.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Path

class DelNoticeBoardService(val view: DelNoticeBoardListFragmentView) {

    fun tryDelNoticeBoardList(noticeId: Int){
        val delNoticeBoardListRetrofitInterface = ApplicationClass.sRetrofit.create(DelNoticeBoardListRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        delNoticeBoardListRetrofitInterface.delNoticeBoardList(token,noticeId).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onDelNoticeGetSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onDelNoticeGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface DelNoticeBoardListRetrofitInterface {
    @DELETE("/board/notice/{noticeId}")
    fun delNoticeBoardList(@Header("token")token:String,@Path(value = "noticeId", encoded = false) noticeId: Int): Call<BaseResponse>
}

interface DelNoticeBoardListFragmentView {

    fun onDelNoticeGetSuccess(response: BaseResponse)

    fun onDelNoticeGetFailure(message: String)
}