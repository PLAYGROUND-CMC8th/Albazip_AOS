package com.playground.albazip.src.home.worker.network

import com.google.gson.annotations.SerializedName
import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.src.home.worker.data.GetAllWHomeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class GetWorkerRemainService(val view: GetWorkerRemainFragmentView) {

    fun tryGetWorkerTimeInfo(){
        val getWorkerRemainRetrofitInterface = ApplicationClass.sRetrofit.create(
            GetWorkerRemainRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        getWorkerRemainRetrofitInterface.getWorkerTime(token).enqueue(object :
            Callback<WorkerRemainResult> {
            override fun onResponse(call: Call<WorkerRemainResult>, response: Response<WorkerRemainResult>) {
                view.onGetWorkerTimeSuccess(response.body() as WorkerRemainResult)
            }

            override fun onFailure(call: Call<WorkerRemainResult>, t: Throwable) {
                view.onGetWorkerTimeFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface GetWorkerRemainRetrofitInterface {
    @GET("/home/worker/remainTime")
    fun getWorkerTime(@Header("token")token:String): Call<WorkerRemainResult>
}

interface GetWorkerRemainFragmentView {

    fun onGetWorkerTimeSuccess(response: WorkerRemainResult)

    fun onGetWorkerTimeFailure(message: String)
}

data class WorkerRemainResult(
    @SerializedName("data")val data:String
):BaseResponse()