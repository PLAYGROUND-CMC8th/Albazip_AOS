package com.example.albazip.src.mypage.manager.workerlist.network

import com.example.albazip.config.BaseResponse
import com.example.albazip.src.mypage.manager.workerlist.data.remote.PostAddWorkerRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AddWorkerRetrofitInterface {
    @POST("/position")
    fun postPosition(@Header("token")token:String, @Body params: PostAddWorkerRequest): Call<BaseResponse>
}