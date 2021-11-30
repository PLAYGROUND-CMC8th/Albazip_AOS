package com.playground.albazip.src.mypage.manager.workerlist.cardevent.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.src.mypage.manager.workerlist.cardevent.data.EmptyWorkerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class EmptyWorkerService(val view: EmptyWorkerFragmentView) {
    fun tryGetEmptyCard(positionId: Int){
        val emptyWorkerRetrofitInterface = ApplicationClass.sRetrofit.create(EmptyWorkerRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        emptyWorkerRetrofitInterface.getEmptyWorker(token,positionId).enqueue(object :
            Callback<EmptyWorkerResponse> {
            override fun onResponse(
                call: Call<EmptyWorkerResponse>,
                response: Response<EmptyWorkerResponse>
            ) {
                view.onGetSuccess(response.body() as EmptyWorkerResponse)
            }

            override fun onFailure(call: Call<EmptyWorkerResponse>, t: Throwable) {
                view.onGetFailure(t.message ?: "통신 오류")
            }

        })
    }
}


interface EmptyWorkerRetrofitInterface {
    @GET("/mypage/workers/ne/{positionId}")
    fun getEmptyWorker(@Header("token")token:String,@Path(value = "positionId", encoded = false)positionId:Int): Call<EmptyWorkerResponse>
}

interface EmptyWorkerFragmentView {

    fun onGetSuccess(response: EmptyWorkerResponse)

    fun onGetFailure(message: String)
}
