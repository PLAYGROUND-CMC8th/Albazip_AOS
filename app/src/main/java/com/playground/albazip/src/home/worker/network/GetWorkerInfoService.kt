package com.playground.albazip.src.home.worker.network

import com.google.gson.annotations.SerializedName
import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class GetWorkerInfoService(val view: GetWorkerInfoWFragmentView) {

    fun tryGetWorkerInfo(){
        val getWorkerInfoWRetrofitInterface = ApplicationClass.sRetrofit.create(
            GetWorkerInfoWRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        getWorkerInfoWRetrofitInterface.getWorkerInfo(token).enqueue(object :
            Callback<WorkerInfoResponse> {
            override fun onResponse(call: Call<WorkerInfoResponse>, response: Response<WorkerInfoResponse>) {
                view.onGetWorkerInfoSuccess(response.body() as WorkerInfoResponse)
            }

            override fun onFailure(call: Call<WorkerInfoResponse>, t: Throwable) {
                view.onGetWorkerInfoFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface GetWorkerInfoWRetrofitInterface {
    @GET("/mypage/profile")
    fun getWorkerInfo(@Header("token")token:String): Call<WorkerInfoResponse>
}

interface GetWorkerInfoWFragmentView {

    fun onGetWorkerInfoSuccess(response: WorkerInfoResponse)

    fun onGetWorkerInfoFailure(message: String)
}

data class WorkerInfoResponse(
    @SerializedName("data")val data:WorkerInfoResult
):BaseResponse()

data class WorkerInfoResult(
    @SerializedName("firstName")val firstName:String,
)