package com.playground.albazip.src.register.common.network

import com.playground.albazip.config.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CommonSignUpService {

    // 휴대폰 인증번호 횟수 체크
    @GET("/user/signup/limit/{phoneNum}")
    fun getPhoneNumCheck(
        @Path(value = "phoneNum", encoded = true) phoneNum: String,
    ): Call<BaseResponse>
}