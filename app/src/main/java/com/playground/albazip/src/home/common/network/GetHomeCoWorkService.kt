package com.playground.albazip.src.home.common.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.src.home.common.data.HomeCoWorkResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class GetHomeCoWorkService(val view: GetHomeCoWorkFragmentView) {

    fun tryGetHomeCoWork(){
        val getHomeCoWorkRetrofitInterface = ApplicationClass.sRetrofit.create(
            GetHomeCoWorkRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        getHomeCoWorkRetrofitInterface.getHomeCoWork(token).enqueue(object :
            Callback<HomeCoWorkResponse> {
            override fun onResponse(call: Call<HomeCoWorkResponse>, response: Response<HomeCoWorkResponse>) {
                view.onGetHomeCoWorkSuccess(response.body() as HomeCoWorkResponse)
            }

            override fun onFailure(call: Call<HomeCoWorkResponse>, t: Throwable) {
                view.onGetHomeCoWorkFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface GetHomeCoWorkRetrofitInterface {
    @GET("/home/todayTask/coTask")
    fun getHomeCoWork(@Header("token")token:String): Call<HomeCoWorkResponse>
}

interface GetHomeCoWorkFragmentView {

    fun onGetHomeCoWorkSuccess(response: HomeCoWorkResponse)

    fun onGetHomeCoWorkFailure(message: String)
}
