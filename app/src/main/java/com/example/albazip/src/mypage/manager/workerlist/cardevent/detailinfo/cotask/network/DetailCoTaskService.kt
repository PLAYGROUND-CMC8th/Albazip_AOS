package com.example.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.cotask.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.src.mypage.common.workerdata.cotask.data.GetCoTaskInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class DetailCoTaskService(val view: DetailCoTaskFragmentView) {

    fun tryGetDetailCoTask(positionId:Int) {
        val detailCoTaskRetrofitInterface =
            ApplicationClass.sRetrofit.create(DetailCoTaskRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        detailCoTaskRetrofitInterface.getDetailTogetherInfo(token,positionId).enqueue(object :
            Callback<GetCoTaskInfoResponse> {
            override fun onResponse(
                call: Call<GetCoTaskInfoResponse>,
                response: Response<GetCoTaskInfoResponse>
            ) {
                view.onCoTaskGetSuccess(response.body() as GetCoTaskInfoResponse)
            }

            override fun onFailure(call: Call<GetCoTaskInfoResponse>, t: Throwable) {
                view.onCoTaskGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface DetailCoTaskRetrofitInterface {
    @GET("/mypage/workers/{positionId}/workerInfo/coTaskInfo")
    fun getDetailTogetherInfo(
        @Header("token") token: String,
        @Path(value = "positionId", encoded = false) positionId: Int,
    ): Call<GetCoTaskInfoResponse>
}

interface DetailCoTaskFragmentView {

    fun onCoTaskGetSuccess(response: GetCoTaskInfoResponse)

    fun onCoTaskGetFailure(message: String)
}