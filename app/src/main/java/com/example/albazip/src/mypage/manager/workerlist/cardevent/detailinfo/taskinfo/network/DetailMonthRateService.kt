package com.example.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.taskinfo.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.src.mypage.common.workerdata.taskinfo.data.GetMonthTaskRateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class DetailMonthRateService(val view: DetailMonthRateFragmentView) {

    fun tryGetTaskRate(positionId: Int,year: String,month: String) {
        val detailMonthRateRetrofitInterface =
            ApplicationClass.sRetrofit.create(DetailMonthRateRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        detailMonthRateRetrofitInterface.getMonthTaskRate(token,positionId,year,month).enqueue(object :
            Callback<GetMonthTaskRateResponse> {
            override fun onResponse(
                call: Call<GetMonthTaskRateResponse>,
                response: Response<GetMonthTaskRateResponse>
            ) {
                view.onMonthRateGetSuccess(response.body() as GetMonthTaskRateResponse)
            }

            override fun onFailure(call: Call<GetMonthTaskRateResponse>, t: Throwable) {
                view.onMonthRateGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface DetailMonthRateRetrofitInterface {
    @GET("/mypage/workers/{positionId}/workerInfo/taskInfo/{year}/{month}")
    fun getMonthTaskRate(
        @Header("token") token: String,
        @Path(value = "positionId", encoded = false) positionId: Int,
        @Path(value = "year", encoded = false) year: String,
        @Path(value = "month", encoded = false) month: String
    ): Call<GetMonthTaskRateResponse>
}

interface DetailMonthRateFragmentView {

    fun onMonthRateGetSuccess(response: GetMonthTaskRateResponse)

    fun onMonthRateGetFailure(message: String)
}