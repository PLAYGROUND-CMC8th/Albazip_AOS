package com.example.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.commute.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.src.mypage.common.workerdata.commute.data.GetCommuteInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class DetailReCommuteService(val view: DetailReCommuteFragmentView) {

    fun tryDetailReGetCommuteInfo(positionId:Int) {
        val detailReCommuteRetrofitInterface =
            ApplicationClass.sRetrofit.create(DetailReCommuteRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        detailReCommuteRetrofitInterface.getCommuteInfo(token,positionId).enqueue(object :
            Callback<GetCommuteInfoResponse> {
            override fun onResponse(
                call: Call<GetCommuteInfoResponse>,
                response: Response<GetCommuteInfoResponse>
            ) {
                view.onReCommuteGetSuccess(response.body() as GetCommuteInfoResponse)
            }

            override fun onFailure(call: Call<GetCommuteInfoResponse>, t: Throwable) {
                view.onReCommuteGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface DetailReCommuteRetrofitInterface {
    @GET("/mypage/workers/{positionId}/workerInfo/commuteInfo")
    fun getCommuteInfo(
        @Header("token") token: String,
        @Path(value = "positionId", encoded = false) positionId: Int,
    ): Call<GetCommuteInfoResponse>
}

interface DetailReCommuteFragmentView {

    fun onReCommuteGetSuccess(response: GetCommuteInfoResponse)

    fun onReCommuteGetFailure(message: String)
}