package com.playground.albazip.src.home.worker.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.src.home.worker.data.GetAllWHomeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class GetAllWHomeInfoService(val view: GetAllWFragmentView) {

    fun tryGetAllWHomeInfo(){
        val getAllWRetrofitInterface = ApplicationClass.sRetrofit.create(
            GetAllWRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        getAllWRetrofitInterface.getAllHomeInfo(token).enqueue(object :
            Callback<GetAllWHomeResponse> {
            override fun onResponse(call: Call<GetAllWHomeResponse>, response: Response<GetAllWHomeResponse>) {
                view.onGetAllWHomeSuccess(response.body() as GetAllWHomeResponse)
            }

            override fun onFailure(call: Call<GetAllWHomeResponse>, t: Throwable) {
                view.onGetAllWHomeFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface GetAllWRetrofitInterface {
    @GET("/home/worker")
    fun getAllHomeInfo(@Header("token")token:String): Call<GetAllWHomeResponse>
}

interface GetAllWFragmentView {

    fun onGetAllWHomeSuccess(response: GetAllWHomeResponse)

    fun onGetAllWHomeFailure(message: String)
}