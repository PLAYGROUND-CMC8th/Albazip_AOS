package com.playground.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.taskinfo.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.src.mypage.common.workerdata.taskinfo.data.GetDayTaskResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class DetailDayTaskService(val view: DetailDayTaskFragmentView) {

    fun tryGetDayTask(positionId: Int,year: String,month: String,date:String) {
        val detailDayTaskRetrofitInterface =
            ApplicationClass.sRetrofit.create(DetailDayTaskRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        detailDayTaskRetrofitInterface.getDetailDayTask(token,positionId,year,month,date).enqueue(object :
            Callback<GetDayTaskResponse> {
            override fun onResponse(
                call: Call<GetDayTaskResponse>,
                response: Response<GetDayTaskResponse>
            ) {
                view.onDetailDayTaskGetSuccess(response.body() as GetDayTaskResponse)
            }

            override fun onFailure(call: Call<GetDayTaskResponse>, t: Throwable) {
                view.onDetailDayTaskGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface DetailDayTaskRetrofitInterface {
    @GET("/mypage/workers/{positionId}/workerInfo/taskInfo/{year}/{month}/{date}")
    fun getDetailDayTask(
        @Header("token") token: String,
        @Path(value = "positionId", encoded = false) positionId: Int,
        @Path(value = "year", encoded = false) year: String,
        @Path(value = "month", encoded = false) month: String,
        @Path(value = "date", encoded = false) date: String
    ): Call<GetDayTaskResponse>
}

interface DetailDayTaskFragmentView {

    fun onDetailDayTaskGetSuccess(response: GetDayTaskResponse)

    fun onDetailDayTaskGetFailure(message: String)
}