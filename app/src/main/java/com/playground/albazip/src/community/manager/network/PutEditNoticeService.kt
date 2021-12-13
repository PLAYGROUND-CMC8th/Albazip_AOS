package com.playground.albazip.src.community.manager.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

class PutEditNoticeService(val view: PutEditNoticeFragmentView) {

    fun tryPutEditNotice(noticeId: Int,title: RequestBody, pin:RequestBody, content: RequestBody, images: ArrayList<MultipartBody.Part>
    ){
        val putEditNoticeRetrofitInterface = ApplicationClass.sRetrofit.create(
            PutEditNoticeRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        putEditNoticeRetrofitInterface.postBoardNotice(token,noticeId,title,pin,content,images).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onPutBoardNoticeSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPutBoardNoticeFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface PutEditNoticeRetrofitInterface {
    @Multipart
    @PUT("/board/notice/{noticeId}")
    fun postBoardNotice(@Header("token")token:String,
                        @Path(value = "noticeId", encoded = false) noticeId: Int,
                        @Part("title")title: RequestBody,
                        @Part("pin")pin: RequestBody,
                        @Part("content")content: RequestBody,
                        @Part images:ArrayList<MultipartBody.Part>): Call<BaseResponse>
}

interface PutEditNoticeFragmentView {

    fun onPutBoardNoticeSuccess(response: BaseResponse)

    fun onPutBoardNoticeFailure(message: String)
}