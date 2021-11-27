package com.example.albazip.src.home.manager.opened.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class GetTodaysWorkerService(val view: GetTodayWorkerFragmentView) {

    fun tryGetTodayWorkers(){
        val getTodayWorkerRetrofitInterface = ApplicationClass.sRetrofit.create(GetTodayWorkerRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        getTodayWorkerRetrofitInterface.getShopList(token).enqueue(object :
            Callback<GetTodayWorkerResponse> {
            override fun onResponse(call: Call<GetTodayWorkerResponse>, response: Response<GetTodayWorkerResponse>) {
                view.onGetTodayWorkersSuccess(response.body() as GetTodayWorkerResponse)
            }

            override fun onFailure(call: Call<GetTodayWorkerResponse>, t: Throwable) {
                view.onGetTodayWorkersFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface GetTodayWorkerRetrofitInterface {
    @GET("/home/todayWorkers")
    fun getShopList(@Header("token")token:String): Call<GetTodayWorkerResponse>
}

interface GetTodayWorkerFragmentView {

    fun onGetTodayWorkersSuccess(response: GetTodayWorkerResponse)

    fun onGetTodayWorkersFailure(message: String)
}

data class GetTodayWorkerResponse(
    @SerializedName("data")val data:ArrayList<TodayWorkerResult>
):BaseResponse()

data class TodayWorkerResult(
    @SerializedName("workerId")val workerId:Int,
    @SerializedName("workerTitle")val workerTitle:String,
    @SerializedName("workerName")val workerName:String,
    @SerializedName("workerImage")val workerImage:String?,
)