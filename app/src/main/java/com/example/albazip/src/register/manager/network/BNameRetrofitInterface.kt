package com.example.albazip.src.register.manager.network

import com.example.albazip.src.register.manager.data.remote.BNameResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BNameRetrofitInterface {
    @GET("/shop/number/match/{registerNumber}/{ownerName}")
    fun getBName(
        @Path(value = "registerNumber", encoded = false) registerNumber: String,
        @Path(value = "ownerName", encoded = false) ownerName: String
    ): Call<BNameResponse>
}