package com.example.albazip.src.mypage.manager.workerlist.cardevent.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class PWorkListService(val view: PWorkListFragmentView) {
    fun tryGetWorkList(positionId: Int){
        val pWorkListRetrofitInterface = ApplicationClass.sRetrofit.create(PWorkListRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        pWorkListRetrofitInterface.getPWorkList(token,positionId).enqueue(object :
            Callback<PWorkResponse> {
            override fun onResponse(
                call: Call<PWorkResponse>,
                response: Response<PWorkResponse>
            ) {
                view.onGetWorkListSuccess(response.body() as PWorkResponse)
            }

            override fun onFailure(call: Call<PWorkResponse>, t: Throwable) {
                view.onGetWorkListFailure(t.message ?: "통신 오류")
            }

        })
    }
}


interface PWorkListRetrofitInterface {
    @GET("/mypage/workers/{positionId}/taskList")
    fun getPWorkList(@Header("token")token:String, @Path(value = "positionId", encoded = false)positionId:Int): Call<PWorkResponse>
}

interface PWorkListFragmentView {

    fun onGetWorkListSuccess(response: PWorkResponse)

    fun onGetWorkListFailure(message: String)
}

data class PWorkResponse(
    @SerializedName("data") val data: ArrayList<PWorkListData>,
):BaseResponse()


data class PWorkListData(
    @SerializedName("id") val id: Int,
    @SerializedName("writerTitle") val writerTitle: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("registerDate") val registerDate: String,
    @SerializedName("writerName") val writerName: String
)