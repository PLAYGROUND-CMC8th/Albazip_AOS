package com.example.albazip.src.mypage.worker.board.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.src.mypage.manager.workerlist.data.remote.GetWorkerListResponse
import com.example.albazip.src.mypage.manager.workerlist.network.WorkerListRetrofitInterface
import com.example.albazip.src.mypage.worker.board.data.GetWorkerBoardResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class WorkerBoardService(val view: WorkBoardFragmentView) {
    fun tryGetSingleWorkerList(){
        val workerBoardinterface = ApplicationClass.sRetrofit.create(WorkerBoardRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        workerBoardinterface.getSingleWorkerList(token).enqueue(object :
            Callback<GetWorkerBoardResponse> {
            override fun onResponse(
                call: Call<GetWorkerBoardResponse>,
                response: Response<GetWorkerBoardResponse>
            ) {
                view.onGetSuccess(response.body() as GetWorkerBoardResponse)
            }

            override fun onFailure(call: Call<GetWorkerBoardResponse>, t: Throwable) {
                view.onGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}


interface WorkerBoardRetrofitInterface {
    @GET("/mypage/boards/worker")
    fun getSingleWorkerList(@Header("token")token:String): Call<GetWorkerBoardResponse>
}

interface WorkBoardFragmentView {

    fun onGetSuccess(response: GetWorkerBoardResponse)

    fun onGetFailure(message: String)
}

