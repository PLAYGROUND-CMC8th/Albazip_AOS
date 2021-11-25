package com.example.albazip.src.mypage.worker.myInfo.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.src.mypage.common.workerdata.commute.data.GetCommuteInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class RecentCommuteInfoService(val view: RecentCommuteFragmentView) {

    fun tryGetRecentCommuteInfo(){
        val recentCommuteInfoRetrofitInterface = ApplicationClass.sRetrofit.create(RecentCommuteInfoRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        recentCommuteInfoRetrofitInterface.getWorkerInfo(token).enqueue(object :
            Callback<GetCommuteInfoResponse> {
            override fun onResponse(call: Call<GetCommuteInfoResponse>, response: Response<GetCommuteInfoResponse>) {
                view.onRecentCommuteInfoGetSuccess(response.body() as GetCommuteInfoResponse)
            }

            override fun onFailure(call: Call<GetCommuteInfoResponse>, t: Throwable) {
                view.onRecentCommuteInfoGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface RecentCommuteInfoRetrofitInterface {
    @GET("/mypage/myinfo/commuteInfo")
    fun getWorkerInfo(@Header("token")token:String): Call<GetCommuteInfoResponse>
}

interface RecentCommuteFragmentView {

    fun onRecentCommuteInfoGetSuccess(response: GetCommuteInfoResponse)

    fun onRecentCommuteInfoGetFailure(message: String)
}