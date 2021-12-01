package com.playground.albazip.src.login.network

import com.playground.albazip.src.login.data.PostSignInRequest
import com.playground.albazip.src.login.data.SignInResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignInRetrofitInterface {

    @POST("/user/signin")
    fun postSignIn(@Body params: PostSignInRequest): Call<SignInResponse>
}