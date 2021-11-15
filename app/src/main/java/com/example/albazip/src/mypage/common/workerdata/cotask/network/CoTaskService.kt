package com.example.albazip.src.mypage.common.workerdata.cotask.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.src.mypage.common.workerdata.cotask.data.GetCoTaskInfoResponse
import com.example.albazip.src.mypage.manager.board.data.remote.GetBoardResponse
import com.example.albazip.src.mypage.manager.board.network.BoardFragmentView
import com.example.albazip.src.mypage.manager.board.network.BoardRetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class CoTaskService(val view: CoTaskFragmentView) {

    fun tryCoTask(){
        val coTaskRetrofitInterface = ApplicationClass.sRetrofit.create(CoTaskRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        coTaskRetrofitInterface.getCoTask(token).enqueue(object :
            Callback<GetCoTaskInfoResponse> {
            override fun onResponse(call: Call<GetCoTaskInfoResponse>, response: Response<GetCoTaskInfoResponse>) {
                view.onCoTaskGetSuccess(response.body() as GetCoTaskInfoResponse)
            }

            override fun onFailure(call: Call<GetCoTaskInfoResponse>, t: Throwable) {
                view.onCoTaskGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface CoTaskRetrofitInterface {
    @GET("/mypage/myinfo/taskInfo/coTaskInfo")
    fun getCoTask(@Header("token")token:String): Call<GetCoTaskInfoResponse>
}

interface CoTaskFragmentView {

    fun onCoTaskGetSuccess(response: GetCoTaskInfoResponse)

    fun onCoTaskGetFailure(message: String)
}