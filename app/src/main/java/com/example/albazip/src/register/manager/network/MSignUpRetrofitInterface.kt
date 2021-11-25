package com.example.albazip.src.register.manager.network

import com.example.albazip.config.BaseResponse
import com.example.albazip.src.register.common.data.remote.PositionRegisterResponse
import com.example.albazip.src.register.manager.data.remote.PostMSignUpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MSignUpRetrofitInterface {
    @POST("/user/signup/manager")
    fun postSignUp(@Header("token")token:String, @Body params: PostMSignUpRequest): Call<PositionRegisterResponse>
}