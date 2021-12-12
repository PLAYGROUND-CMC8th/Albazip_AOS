package com.playground.albazip.src.mypage.manager.workerlist.cardevent.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

class PutRejectWorkerOutService(val view: PutRejectWorkerOutFragmentView) {
    fun tryPutRejectOut(positionId: Int){
        val putRejectWorkerRetrofitInterface = ApplicationClass.sRetrofit.create(PutRejectWorkerRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        putRejectWorkerRetrofitInterface.putRejectOut(token,positionId).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                view.onPutRejectSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPutRejectFailure(t.message ?: "통신 오류")
            }

        })
    }
}


interface PutRejectWorkerRetrofitInterface {
    @PUT("/mypage/workers/{positionId}")
    fun putRejectOut(@Header("token")token:String, @Path(value = "positionId", encoded = false)positionId:Int): Call<BaseResponse>
}

interface PutRejectWorkerOutFragmentView {

    fun onPutRejectSuccess(response: BaseResponse)

    fun onPutRejectFailure(message: String)
}