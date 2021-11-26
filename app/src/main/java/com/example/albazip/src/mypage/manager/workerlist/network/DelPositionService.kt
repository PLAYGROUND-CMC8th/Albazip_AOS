package com.example.albazip.src.mypage.manager.workerlist.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Path

class DelPositionService(val view: DelPositionFragmentView) {
    fun tryDelWorkerPosition(positionId: Int) {
        val delPositionRetrofitInterface =
            ApplicationClass.sRetrofit.create(DelPositionRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        delPositionRetrofitInterface.delWorkerPosition(token,positionId).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                view.onPositionDelSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPositionDelFailure(t.message ?: "통신 오류")
            }

        })
    }
}


interface DelPositionRetrofitInterface {
    @DELETE("/position/{positionId}")
    fun delWorkerPosition(
        @Header("token") token: String,
        @Path(value = "positionId", encoded = false) positionId: Int
    ): Call<BaseResponse>
}

interface DelPositionFragmentView {

    fun onPositionDelSuccess(response: BaseResponse)

    fun onPositionDelFailure(message: String)
}

