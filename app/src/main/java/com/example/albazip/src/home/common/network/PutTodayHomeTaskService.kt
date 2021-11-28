package com.example.albazip.src.home.common.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.example.albazip.src.home.common.data.HomeCoWorkResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

class PutTodayHomeTaskService(val view: PutTodayHomeTaskFragmentView) {

    fun tryPutTodayTask(taskId: Int){
        val putTodayTaskRetrofitInterface = ApplicationClass.sRetrofit.create(
            PutTodayTaskRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        putTodayTaskRetrofitInterface.putTodayTask(token,taskId).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onPutTodayTaskSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPutTodayTaskFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface PutTodayTaskRetrofitInterface {
    @PUT("/home/todayTask/{taskId}")
    fun putTodayTask(@Header("token")token:String,
                     @Path(value = "taskId", encoded = false) taskId: Int): Call<BaseResponse>
}

interface PutTodayHomeTaskFragmentView {

    fun onPutTodayTaskSuccess(response: BaseResponse)

    fun onPutTodayTaskFailure(message: String)
}