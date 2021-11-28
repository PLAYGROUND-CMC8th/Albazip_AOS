package com.example.albazip.src.home.manager.worklist.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Path

class DelTodayTaskService(val view: DelTodayTaskFragmentView) {

    fun tryGetAllWHomeInfo(taskId:Int){
        val delTodayTaskRetrofitInterface = ApplicationClass.sRetrofit.create(
            DelTodayTaskRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        delTodayTaskRetrofitInterface.delTodayTask(token,taskId).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onDelTaskSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onDelTaskFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface DelTodayTaskRetrofitInterface {
    @DELETE("/home/todayTask/{taskId}")
    fun delTodayTask(@Header("token")token:String,
                     @Path(value = "taskId", encoded = false) taskId: Int): Call<BaseResponse>
}

interface DelTodayTaskFragmentView {

    fun onDelTaskSuccess(response: BaseResponse)

    fun onDelTaskFailure(message: String)
}