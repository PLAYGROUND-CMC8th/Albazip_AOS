package com.example.albazip.src.mypage.common.workerdata.commute.network

import com.example.albazip.src.mypage.common.workerdata.commute.data.GetCommuteInfoResponse
import retrofit2.http.Path
import com.example.albazip.config.ApplicationClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class CommuteService(val view: CommuteFragmentView) {

    fun tryGetCommuteInfo(year: String,month: String) {
        val commuteRetrofitInterface =
            ApplicationClass.sRetrofit.create(CommuteRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        commuteRetrofitInterface.getCommuteInfo(token,year,month).enqueue(object :
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

interface CommuteRetrofitInterface {
    @GET("/mypage/myinfo/commuteInfo/{year}/{month}")
    fun getCommuteInfo(
        @Header("token") token: String,
        @Path(value = "year", encoded = false) year: String,
        @Path(value = "month", encoded = false) month: String
    ): Call<GetCommuteInfoResponse>
}

interface CommuteFragmentView {

    fun onCommuteGetSuccess(response: GetCommuteInfoResponse)

    fun onCommuteGetFailure(message: String)
}