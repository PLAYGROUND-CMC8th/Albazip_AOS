package com.example.albazip.src.register.manager.network

import com.example.albazip.src.register.manager.data.remote.BNumResponse
import retrofit2.Call
import retrofit2.http.*

interface BNumRetrofitInterface {
    @GET("/shop/number/exist/{registerNumber}")
    fun getBNum(@Path(value = "registerNumber", encoded = false)registerNumber:String): Call<BNumResponse>
}