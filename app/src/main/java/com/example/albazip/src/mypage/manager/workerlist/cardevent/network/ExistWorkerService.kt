package com.example.albazip.src.mypage.manager.workerlist.cardevent.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.example.albazip.src.mypage.manager.init.data.BoardInfo
import com.example.albazip.src.mypage.manager.init.data.ProfileInfo
import com.example.albazip.src.mypage.manager.init.data.WorkerList
import com.example.albazip.src.mypage.manager.workerlist.cardevent.data.EmptyWorkerResponse
import com.example.albazip.src.mypage.manager.workerlist.cardevent.data.ExistWorkerResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class ExistWorkerService(val view: ExistWorkerFragmentView) {
    fun tryGetExistCard(positionId: Int){
        val existWorkerRetrofitInterface = ApplicationClass.sRetrofit.create(ExistWorkerRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        existWorkerRetrofitInterface.getExistWorker(token,positionId).enqueue(object :
            Callback<ExistWorkerResponse> {
            override fun onResponse(
                call: Call<ExistWorkerResponse>,
                response: Response<ExistWorkerResponse>
            ) {
                view.onGetSuccess(response.body() as ExistWorkerResponse)
            }

            override fun onFailure(call: Call<ExistWorkerResponse>, t: Throwable) {
                view.onGetFailure(t.message ?: "통신 오류")
            }

        })
    }
}


interface ExistWorkerRetrofitInterface {
    @GET("/mypage/workers/{positionId}")
    fun getExistWorker(@Header("token")token:String, @Path(value = "positionId", encoded = false)positionId:Int): Call<ExistWorkerResponse>
}

interface ExistWorkerFragmentView {

    fun onGetSuccess(response: ExistWorkerResponse)

    fun onGetFailure(message: String)
}


