package com.playground.albazip.src.community.manager.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class GetBoardNoticeService(val view: GetBoardListFragmentView) {

    fun tryGetBoardList(){
        val getBoardListRetrofitInterface = ApplicationClass.sRetrofit.create(GetBoardListRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        getBoardListRetrofitInterface.getBoardList(token).enqueue(object :
            Callback<GetBoardListResponse> {
            override fun onResponse(call: Call<GetBoardListResponse>, response: Response<GetBoardListResponse>) {
                view.onBoardListGetSuccess(response.body() as GetBoardListResponse)
            }

            override fun onFailure(call: Call<GetBoardListResponse>, t: Throwable) {
                view.onBoardListGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface GetBoardListRetrofitInterface {
    @GET("/board/notice")
    fun getBoardList(@Header("token")token:String): Call<GetBoardListResponse>
}

interface GetBoardListFragmentView {

    fun onBoardListGetSuccess(response: GetBoardListResponse)

    fun onBoardListGetFailure(message: String)
}

data class GetBoardListResponse(
    @SerializedName("data")val data:ArrayList<CommuNoticeInfo>
):BaseResponse()

data class CommuNoticeInfo(
    @SerializedName("id")val id:Int,
    @SerializedName("pin")val pin:Int,
    @SerializedName("title")val title:String,
    @SerializedName("registerDate")val registerDate:String,
    @SerializedName("confirm")val confirm:Int,
)
