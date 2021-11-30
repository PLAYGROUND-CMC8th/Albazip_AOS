package com.playground.albazip.src.community.worker.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

class PostNoticeReportService(val view: PutNoticeReportFragmentView) {

    fun tryPutNoticeReport(noticeReportRequest:NoticeReportRequest){
        val putNoticeRetrofitInterface = ApplicationClass.sRetrofit.create(
            PutNoticeRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        putNoticeRetrofitInterface.putNoticeReport(token,noticeReportRequest).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onPutReportSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPutReportFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface PutNoticeRetrofitInterface {
    @POST("/board/notice/report")
    fun putNoticeReport(@Header("token")token:String, @Body params: NoticeReportRequest): Call<BaseResponse>
}

interface PutNoticeReportFragmentView {

    fun onPutReportSuccess(response: BaseResponse)

    fun onPutReportFailure(message: String)
}

data class NoticeReportRequest(
    @SerializedName("noticeId")val noticeId:Int,
    @SerializedName("reportReason")val reportReason:String
)