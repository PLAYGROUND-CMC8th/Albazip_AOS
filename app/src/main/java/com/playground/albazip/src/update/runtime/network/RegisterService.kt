package com.playground.albazip.src.update.runtime.network

import com.playground.albazip.src.update.runtime.network.RequestMSignUp
import com.playground.albazip.src.update.runtime.network.ResponseMSignUp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RegisterService{

    // 관리자 회원가입
    @POST("/user/signup/manager")
    fun postSignUpManager(@Header("token")token:String, @Body params: RequestMSignUp): Call<ResponseMSignUp>
}