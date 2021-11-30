package com.playground.albazip.src.home.manager.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.src.home.manager.data.HomeMPerWorkResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header


class GetHomeMPerService(val view: GetHomeMPerServiceFragmentView) {

    fun tryGetHomeMPer(){
        val getHomeMPerRetrofitInterface = ApplicationClass.sRetrofit.create(
            GetHomeMPerRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        getHomeMPerRetrofitInterface.getHomeMPerWork(token).enqueue(object :
            Callback<HomeMPerWorkResponse> {
            override fun onResponse(call: Call<HomeMPerWorkResponse>, response: Response<HomeMPerWorkResponse>) {
                view.onGetMPerWorkSuccess(response.body() as HomeMPerWorkResponse)
            }

            override fun onFailure(call: Call<HomeMPerWorkResponse>, t: Throwable) {
                view.onGetMPerWorkFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface GetHomeMPerRetrofitInterface {
    @GET("/home/todayTask/manager/workerPerTask")
    fun getHomeMPerWork(@Header("token")token:String): Call<HomeMPerWorkResponse>
}

interface GetHomeMPerServiceFragmentView {

    fun onGetMPerWorkSuccess(response: HomeMPerWorkResponse)

    fun onGetMPerWorkFailure(message: String)
}
