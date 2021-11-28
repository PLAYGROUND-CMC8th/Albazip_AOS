package com.example.albazip.src.home.manager.worklist.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.example.albazip.src.home.worker.opened.worklist.network.GetWPerTaskResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class GetPositionPerWorkService(val view: GetPositionPerFragmentView) {

    fun tryGetPositionPerWorkInfo(workerId: Int){
        val getPositionPerRetrofitInterface = ApplicationClass.sRetrofit.create(
            GetPositionPerRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        getPositionPerRetrofitInterface.getPositionPerInfo(token,workerId).enqueue(object :
            Callback<GetWPerTaskResponse> {
            override fun onResponse(call: Call<GetWPerTaskResponse>, response: Response<GetWPerTaskResponse>) {
                view.onGetMTaskSuccess(response.body() as GetWPerTaskResponse)
            }

            override fun onFailure(call: Call<GetWPerTaskResponse>, t: Throwable) {
                view.onGetMTaskFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface GetPositionPerRetrofitInterface {
    @GET("/home/todayTask/manager/workerPerTask/{workerId}")
    fun getPositionPerInfo(@Header("token")token:String, @Path(value = "workerId", encoded = false) workerId: Int,): Call<GetWPerTaskResponse>
}

interface GetPositionPerFragmentView {

    fun onGetMTaskSuccess(response: GetWPerTaskResponse)

    fun onGetMTaskFailure(message: String)
}

data class GetPositionPerResponse(
    @SerializedName("data")val data: GetWPerTaskResponse
):BaseResponse()
