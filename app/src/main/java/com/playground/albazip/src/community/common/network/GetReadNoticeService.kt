package com.playground.albazip.src.community.common.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class GetReadNoticeService(val view: GetReadNoticeFragmentView) {

    fun tryGetNoticeRead(noticeId: Int){
        val getReadNoticeRetrofitInterface = ApplicationClass.sRetrofit.create(
            GetReadNoticeRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        getReadNoticeRetrofitInterface.getNoticeRead(token,noticeId).enqueue(object :
            Callback<ReadNoticeResponse> {
            override fun onResponse(call: Call<ReadNoticeResponse>, response: Response<ReadNoticeResponse>) {
                view.onGetReadNoticeSuccess(response.body() as ReadNoticeResponse)
            }

            override fun onFailure(call: Call<ReadNoticeResponse>, t: Throwable) {
                view.onGetReadNoticeFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface GetReadNoticeRetrofitInterface {
    @GET("/board/notice/{noticeId}")
    fun getNoticeRead(@Header("token")token:String, @Path(value = "noticeId", encoded = false) noticeId: Int
    ): Call<ReadNoticeResponse>
}

interface GetReadNoticeFragmentView {

    fun onGetReadNoticeSuccess(response: ReadNoticeResponse)

    fun onGetReadNoticeFailure(message: String)
}

data class ReadNoticeResponse(
    @SerializedName("data")val data:NoticeReadResult
):BaseResponse()

data class NoticeReadResult(
    @SerializedName("writerInfo")val writerInfo:NoticeWriterInfo,
    @SerializedName("boardInfo")val boardInfo:NoticeBoardInfo,
    @SerializedName("confirmInfo")val confirmInfo:NoticeConfirmInfo,
)

data class NoticeWriterInfo(
    @SerializedName("title")val title:String,
    @SerializedName("name")val name:String,
    @SerializedName("image")val image:String,
)

data class NoticeBoardInfo(
    @SerializedName("title")val title:String,
    @SerializedName("content")val content:String,
    @SerializedName("registerDate")val registerDate:String,
    @SerializedName("image")val image:ArrayList<BoardImage>,
)

data class BoardImage(
    @SerializedName("id")val id:Int,
    @SerializedName("image_path")val image_path:String,
)

data class NoticeConfirmInfo(
    @SerializedName("count")val count:Int,
    @SerializedName("confirmer")val confirmer:ArrayList<NoticeConfirmerInfo>
)

data class NoticeConfirmerInfo(
    @SerializedName("writerTitle")val writerTitle:String,
    @SerializedName("writerName")val writerName:String,
    @SerializedName("writerImage")val writerImage:String,
)
