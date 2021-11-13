package com.example.albazip.src.mypage.worker.myInfo.network

import GetWorkerMyInfoResponse
import com.example.albazip.config.ApplicationClass
import com.example.albazip.src.mypage.worker.init.data.GetWMyPageInfoResponse
import com.example.albazip.src.mypage.worker.init.network.WMyPageFragmentView
import com.example.albazip.src.mypage.worker.init.network.WMyPageRetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class WorkerMyInfoService(val view: WorkerInfoFragmentView) {

    fun tryGetWMyPage(){
        val workerMyInfoRetrofitInterface = ApplicationClass.sRetrofit.create(WorkerMyInfoRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        workerMyInfoRetrofitInterface.getWorkerInfo(token).enqueue(object :
            Callback<GetWorkerMyInfoResponse> {
            override fun onResponse(call: Call<GetWorkerMyInfoResponse>, response: Response<GetWorkerMyInfoResponse>) {
                view.onMyInfoGetSuccess(response.body() as GetWorkerMyInfoResponse)
            }

            override fun onFailure(call: Call<GetWorkerMyInfoResponse>, t: Throwable) {
                view.onMyInfoGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface WorkerMyInfoRetrofitInterface {
    @GET("/mypage/myinfo")
    fun getWorkerInfo(@Header("token")token:String): Call<GetWorkerMyInfoResponse>
}

interface WorkerInfoFragmentView {

    fun onMyInfoGetSuccess(response: GetWorkerMyInfoResponse)

    fun onMyInfoGetFailure(message: String)
}