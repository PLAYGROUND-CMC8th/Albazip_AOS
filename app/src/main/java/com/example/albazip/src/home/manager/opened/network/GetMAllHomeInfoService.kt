package com.example.albazip.src.home.manager.opened.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.src.home.manager.data.GetAllMHomeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class GetMAllHomeInfoService(val view: GetAllMFragmentView) {

    fun tryGetAllMHomeInfo(){
        val getAllMRetrofitInterface = ApplicationClass.sRetrofit.create(
            GetAllMRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        getAllMRetrofitInterface.getAllHomeInfo(token).enqueue(object :
            Callback<GetAllMHomeResponse> {
            override fun onResponse(call: Call<GetAllMHomeResponse>, response: Response<GetAllMHomeResponse>) {
                view.onGetAllMHomeSuccess(response.body() as GetAllMHomeResponse)
            }

            override fun onFailure(call: Call<GetAllMHomeResponse>, t: Throwable) {
                view.onGetAllMHomeFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface GetAllMRetrofitInterface {
    @GET("/home/manager")
    fun getAllHomeInfo(@Header("token")token:String): Call<GetAllMHomeResponse>
}

interface GetAllMFragmentView {

    fun onGetAllMHomeSuccess(response: GetAllMHomeResponse)

    fun onGetAllMHomeFailure(message: String)
}

