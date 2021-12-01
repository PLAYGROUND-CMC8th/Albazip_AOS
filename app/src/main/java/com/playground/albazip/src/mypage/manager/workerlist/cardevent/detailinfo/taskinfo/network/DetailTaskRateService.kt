package com.playground.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.taskinfo.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.src.mypage.common.workerdata.taskinfo.data.GetTaskRateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class DetailTaskRateService(val view: DetailTaskRateFragmentView) {

    fun tryGetDetailTaskRate(positionId: Int) {
        val detailTaskRateRetrofitInterface =
            ApplicationClass.sRetrofit.create(DetailTaskRateRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        detailTaskRateRetrofitInterface.getTaskRate(token,positionId).enqueue(object :
            Callback<GetTaskRateResponse> {
            override fun onResponse(
                call: Call<GetTaskRateResponse>,
                response: Response<GetTaskRateResponse>
            ) {
                view.onTaskRateGetSuccess(response.body() as GetTaskRateResponse)
            }

            override fun onFailure(call: Call<GetTaskRateResponse>, t: Throwable) {
                view.onTaskRateGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface DetailTaskRateRetrofitInterface {
    @GET("/mypage/workers/{positionId}/workerInfo/taskInfo")
    fun getTaskRate(
        @Header("token") token: String,
        @Path(value = "positionId", encoded = false) positionId: Int,
    ): Call<GetTaskRateResponse>
}

interface DetailTaskRateFragmentView {

    fun onTaskRateGetSuccess(response: GetTaskRateResponse)

    fun onTaskRateGetFailure(message: String)
}