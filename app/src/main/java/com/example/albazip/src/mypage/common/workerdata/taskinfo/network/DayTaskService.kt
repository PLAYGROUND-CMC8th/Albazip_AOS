package com.example.albazip.src.mypage.common.workerdata.taskinfo.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.src.mypage.common.workerdata.taskinfo.data.GetDayTaskResponse
import com.example.albazip.src.mypage.common.workerdata.taskinfo.data.GetMonthTaskRateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class DayTaskService(val view: DayTaskFragmentView) {

    fun tryGetDayTask(year: String,month: String,date:String) {
        val dayTaskRetrofitInterface =
            ApplicationClass.sRetrofit.create(DayTaskRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        dayTaskRetrofitInterface.getDayTask(token,year,month,date).enqueue(object :
            Callback<GetDayTaskResponse> {
            override fun onResponse(
                call: Call<GetDayTaskResponse>,
                response: Response<GetDayTaskResponse>
            ) {
                view.onDayTaskGetSuccess(response.body() as GetDayTaskResponse)
            }

            override fun onFailure(call: Call<GetDayTaskResponse>, t: Throwable) {
                view.onDayTaskGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface DayTaskRetrofitInterface {
    @GET("/mypage/myinfo/taskInfo/{year}/{month}/{date}")
    fun getDayTask(
        @Header("token") token: String, @Path(value = "year", encoded = false) year: String,
        @Path(value = "month", encoded = false) month: String,
        @Path(value = "date", encoded = false) date: String
    ): Call<GetDayTaskResponse>
}

interface DayTaskFragmentView {

    fun onDayTaskGetSuccess(response: GetDayTaskResponse)

    fun onDayTaskGetFailure(message: String)
}