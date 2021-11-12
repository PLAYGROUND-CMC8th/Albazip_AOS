package com.example.albazip.src.mypage.manager.workerlist.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.src.mypage.manager.workerlist.data.remote.GetWorkerListResponse
import com.example.albazip.src.mypage.manager.workerlist.data.remote.WorkerListData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header


class WorkerListService(val view: WorkListFragmentView) {
    fun tryGetWorkerList(){
        val workerListInterface = ApplicationClass.sRetrofit.create(WorkerListRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        workerListInterface.getWorkerList(token).enqueue(object :
            Callback<GetWorkerListResponse> {
            override fun onResponse(
                call: Call<GetWorkerListResponse>,
                response: Response<GetWorkerListResponse>
            ) {
                view.onGetSuccess(response.body() as GetWorkerListResponse)
            }

            override fun onFailure(call: Call<GetWorkerListResponse>, t: Throwable) {
               view.onGetFailure(t.message ?: "통신 오류")
            }

        })
    }
}


interface WorkerListRetrofitInterface {
    @GET("/mypage/workers")
    fun getWorkerList(@Header("token")token:String): Call<GetWorkerListResponse>
}

interface WorkListFragmentView {

    fun onGetSuccess(response: GetWorkerListResponse)

    fun onGetFailure(message: String)
}


