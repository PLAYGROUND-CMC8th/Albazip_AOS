package com.example.albazip.src.login.network

import com.example.albazip.src.login.data.PostSignInRequest
import com.example.albazip.src.login.data.SignInResponse
import com.example.albazip.src.register.common.data.remote.PostSignUpRequest
import com.example.albazip.src.register.common.data.remote.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignInRetrofitInterface {

    @POST("/user/signin")
    fun postSignIn(@Body params: PostSignInRequest): Call<SignInResponse>
}