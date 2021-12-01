package com.playground.albazip.src.register.common.network

import com.playground.albazip.src.register.common.data.remote.PhoneCheckResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PhoneCheckRetrofitInterface {
    @GET("/user/signup/{phoneNumber}")
    fun getBNum(@Path(value = "phoneNumber", encoded = false)phoneNumber:String): Call<PhoneCheckResponse>
}