package com.example.albazip.src.community.manager.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

class PostBoardNoticeService(val view: PostBoardNoticeFragmentView) {

    fun tryPutNoticeReport(postBoardNoticeRequest: PostBoardNoticeRequest){
        val postBoardNoticeRetrofitInterface = ApplicationClass.sRetrofit.create(
            PostBoardNoticeRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        postBoardNoticeRetrofitInterface.postBoardNotice(token,postBoardNoticeRequest).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onPostBoardNoticeSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPostBoardNoticeFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface PostBoardNoticeRetrofitInterface {
    @POST("/board/notice")
    fun postBoardNotice(@Header("token")token:String, @Body params: PostBoardNoticeRequest): Call<BaseResponse>
}

interface PostBoardNoticeFragmentView {

    fun onPostBoardNoticeSuccess(response: BaseResponse)

    fun onPostBoardNoticeFailure(message: String)
}

data class PostBoardNoticeRequest(
    @SerializedName("title")val title:String,
    @SerializedName("content")val content:String,
    @SerializedName("image")val image:ArrayList<MultipartBody.Part?>,
)