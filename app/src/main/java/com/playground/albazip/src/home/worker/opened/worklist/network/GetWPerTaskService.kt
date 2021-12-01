package com.playground.albazip.src.home.worker.opened.worklist.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class GetWPerTaskService(val view: GetWPerTaskFragmentView) {

    fun tryGetPerTask(){
        val getWPerTaskRetrofitInterface = ApplicationClass.sRetrofit.create(
            GetWPerTaskRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        getWPerTaskRetrofitInterface.getWPerTask(token).enqueue(object :
            Callback<GetWPerTaskResponse> {
            override fun onResponse(call: Call<GetWPerTaskResponse>, response: Response<GetWPerTaskResponse>) {
                view.onGetWPerTaskSuccess(response.body() as GetWPerTaskResponse)
            }

            override fun onFailure(call: Call<GetWPerTaskResponse>, t: Throwable) {
                view.onGetWPerTaskFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface GetWPerTaskRetrofitInterface {
    @GET("/home/todayTask/worker/perTask")
    fun getWPerTask(@Header("token")token:String): Call<GetWPerTaskResponse>
}

interface GetWPerTaskFragmentView {

    fun onGetWPerTaskSuccess(response: GetWPerTaskResponse)

    fun onGetWPerTaskFailure(message: String)
}

data class GetWPerTaskResponse(
    @SerializedName("data")val data:WPerTaskResult
):BaseResponse()

data class WPerTaskResult(
    @SerializedName("positionTitle")val positionTitle:String,
    @SerializedName("nonComPerTask")val nonComPerTask:ArrayList<NonComPerTask>,
    @SerializedName("compPerTask")val compPerTask:ArrayList<ComPerTask>,
)