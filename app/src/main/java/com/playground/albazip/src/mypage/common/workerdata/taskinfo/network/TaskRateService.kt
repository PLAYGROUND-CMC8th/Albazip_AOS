package com.playground.albazip.src.mypage.common.workerdata.taskinfo.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.src.mypage.common.workerdata.taskinfo.data.GetTaskRateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class TaskRateService(val view: TaskRateFragmentView) {

    fun tryGetTaskRate(){
        val taskRateRetrofitInterface = ApplicationClass.sRetrofit.create(TaskRateRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        taskRateRetrofitInterface.getTaskRate(token).enqueue(object :
            Callback<GetTaskRateResponse> {
            override fun onResponse(call: Call<GetTaskRateResponse>, response: Response<GetTaskRateResponse>) {
                view.onTaskRateGetSuccess(response.body() as GetTaskRateResponse)
            }

            override fun onFailure(call: Call<GetTaskRateResponse>, t: Throwable) {
                view.onTaskRateGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface TaskRateRetrofitInterface {
    @GET("/mypage/myinfo/taskInfo")
    fun getTaskRate(@Header("token")token:String): Call<GetTaskRateResponse>
}

interface TaskRateFragmentView {

    fun onTaskRateGetSuccess(response: GetTaskRateResponse)

    fun onTaskRateGetFailure(message: String)
}