package com.playground.albazip.src.update.setworker.network

import com.playground.albazip.config.BaseResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MMyPageService {

    // 근무자 포지션 추가
    @POST("/position")
    fun postAddPosition(
        @Header("token") token: String,
        @Body params: RequestAddPosition
    ): Call<BaseResponse>
}