package com.playground.albazip.src.home.manager.service

import com.playground.albazip.src.home.manager.editshop.data.ResponseEditShopInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface MHomeService {

    // 근무자 포지션 추가
    @GET("/shop/{managerId}")
    fun getEditShopInfo(
        @Header("token") token: String,
        @Path("managerId") managerId: Int,
    ): Call<ResponseEditShopInfo>
}