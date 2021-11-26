package com.example.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.src.mypage.common.workerdata.commute.data.GetCommuteInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class DetailCommuteService(val view: DetailCommuteFragmentView) {

    fun tryDetailGetCommuteInfo(positionId: Int, year: String,month: String) {
        val detailCommuteRetrofitInterface =
            ApplicationClass.sRetrofit.create(DetailCommuteRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        detailCommuteRetrofitInterface.getCommuteInfo(token,positionId,year,month).enqueue(object :
            Callback<GetCommuteInfoResponse> {
            override fun onResponse(
                call: Call<GetCommuteInfoResponse>,
                response: Response<GetCommuteInfoResponse>
            ) {
                view.onCommuteGetSuccess(response.body() as GetCommuteInfoResponse)
            }

            override fun onFailure(call: Call<GetCommuteInfoResponse>, t: Throwable) {
                view.onCommuteGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface DetailCommuteRetrofitInterface {
    @GET("/mypage/workers/{positionId}/workerInfo/commuteInfo/{year}/{month}")
    fun getCommuteInfo(
        @Header("token") token: String,
        @Path(value = "positionId", encoded = false) positionId: Int,
        @Path(value = "year", encoded = false) year: String,
        @Path(value = "month", encoded = false) month: String
    ): Call<GetCommuteInfoResponse>
}

interface DetailCommuteFragmentView {

    fun onCommuteGetSuccess(response: GetCommuteInfoResponse)

    fun onCommuteGetFailure(message: String)
}