package com.playground.albazip.src.home.manager.service

import com.playground.albazip.config.BaseResponse
import com.playground.albazip.src.home.manager.editshop.data.RequestEditShop
import com.playground.albazip.src.home.manager.editshop.data.ResponseEditShopInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface MHomeService {

    // 홈 편집 전 정보 불러오기
    @GET("/shop/{managerId}")
    fun getEditShopInfo(
        @Header("token") token: String,
        @Path("managerId") managerId: Int,
    ): Call<ResponseEditShopInfo>

    // 홈 편집
    @POST("/shop/{managerId}")
    fun postEditShopInfo(
        @Header("token") token: String,
        @Path("managerId") managerId: Int,
        @Body body: RequestEditShop
    ): Call<BaseResponse>

}