package com.example.albazip.src.register.common.network

import com.example.albazip.src.register.common.data.remote.PostSignUpRequest
import com.example.albazip.src.register.common.data.remote.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpRetrofitInterface {

    @POST("/user/signup")
    fun postSignUp(@Body params: PostSignUpRequest): Call<SignUpResponse>
}